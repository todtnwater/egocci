package command.song;

import java.util.ArrayList;

import common.CommonExecute;
import dao.SongDao;
import dto.SongDto;
import jakarta.servlet.http.HttpServletRequest;

public class SongView implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        SongDao dao = new SongDao();

        try {
            String noStr = request.getParameter("t_no");
            int songId = Integer.parseInt(noStr);

            SongDto dto = dao.getSongView(songId);

            ArrayList<String[]> streamingLinks = dao.getStreamingLinks(songId);

            request.setAttribute("t_dto", dto);
            request.setAttribute("streamingLinks", streamingLinks);

        } catch (NumberFormatException e) {
            System.err.println("SongView 숫자 변환 오류: " + e.getMessage());
            request.setAttribute("t_dto", null);
            request.setAttribute("streamingLinks", new ArrayList<>());
        } catch (Exception e) {
            System.err.println("SongView 오류: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("t_dto", null);
            request.setAttribute("streamingLinks", new ArrayList<>());
        }
    }
}
