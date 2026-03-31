package controller;

import java.io.IOException;

import dao.MemberDao;
import dto.MemberDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Manager")
public class Manager extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (!checkAdmin(request)) {
            setMessage(request, "관리자만 접근할 수 있습니다.", "Index");
            request.getRequestDispatcher("common_alert.jsp").forward(request, response);
            return;
        }

        String gubun = request.getParameter("t_gubun");
        if (gubun == null) {
			gubun = "list";
		}

        MemberDao dao = new MemberDao();
        String viewPage = "";

        if ("list".equals(gubun)) {
            request.setAttribute("t_dtos", dao.getMemberList());
            viewPage = "manager/member_list.jsp";

        } else if ("view".equals(gubun)) {
            String email = request.getParameter("t_email");
            MemberDto dto = dao.getMemberView(email);

            if (dto == null) {
                setMessage(request, "해당 회원 정보를 찾을 수 없습니다.", "Manager?t_gubun=list");
                viewPage = "common_alert.jsp";
            } else {
                request.setAttribute("t_dto", dto);
                viewPage = "manager/member_view.jsp";
            }

        } else {
            setMessage(request, "잘못된 요청입니다.", "Manager?t_gubun=list");
            viewPage = "common_alert.jsp";
        }

        RequestDispatcher rd = request.getRequestDispatcher(viewPage);
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (!checkAdmin(request)) {
            setMessage(request, "관리자만 접근할 수 있습니다.", "Index");
            request.getRequestDispatcher("common_alert.jsp").forward(request, response);
            return;
        }

        String gubun = request.getParameter("t_gubun");
        if (gubun == null) {
			gubun = "list";
		}

        MemberDao dao = new MemberDao();
        String viewPage = "common_alert.jsp";

        if ("update".equals(gubun)) {
            MemberDto dto = new MemberDto();
            dto.setEmail(request.getParameter("t_email"));
            dto.setName(request.getParameter("t_name"));
            dto.setPhone1(request.getParameter("t_phone1"));
            dto.setPhone2(request.getParameter("t_phone2"));
            dto.setPhone3(request.getParameter("t_phone3"));
            dto.setGender(request.getParameter("t_gender"));
            dto.setRole(request.getParameter("t_role"));

            int result = dao.updateMemberByAdmin(dto);

            if (result > 0) {
                setMessage(request, "회원 정보가 수정되었습니다.", "Manager?t_gubun=view&t_email=" + dto.getEmail());
            } else {
                setMessage(request, "회원 정보 수정에 실패했습니다.", "Manager?t_gubun=view&t_email=" + dto.getEmail());
            }

        } else if ("delete".equals(gubun)) {
            String email = request.getParameter("t_email");
            int result = dao.deleteMemberByAdmin(email);

            if (result > 0) {
                setMessage(request, "회원이 삭제 처리되었습니다.", "Manager?t_gubun=list");
            } else {
                setMessage(request, "회원 삭제에 실패했습니다.", "Manager?t_gubun=view&t_email=" + email);
            }

        } else {
            setMessage(request, "잘못된 요청입니다.", "Manager?t_gubun=list");
        }

        request.getRequestDispatcher(viewPage).forward(request, response);
    }

    private boolean checkAdmin(HttpServletRequest request) {
        String level = (String) request.getSession().getAttribute("sessionLevel");
        return "top".equals(level);
    }

    private void setMessage(HttpServletRequest request, String msg, String url) {
        request.setAttribute("t_msg", msg);
        request.setAttribute("t_url", url);
    }
}