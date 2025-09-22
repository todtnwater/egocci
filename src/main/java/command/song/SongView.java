package command.song;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import common.CommonExecute;
import dao.SongDao;
import dto.SongDto;

public class SongView implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        SongDao dao = new SongDao();
        String no = request.getParameter("t_no");
        
        // 곡 상세 정보 조회
        SongDto dto = dao.getSongView(no);
        
        // 스트리밍 링크도 함께 조회
        ArrayList<String[]> streamingLinks = dao.getStreamingLinks(no);
        
        request.setAttribute("t_dto", dto);
        request.setAttribute("streamingLinks", streamingLinks);
    }
}