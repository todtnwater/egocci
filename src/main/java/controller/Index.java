package controller;

import java.io.IOException;
import java.util.ArrayList;

import dao.SocialDao;
import dao.SongDao;
import dto.SocialDto;
import dto.SongDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Index")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Index() {
        super();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		SongDao songDao = new SongDao();
		SocialDao socialDao = new SocialDao();

		try {
			SongDto latestSong = songDao.getLatestSong();

			ArrayList<SongDto> popularSongs = songDao.getPopularSongs(10);

			ArrayList<SongDto> recentAlbums = songDao.getRecentAlbums(3);

			ArrayList<SocialDto> socialLinks = socialDao.getActiveSocialLinks();

			request.setAttribute("latestSong", latestSong);
			request.setAttribute("popularSongs", popularSongs);
			request.setAttribute("recentAlbums", recentAlbums);
			request.setAttribute("socialLinks", socialLinks);

		} catch (Exception e) {
			System.out.println("Index 페이지 데이터 로드 오류: " + e.getMessage());
			e.printStackTrace();
		}

		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
