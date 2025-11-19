package command.song;

import javax.servlet.http.HttpServletRequest;
import common.CommonExecute;
import dao.SongDao;

public class SongDelete implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        SongDao dao = new SongDao();
        
        try {
            String noStr = request.getParameter("t_no");
            int songId = Integer.parseInt(noStr);
            
            boolean result = dao.deleteSong(songId);
            
            String msg = result ? "곡이 삭제되었습니다." : "삭제 실패!";
            request.setAttribute("t_msg", msg);
            request.setAttribute("t_url", "Song?t_gubun=list");
            
        } catch (NumberFormatException e) {
            System.err.println("SongDelete 숫자 변환 오류: " + e.getMessage());
            request.setAttribute("t_msg", "유효하지 않은 곡 ID입니다.");
            request.setAttribute("t_url", "Song?t_gubun=list");
        } catch (Exception e) {
            System.err.println("SongDelete 오류: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("t_msg", "삭제 중 오류가 발생했습니다.");
            request.setAttribute("t_url", "Song?t_gubun=list");
        }
    }
}