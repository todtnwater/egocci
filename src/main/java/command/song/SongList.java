package command.song;

import java.util.ArrayList;

import common.CommonExecute;
import dao.SongDao;
import dto.SongDto;
import jakarta.servlet.http.HttpServletRequest;

public class SongList implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        SongDao dao = new SongDao();
        String songtype = request.getParameter("songtype");
        String field = request.getParameter("field");
        String keyword = request.getParameter("keyword");
        if(keyword == null) {
        	keyword = "";
        }
        ArrayList<SongDto> albumSongs = dao.getSongsByAlbum("album");
        ArrayList<SongDto> epSongs = dao.getSongsByAlbum("ep");
        ArrayList<SongDto> singleSongs = dao.getSongsByAlbum("single");

        request.setAttribute("albumSongs", albumSongs);
        request.setAttribute("epSongs", epSongs);
        request.setAttribute("singleSongs", singleSongs);
    }
}
