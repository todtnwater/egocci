package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.SongDao;
import dao.SocialDao;
import dto.SongDto;
import dto.SocialDto;

@WebServlet("/Index")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Index() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 메인 페이지용 데이터 가져오기
		SongDao songDao = new SongDao();
		SocialDao socialDao = new SocialDao();
		
		try {
			// 1. 최신 곡 (타이틀곡 우선, 최근 발매순)
			SongDto latestSong = songDao.getLatestSong();
			
			// 2. 인기 곡들 (슬라이더용 - 최대 10곡)
			ArrayList<SongDto> popularSongs = songDao.getPopularSongs(10);
			
			// 3. 최신 앨범 정보
			ArrayList<SongDto> recentAlbums = songDao.getRecentAlbums(3);
			
			// 4. 소셜 링크 목록
			ArrayList<SocialDto> socialLinks = socialDao.getActiveSocialLinks();
			
			// request에 데이터 설정
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}