package controller;

import java.io.IOException;

import command.video.VideoDelete;
import command.video.VideoList;
import command.video.VideoSave;
import command.video.VideoUpdate;
import common.CommonExecute;
import dao.VideoDao;
import dto.VideoDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Video")
public class Video extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String gubun = request.getParameter("t_gubun");
        if (gubun == null) {
			gubun = "list";
		}
        String viewPage = "";

        if (gubun.equals("list")) {
            CommonExecute cmd = new VideoList();
            cmd.execute(request);
            viewPage = "video/video_list.jsp";
        } else {
            request.setAttribute("t_msg", "잘못된 접근 방식입니다.");
            request.setAttribute("t_url", "Video?t_gubun=list");
            viewPage = "common/alert.jsp";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String gubun = request.getParameter("t_gubun");
        if (gubun == null) {
			gubun = "list";
		}
        String viewPage = "";

        if (gubun.equals("list")) {
            CommonExecute cmd = new VideoList();
            cmd.execute(request);
            viewPage = "video/video_list.jsp";

        } else if (gubun.equals("writeForm")) {
            if (checkAdmin(request)) {
                viewPage = "video/video_write.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("save")) {
            if (checkAdmin(request)) {
                CommonExecute cmd = new VideoSave();
                cmd.execute(request);
                viewPage = "common/alert.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("updateForm")) {
            if (checkAdmin(request)) {
                String noStr = request.getParameter("t_no");
                if (noStr != null && !noStr.trim().isEmpty()) {
                    VideoDao dao = new VideoDao();
                    VideoDto dto = dao.getVideoView(Integer.parseInt(noStr));
                    request.setAttribute("t_dto", dto);
                }
                viewPage = "video/video_update.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("update")) {
            if (checkAdmin(request)) {
                CommonExecute cmd = new VideoUpdate();
                cmd.execute(request);
                viewPage = "common/alert.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("delete")) {
            if (checkAdmin(request)) {
                CommonExecute cmd = new VideoDelete();
                cmd.execute(request);
                viewPage = "common/alert.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else {
            setError(request, "잘못된 요청입니다.");
            viewPage = "common/alert.jsp";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
        dispatcher.forward(request, response);
    }

    private boolean checkAdmin(HttpServletRequest request) {
        String level = (String) request.getSession().getAttribute("sessionLevel");
        return "top".equals(level);
    }

    private void setError(HttpServletRequest request, String message) {
        request.setAttribute("t_msg", message);
        request.setAttribute("t_url", "Video?t_gubun=list");
    }
}
