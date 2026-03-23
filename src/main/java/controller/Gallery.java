package controller;

import java.io.IOException;

import command.gallery.GalleryDelete;
import command.gallery.GalleryList;
import command.gallery.GallerySave;
import common.CommonExecute;
import dao.GalleryDao;
import dto.GalleryDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize       = 1024 * 1024 * 10,
    maxRequestSize    = 1024 * 1024 * 20
)
@WebServlet("/Gallery")
public class Gallery extends HttpServlet {
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
            CommonExecute cmd = new GalleryList();
            cmd.execute(request);
            viewPage = "gallery/gallery_list.jsp";
        } else {
            request.setAttribute("t_msg", "잘못된 접근입니다.");
            request.setAttribute("t_url", "Gallery?t_gubun=list");
            viewPage = "common/alert.jsp";
        }

        request.getRequestDispatcher(viewPage).forward(request, response);
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
            CommonExecute cmd = new GalleryList();
            cmd.execute(request);
            viewPage = "gallery/gallery_list.jsp";

        } else if (gubun.equals("writeForm")) {
            if (checkAdmin(request)) {
                viewPage = "gallery/gallery_write.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("save")) {
            if (checkAdmin(request)) {
                CommonExecute cmd = new GallerySave();
                cmd.execute(request);
                viewPage = "common/alert.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("view")) {
            String noStr = request.getParameter("t_no");
            if (noStr != null && !noStr.trim().isEmpty()) {
                GalleryDao dao = new GalleryDao();
                GalleryDto dto = dao.getGalleryView(Integer.parseInt(noStr));
                request.setAttribute("t_dto", dto);
            }
            viewPage = "gallery/gallery_view.jsp";

        } else if (gubun.equals("delete")) {
            if (checkAdmin(request)) {
                CommonExecute cmd = new GalleryDelete();
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

        request.getRequestDispatcher(viewPage).forward(request, response);
    }

    private boolean checkAdmin(HttpServletRequest request) {
        String level = (String) request.getSession().getAttribute("sessionLevel");
        return "top".equals(level);
    }

    private void setError(HttpServletRequest request, String message) {
        request.setAttribute("t_msg", message);
        request.setAttribute("t_url", "Gallery?t_gubun=list");
    }
}
