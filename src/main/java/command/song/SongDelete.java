package command.song;

import javax.servlet.http.HttpServletRequest;
import common.CommonExecute;
import dao.SongDao;

public class SongDelete implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        SongDao dao = new SongDao();
        String no = request.getParameter("t_no");
        int result = dao.songDelete(no);
        
        String msg = result == 1 ? "곡이 삭제되었습니다." : "삭제 실패!";
        request.setAttribute("t_msg", msg);
        request.setAttribute("t_url", "Song");
    }
}