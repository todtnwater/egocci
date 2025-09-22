package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import command.song.*;
import common.CommonExecute;
import common.CommonUtil;
import dao.AlbumDao;
import dto.AlbumDto;

@WebServlet("/Song")
public class Song extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Song() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String gubun = request.getParameter("t_gubun");
        if(gubun == null) gubun = "list";
        String viewPage = "";
        
        // GET 방식으로만 처리할 기능들
        if(gubun.equals("list")) {
            // 검색 기능은 GET 유지 (북마크, 공유 가능)
            CommonExecute song = new SongList();
            song.execute(request);
            viewPage = "song/song_list.jsp";
        } 
        // AJAX 처리 - 앨범 목록 조회 (GET 유지)
        else if(gubun.equals("getAlbumList")) {
            try {
                AlbumDao dao = new AlbumDao();
                String albumType = request.getParameter("type");
                ArrayList<AlbumDto> albums = dao.getAlbumsByType(albumType);
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                
                // JSON 응답 생성 (Gson 없이)
                StringBuilder json = new StringBuilder("[");
                for(int i = 0; i < albums.size(); i++) {
                    AlbumDto album = albums.get(i);
                    if(i > 0) json.append(",");
                    json.append("{")
                        .append("\"album_id\":\"").append(album.getAlbum_id()).append("\",")
                        .append("\"album_title\":\"").append(album.getAlbum_title()).append("\",")
                        .append("\"album_number\":\"").append(album.getAlbum_number()).append("\"")
                        .append("}");
                }
                json.append("]");
                
                PrintWriter out = response.getWriter();
                out.print(json.toString());
                out.flush();
                return;
                
            } catch (Exception e) {
                System.out.println("GetAlbumList 오류: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        }
        // GET으로 온 다른 요청들은 POST로 리다이렉트
        else {
            // GET으로 온 데이터 변경 요청들은 POST로 리다이렉트하거나 오류 처리
            request.setAttribute("t_msg", "잘못된 접근 방식입니다. POST 방식을 사용해주세요.");
            request.setAttribute("t_url", "Song");
            viewPage = "common/alert.jsp";
        }
        
        // 페이지 이동
        if(!viewPage.equals("")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // POST 방식 한글 인코딩 설정
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String gubun = request.getParameter("t_gubun");
        if(gubun == null) gubun = "list";
        String viewPage = "";
        
        // POST 방식으로만 처리할 기능들 (데이터 변경 작업)
        if(gubun.equals("view")) {
            CommonExecute song = new SongView();
            song.execute(request);
            viewPage = "song/song_view.jsp";
        } else if(gubun.equals("writeForm")) {
            String level = CommonUtil.getSessionInfo(request, "level");
            if(!level.equals("top")) {
                request.setAttribute("t_msg", "잘못된 접근입니다.");
                request.setAttribute("t_url", "Song");
                viewPage = "common/alert.jsp";
            } else {
                request.setAttribute("toDay", CommonUtil.getToday());
                viewPage = "song/song_write.jsp";
            }
        } else if(gubun.equals("save")) {
            String level = CommonUtil.getSessionInfo(request, "level");
            if(!level.equals("top")) {
                request.setAttribute("t_msg", "잘못된 접근입니다.");
                request.setAttribute("t_url", "Song");
                viewPage = "common/alert.jsp";
            } else {
                CommonExecute song = new SongSave();
                song.execute(request);
                viewPage = "common/alert.jsp";
            }
        } else if(gubun.equals("updateForm")) {
            String level = CommonUtil.getSessionInfo(request, "level");
            if(!level.equals("top")) {
                request.setAttribute("t_msg", "잘못된 접근입니다.");
                request.setAttribute("t_url", "Song");
                viewPage = "common/alert.jsp";
            } else {
                CommonExecute song = new SongView();
                song.execute(request);
                request.setAttribute("t_gubun", "edit");
                viewPage = "song/song_update.jsp";
            }
        } else if(gubun.equals("update")) {
            String level = CommonUtil.getSessionInfo(request, "level");
            if(!level.equals("top")) {
                request.setAttribute("t_msg", "잘못된 접근입니다.");
                request.setAttribute("t_url", "Song");
                viewPage = "common/alert.jsp";
            } else {
                CommonExecute song = new SongUpdate();
                song.execute(request);
                viewPage = "common/alert.jsp";
            }
        } else if(gubun.equals("delete")) {
            String level = CommonUtil.getSessionInfo(request, "level");
            if(!level.equals("top")) {
                request.setAttribute("t_msg", "잘못된 접근입니다.");
                request.setAttribute("t_url", "Song");
                viewPage = "common/alert.jsp";
            } else {
                CommonExecute song = new SongDelete();
                song.execute(request);
                viewPage = "common/alert.jsp";
            }
        } else if(gubun.equals("list")) {
            // POST로 온 list 요청 처리 (목록으로 돌아가기 버튼 등)
            CommonExecute song = new SongList();
            song.execute(request);
            viewPage = "song/song_list.jsp";
        } else {
            // 알 수 없는 요청
            request.setAttribute("t_msg", "잘못된 요청입니다.");
            request.setAttribute("t_url", "Song");
            viewPage = "common/alert.jsp";
        }
        
        // 페이지 이동
        if(!viewPage.equals("")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
            dispatcher.forward(request, response);
        }
    }
}