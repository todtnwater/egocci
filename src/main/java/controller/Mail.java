package controller;

import java.io.IOException;
import java.util.ArrayList;

import common.MailUtil;
import dao.BusinessMailDao;
import dto.BusinessMailDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Mail")
public class Mail extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String RECEIVER_EMAIL = "korangemgmt@gmail.com";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String gubun = request.getParameter("t_gubun");
        if(gubun == null) {
			gubun = "writeForm";
		}

        BusinessMailDao dao = new BusinessMailDao();
        String viewPage = "";

        if(gubun.equals("writeForm")) {
            viewPage = "mail/mail_write.jsp";

        } else if(gubun.equals("adminList")) {
            if(!isAdmin(request)) {
                request.setAttribute("t_msg", "관리자만 접근 가능합니다.");
                request.setAttribute("t_url", "Index");
                viewPage = "common_alert.jsp";
            } else {
                ArrayList<BusinessMailDto> list = dao.getMailList();
                request.setAttribute("t_dtos", list);
                viewPage = "mail/admin_mail_list.jsp";
            }

        } else if(gubun.equals("adminView")) {
            if(!isAdmin(request)) {
                request.setAttribute("t_msg", "관리자만 접근 가능합니다.");
                request.setAttribute("t_url", "Index");
                viewPage = "common_alert.jsp";
            } else {
                int mailId = Integer.parseInt(request.getParameter("t_mail_id"));
                BusinessMailDto dto = dao.getMailView(mailId);
                request.setAttribute("t_dto", dto);
                viewPage = "mail/admin_mail_view.jsp";
            }

        } else if(gubun.equals("adminDelete")) {
            if(!isAdmin(request)) {
                request.setAttribute("t_msg", "관리자만 접근 가능합니다.");
                request.setAttribute("t_url", "Index");
                viewPage = "common_alert.jsp";
            } else {
                int mailId = Integer.parseInt(request.getParameter("t_mail_id"));
                int result = dao.deleteMail(mailId);

                if(result > 0) {
                    request.setAttribute("t_msg", "메일 기록이 삭제 처리되었습니다.");
                } else {
                    request.setAttribute("t_msg", "메일 기록 삭제에 실패했습니다.");
                }
                request.setAttribute("t_url", "Mail?t_gubun=adminList");
                viewPage = "common_alert.jsp";
            }

        } else {
            request.setAttribute("t_msg", "잘못된 접근입니다.");
            request.setAttribute("t_url", "Index");
            viewPage = "common_alert.jsp";
        }

        RequestDispatcher rd = request.getRequestDispatcher(viewPage);
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String gubun = request.getParameter("t_gubun");
        if(gubun == null) {
			gubun = "";
		}

        BusinessMailDao dao = new BusinessMailDao();
        String viewPage = "common_alert.jsp";

        if(gubun.equals("send")) {
            String sessionId = (String)request.getSession().getAttribute("sessionId");
            String userName = (String)request.getSession().getAttribute("userName");

            if(sessionId == null || userName == null) {
                request.setAttribute("t_msg", "로그인 후 이용 가능합니다.");
                request.setAttribute("t_url", "Member");
                request.getRequestDispatcher(viewPage).forward(request, response);
                return;
            }

            String subject = request.getParameter("t_subject");
            String content = request.getParameter("t_content");

            if(subject == null || subject.trim().equals("") ||
               content == null || content.trim().equals("")) {
                request.setAttribute("t_msg", "제목과 내용을 입력하세요.");
                request.setAttribute("t_url", "Mail?t_gubun=writeForm");
                request.getRequestDispatcher(viewPage).forward(request, response);
                return;
            }

            BusinessMailDto dto = new BusinessMailDto();
            dto.setSenderName(userName);
            dto.setSenderEmail(sessionId);
            dto.setRecipientEmail(RECEIVER_EMAIL);
            dto.setSubject(subject);
            dto.setContent(content);

            int mailId = dao.saveMail(dto);

            if(mailId <= 0) {
                request.setAttribute("t_msg", "메일 기록 저장에 실패했습니다.");
                request.setAttribute("t_url", "Mail?t_gubun=writeForm");
                request.getRequestDispatcher(viewPage).forward(request, response);
                return;
            }

            boolean sendResult = MailUtil.sendMail(RECEIVER_EMAIL, userName, sessionId, subject, content);

            if(sendResult) {
                dao.updateSendSuccess(mailId);
                request.setAttribute("t_msg", "메일이 정상적으로 전송되었습니다.");
                request.setAttribute("t_url", "Index");
            } else {
                dao.updateSendFail(mailId, "SMTP 전송 실패");
                request.setAttribute("t_msg", "메일 전송에 실패했습니다.");
                request.setAttribute("t_url", "Mail?t_gubun=writeForm");
            }

        } else {
            request.setAttribute("t_msg", "잘못된 요청입니다.");
            request.setAttribute("t_url", "Index");
        }

        request.getRequestDispatcher(viewPage).forward(request, response);
    }

    private boolean isAdmin(HttpServletRequest request) {
        String sessionLevel = (String)request.getSession().getAttribute("sessionLevel");
        return sessionLevel != null && sessionLevel.equals("top");
    }
}