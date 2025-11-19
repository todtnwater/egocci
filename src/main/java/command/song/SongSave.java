package command.song;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import common.CommonExecute;
import common.CommonUtil;
import dao.SongDao;
import dao.AlbumDao;
import dto.SongDto;
import dto.AlbumDto;

public class SongSave implements CommonExecute {
	@Override
	public void execute(HttpServletRequest request) {
		System.out.println("=== SongSave 실행됨 ===");
	    // 권한 체크
	    HttpSession session = request.getSession();
	    String level = (String)session.getAttribute("sessionLevel");
	    
	    if(level == null || !"top".equals(level)) {
	        request.setAttribute("t_msg", "관리자 권한이 필요합니다.");
	        request.setAttribute("t_url", "Song?t_gubun=list");
	        return;
	    }
	    
	    SongDao songDao = new SongDao();
	    AlbumDao albumDao = new AlbumDao();
	    
	    try {
	        // 파일 업로드 경로 설정
	        String uploadPath = request.getServletContext().getRealPath("/images/song");
	        
	        // 디렉토리 없으면 생성
	        File uploadDir = new File(uploadPath);
	        if(!uploadDir.exists()) {
	        	boolean created = uploadDir.mkdirs();
	        	System.out.println("업로드 디렉토리 생성 :" + created + " - " + uploadPath);
	        }
	        System.out.println("업로드 경로 : " + uploadPath);
	        System.out.println("디렉토리 존재 : " + uploadDir.exists());
	        System.out.println("쓰기 권한 : " + uploadDir.canWrite());
	        int maxSize = 5 * 1024 * 1024; // 5MB
	        
	        MultipartRequest multi = null;
	        try {
	        multi = new MultipartRequest(
	            request, uploadPath, maxSize, "UTF-8", new DefaultFileRenamePolicy()
	        );
	        System.out.println("MultipartRequest 생성 완료");
	        }catch (Exception e) {
				System.err.println("MultipartRequest 생성 실패");
				System.err.println("원인" + e.getMessage());
				e.printStackTrace();
				// 구체적인 에러 메시지
                String errorMsg = "파일 업로드 실패: ";
                if (e.getMessage().contains("exceed")) {
                    errorMsg += "파일 크기가 5MB를 초과했습니다.";
                } else if (e.getMessage().contains("directory")) {
                    errorMsg += "업로드 디렉토리 오류입니다.";
                } else {
                    errorMsg += e.getMessage();
                }
                
                request.setAttribute("t_msg", errorMsg);
                request.setAttribute("t_url", "Song?t_gubun=writeForm");
                return;
			}
	        
	        int albumId = 0;
	        String albumChoice = multi.getParameter("album_choice");
	        
	        if ("new".equals(albumChoice)) {
	            String albumType = multi.getParameter("t_album_type");
	            String newAlbumTitle = multi.getParameter("t_new_album_title");
	            String albumNumber = multi.getParameter("t_album_number");
	            String releaseDate = multi.getParameter("t_release_date");
	            String albumDescription = multi.getParameter("t_album_description");
	            
	            AlbumDto albumDto = new AlbumDto();
	            albumDto.setAlbum_title(CommonUtil.setQuote(newAlbumTitle));
	            albumDto.setAlbum_type(albumType);
	            albumDto.setAlbum_number(CommonUtil.setQuote(albumNumber));
	            albumDto.setRelease_date(releaseDate);
	            if (albumDescription != null && !albumDescription.trim().isEmpty()) {
	                albumDto.setAlbum_description(CommonUtil.setQuote(albumDescription));
	            }
	            
	            albumId = albumDao.saveAlbum(albumDto);
	            
	            if (albumId == 0) {
	                request.setAttribute("t_msg", "앨범 생성에 실패했습니다.");
	                request.setAttribute("t_url", "Song?t_gubun=writeForm");
	                return;
	            }
	        } else {
	            String albumIdStr = multi.getParameter("t_album_id");
	            if (albumIdStr != null && !albumIdStr.trim().isEmpty()) {
	                albumId = Integer.parseInt(albumIdStr);
	            }
	        }
	        
	        if (albumId == 0) {
	            request.setAttribute("t_msg", "앨범을 선택하거나 생성해주세요.");
	            request.setAttribute("t_url", "Song?t_gubun=writeForm");
	            return;
	        }
	        
	        // 파일명 가져오기
	        String coverImage = multi.getFilesystemName("t_cover_image");
	        
	        String songTitle = multi.getParameter("t_song_title");
	        String artistName = multi.getParameter("t_artist_name");
	        String genre = multi.getParameter("t_genre");
	        String trackNumber = multi.getParameter("t_track_number");
	        String duration = multi.getParameter("t_duration");
	        String lyrics = multi.getParameter("t_lyrics");
	        String isTitleTrack = multi.getParameter("t_is_title_track");
	        
	        songTitle = CommonUtil.setQuote(songTitle);
	        artistName = CommonUtil.setQuote(artistName);
	        if (lyrics != null && !lyrics.trim().isEmpty()) {
	            lyrics = CommonUtil.setQuote(lyrics);
	        }
	        
	        SongDto songDto = new SongDto();
	        songDto.setAlbum_id(albumId);
	        songDto.setSong_title(songTitle);
	        songDto.setArtist_name(artistName);
	        songDto.setGenre(genre);
	        songDto.setLyrics(lyrics);
	        // ✅ 파일이 실제로 업로드되었을 때만 저장
	        if (coverImage != null && !coverImage.isEmpty()) {
                songDto.setSong_cover_image(coverImage);
                System.out.println(" 커버 이미지 설정: " + coverImage);
            } else {
                songDto.setSong_cover_image(null);
                System.out.println(" 커버 이미지 없음");
            }
	        if (trackNumber != null && !trackNumber.trim().isEmpty()) {
	            try {
	                songDto.setTrack_number(Integer.parseInt(trackNumber));
	            } catch (NumberFormatException e) {
	                songDto.setTrack_number(1);
	            }
	        } else {
	            songDto.setTrack_number(1);
	        }
	        
	        if (duration != null && !duration.trim().isEmpty()) {
	            songDto.setDuration(duration);
	        }
	        
	        songDto.setIs_title_track("true".equals(isTitleTrack));
	        songDto.setDisplay_order(0);
	        
	        int songId = songDao.saveSong(songDto);
	        System.out.println("저장된 곡 ID: " + songId);
	        if (songId > 0) {
	            saveStreamingLinks(multi, songDao, songId);
	            
	            request.setAttribute("t_msg", "곡이 성공적으로 등록되었습니다.");
	            request.setAttribute("t_url", "Song?t_gubun=list");
	        } else {
	            request.setAttribute("t_msg", "곡 등록에 실패했습니다.");
	            request.setAttribute("t_url", "Song?t_gubun=writeForm");
	        }
	        
	    } catch (Exception e) {
	        System.err.println("SongSave 오류: " + e.getMessage());
	        e.printStackTrace();
	        request.setAttribute("t_msg", "시스템 오류가 발생했습니다.");
	        request.setAttribute("t_url", "Song?t_gubun=writeForm");
	    }
	}

