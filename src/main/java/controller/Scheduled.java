package controller;

import java.io.IOException;

import command.scheduled.ScheduledDelete;
import command.scheduled.ScheduledList;
import command.scheduled.ScheduledSave;
import command.scheduled.ScheduledUpdate;
import command.scheduled.ScheduledView;
import common.CommonExecute;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize       = 1024 * 1024 * 10,
    maxRequestSize    = 1024 * 1024 * 15
)
@WebServlet("/Scheduled")
public class Scheduled extends HttpServlet {
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
            CommonExecute sc = new ScheduledList();
            sc.execute(request);
            viewPage = "scheduled/scheduled_list.jsp";
        } else {
            request.setAttribute("t_msg", "잘못된 접근입니다.");
            request.setAttribute("t_url", "Scheduled");
            viewPage = "common/alert.jsp";
        }

        RequestDispatcher rd = request.getRequestDispatcher(viewPage);
        rd.forward(request, response);
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
            CommonExecute sc = new ScheduledList();
            sc.execute(request);
            viewPage = "scheduled/scheduled_list.jsp";

        } else if (gubun.equals("writeForm")) {
            if (checkAdmin(request)) {
                viewPage = "scheduled/scheduled_write.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("save")) {
            if (checkAdmin(request)) {
                CommonExecute sc = new ScheduledSave();
                sc.execute(request);
                viewPage = "common/alert.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("updateForm")) {
            if (checkAdmin(request)) {
                CommonExecute sc = new ScheduledView();
                sc.execute(request);
                viewPage = "scheduled/scheduled_update.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("update")) {
            if (checkAdmin(request)) {
                CommonExecute sc = new ScheduledUpdate();
                sc.execute(request);
                viewPage = "common/alert.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("delete")) {
            if (checkAdmin(request)) {
                CommonExecute sc = new ScheduledDelete();
                sc.execute(request);
                viewPage = "common/alert.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else {
            setError(request, "잘못된 요청입니다.");
            viewPage = "common/alert.jsp";
        }

        RequestDispatcher rd = request.getRequestDispatcher(viewPage);
        rd.forward(request, response);
    }

    private boolean checkAdmin(HttpServletRequest request) {
        String level = (String) request.getSession().getAttribute("sessionLevel");
        return "top".equals(level);
    }

    private void setError(HttpServletRequest request, String message) {
        request.setAttribute("t_msg", message);
        request.setAttribute("t_url", "Scheduled");
    }
}
