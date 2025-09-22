
package command.song;

import javax.servlet.http.HttpServletRequest;
import common.CommonExecute;
import common.CommonUtil;
import dao.SongDao;
import dto.SongDto;

public class SongUpdate implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        SongDao dao = new SongDao();
        
        String no = request.getParameter("t_no");
        String title = request.getParameter("t_song_title");
        String artist = request.getParameter("t_artist_name");
        String genre = request.getParameter("t_genre");
        String lyrics = request.getParameter("t_lyrics");
        
        title = CommonUtil.setQuote(title);
        lyrics = CommonUtil.setQuote(lyrics);
        
        SongDto dto = new SongDto(no, title, artist, genre, lyrics);
        int result = dao.songUpdate(dto);
        
        String msg = result == 1 ? "곡이 수정되었습니다." : "수정 실패!";
        request.setAttribute("t_msg", msg);
        request.setAttribute("t_url", "Song?t_gubun=view&t_no=" + no);
    }
}