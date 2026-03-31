package controller;

import java.io.IOException;
import java.util.ArrayList;

import command.song.SongDelete;
import command.song.SongList;
import command.song.SongSave;
import command.song.SongUpdate;
import command.song.SongView;
import common.CommonExecute;
import dao.AlbumDao;
import dto.AlbumDto;
import dto.SongDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 10
)
@WebServlet("/Song")
public class Song extends HttpServlet {
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
            CommonExecute song = new SongList();
            song.execute(request);
            viewPage = "song/song_list.jsp";

        } else if (gubun.equals("getAlbumList")) {
            handleAlbumListAjax(request, response);
            return;

        } else {
            request.setAttribute("t_msg", "잘못된 접근 방식입니다.");
            request.setAttribute("t_url", "Song?t_gubun=list");
            viewPage = "common/alert.jsp";
        }

        if (!viewPage.equals("")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
            dispatcher.forward(request, response);
        }
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
            CommonExecute song = new SongList();
            song.execute(request);
            viewPage = "song/song_list.jsp";

        } else if (gubun.equals("view")) {
            CommonExecute song = new SongView();
            song.execute(request);
            viewPage = "song/song_view.jsp";

        } else if (gubun.equals("writeForm")) {
            if (checkAdmin(request)) {
                viewPage = "song/song_write.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("save")) {
            if (checkAdmin(request)) {
                CommonExecute song = new SongSave();
                song.execute(request);
                viewPage = "common/alert.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("updateForm")) {
            if (checkAdmin(request)) {
                CommonExecute song = new SongView();
                song.execute(request);
                viewPage = "song/song_update.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("update")) {
            if (checkAdmin(request)) {
                CommonExecute song = new SongUpdate();
                song.execute(request);
                viewPage = "common/alert.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("delete")) {
            if (checkAdmin(request)) {
                CommonExecute song = new SongDelete();
                song.execute(request);
                viewPage = "common/alert.jsp";
            } else {
                setError(request, "권한이 없습니다.");
                viewPage = "common/alert.jsp";
            }

        } else if (gubun.equals("viewAjax")) {
            handleSongViewAjax(request, response);
            return;

        } else {
            setError(request, "잘못된 요청입니다.");
            viewPage = "common/alert.jsp";
        }

        if (!viewPage.equals("")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
            dispatcher.forward(request, response);
        }
    }

    private void handleAlbumListAjax(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String albumType = request.getParameter("type");
            if (albumType == null || albumType.trim().isEmpty()) {
                response.setStatus(400);
                return;
            }

            AlbumDao dao = new AlbumDao();
            ArrayList<AlbumDto> albums = dao.getAlbumsByType(albumType);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < albums.size(); i++) {
                if (i > 0) {
                    json.append(",");
                }
                AlbumDto album = albums.get(i);
                json.append("{")
                    .append("\"album_id\":").append(album.getAlbum_id()).append(",")
                    .append("\"album_title\":\"").append(escapeJson(album.getAlbum_title())).append("\",")
                    .append("\"album_number\":\"").append(escapeJson(album.getAlbum_number())).append("\"")
                    .append("}");
            }
            json.append("]");

            response.getWriter().print(json.toString());

        } catch (Exception e) {
            System.err.println("앨범 목록 조회 오류: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(500);
        }
    }

    private void handleSongViewAjax(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String songIdStr = request.getParameter("t_no");
            if (songIdStr == null || songIdStr.trim().isEmpty()) {
                sendErrorJson(response, "곡 ID가 필요합니다.");
                return;
            }

            int songId = Integer.parseInt(songIdStr);

            dao.SongDao songDao = new dao.SongDao();
            SongDto song = songDao.getSongView(songId);
            ArrayList<String[]> streamingLinks = songDao.getStreamingLinks(songId);

            if (song != null) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                StringBuilder json = new StringBuilder("{");
                json.append("\"success\":true,");
                json.append("\"song\":{");
                json.append("\"song_id\":").append(song.getSong_id()).append(",");
                json.append("\"song_title\":\"").append(escapeJson(song.getSong_title())).append("\",");
                json.append("\"artist_name\":\"").append(escapeJson(song.getArtist_name())).append("\",");
                json.append("\"genre\":\"").append(escapeJson(song.getGenre())).append("\",");
                json.append("\"lyrics\":\"").append(escapeJson(song.getLyrics())).append("\",");
                json.append("\"behind_note\":\"").append(escapeJson(song.getBehind_note())).append("\",");
                json.append("\"duration\":\"").append(escapeJson(song.getDuration())).append("\",");
                json.append("\"created_at\":\"").append(escapeJson(song.getCreated_at())).append("\",");
                json.append("\"updated_at\":\"").append(escapeJson(song.getUpdated_at())).append("\",");
                json.append("\"is_title_track\":").append(song.getIs_title_track()).append(",");
                json.append("\"song_cover_image\":\"").append(escapeJson(song.getSong_cover_image())).append("\",");
                json.append("\"album_title\":\"").append(escapeJson(song.getAlbum_title())).append("\",");
                json.append("\"album_type\":\"").append(escapeJson(song.getAlbum_type())).append("\",");
                json.append("\"album_number\":\"").append(escapeJson(song.getAlbum_number())).append("\",");
                json.append("\"release_date\":\"").append(escapeJson(song.getRelease_date())).append("\",");
                json.append("\"track_number\":").append(song.getTrack_number());
                json.append("},");

                json.append("\"streamingLinks\":[");
                if (streamingLinks != null && !streamingLinks.isEmpty()) {
                    boolean first = true;
                    for (String[] link : streamingLinks) {
                        if (link != null && link.length >= 2 && link[0] != null && link[1] != null) {
                            if (!first) {
                                json.append(",");
                            }
                            first = false;
                            json.append("{");
                            json.append("\"platform\":\"").append(escapeJson(link[0])).append("\",");
                            json.append("\"url\":\"").append(escapeJson(link[1])).append("\"");
                            json.append("}");
                        }
                    }
                }
                json.append("]}");

                response.getWriter().print(json.toString());
            } else {
                sendErrorJson(response, "곡을 찾을 수 없습니다.");
            }

        } catch (NumberFormatException e) {
            sendErrorJson(response, "유효하지 않은 곡 ID입니다.");
        } catch (Exception e) {
            System.err.println("AJAX 곡 상세 조회 오류: " + e.getMessage());
            e.printStackTrace();
            sendErrorJson(response, "서버 오류가 발생했습니다.");
        }
    }

    private boolean checkAdmin(HttpServletRequest request) {
        String level = (String) request.getSession().getAttribute("sessionLevel");
        return "top".equals(level);
    }

    private void setError(HttpServletRequest request, String message) {
        request.setAttribute("t_msg", message);
        request.setAttribute("t_url", "Song?t_gubun=list");
    }

    private void sendErrorJson(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(400);
        response.getWriter().print("{\"success\":false,\"error\":\"" + escapeJson(message) + "\"}");
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}