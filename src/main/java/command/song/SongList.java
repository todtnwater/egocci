package command.song;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import common.CommonExecute;
import dao.SongDao;
import dto.SongDto;

public class SongList implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        SongDao dao = new SongDao();
        
        ArrayList<SongDto> albumSongs = dao.getSongsByAlbum("album");
        ArrayList<SongDto> epSongs = dao.getSongsByAlbum("ep");
        ArrayList<SongDto> singleSongs = dao.getSongsByAlbum("single");
        
        request.setAttribute("albumSongs", albumSongs);
        request.setAttribute("epSongs", epSongs);
        request.setAttribute("singleSongs", singleSongs);
    }
}