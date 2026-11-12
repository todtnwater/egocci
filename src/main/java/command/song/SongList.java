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
        String field    = request.getParameter("field");
        String keyword  = request.getParameter("keyword");

        if (songtype == null) {
			songtype = "all";
		}
        if (field    == null) {
			field    = "all";
		}
        if (keyword  == null) {
			keyword  = "";
		}

        request.setAttribute("songtype", songtype);
        request.setAttribute("field",    field);
        request.setAttribute("keyword",  keyword);

        // 키워드가 있으면 검색 모드
        if (!keyword.trim().isEmpty()) {
            ArrayList<SongDto> searchResults = dao.searchSongs(songtype, field, keyword);
            request.setAttribute("searchResults", searchResults);
            request.setAttribute("isSearch", true);
            return;
        }

        // 키워드 없으면 기존 전체 목록 모드
        request.setAttribute("isSearch", false);

        ArrayList<SongDto> albumSongs  = dao.getSongsByAlbum("album");
        ArrayList<SongDto> epSongs     = dao.getSongsByAlbum("ep");
        ArrayList<SongDto> singleSongs = dao.getSongsByAlbum("single");

        request.setAttribute("albumSongs",  albumSongs);
        request.setAttribute("epSongs",     epSongs);
        request.setAttribute("singleSongs", singleSongs);
    }
}