	private void saveStreamingLinks(MultipartRequest multi, SongDao dao, int songId) {
	    String spotifyUrl = multi.getParameter("t_spotify_url");
	    if (spotifyUrl != null && !spotifyUrl.trim().isEmpty()) {
	        dao.saveStreamingLink(songId, 1, spotifyUrl.trim());
	    }
	    
	    String melonUrl = multi.getParameter("t_melon_url");
	    if (melonUrl != null && !melonUrl.trim().isEmpty()) {
	        dao.saveStreamingLink(songId, 2, melonUrl.trim());
	    }
	    
	    String appleMusicUrl = multi.getParameter("t_apple_music_url");
	    if (appleMusicUrl != null && !appleMusicUrl.trim().isEmpty()) {
	        dao.saveStreamingLink(songId, 3, appleMusicUrl.trim());
	    }
	    
	    String youtubeUrl = multi.getParameter("t_youtube_url");
	    if (youtubeUrl != null && !youtubeUrl.trim().isEmpty()) {
	        dao.saveStreamingLink(songId, 4, youtubeUrl.trim());
	    }
	    
	    String soundcloudUrl = multi.getParameter("t_soundcloud_url");
	    if (soundcloudUrl != null && !soundcloudUrl.trim().isEmpty()) {
	        dao.saveStreamingLink(songId, 7, soundcloudUrl.trim());
	    }
	}
}