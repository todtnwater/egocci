package command.song;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import common.CommonExecute;
import common.CommonUtil;
import dao.SongDao;
import dto.SongDto;

public class SongUpdate implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        // 권한 체크
        HttpSession session = request.getSession();
        String level = (String)session.getAttribute("sessionLevel");
        
        if(level == null || !"top".equals(level)) {
            request.setAttribute("t_msg", "관리자 권한이 필요합니다.");
            request.setAttribute("t_url", "Song?t_gubun=list");
            return;
        }
        
        SongDao dao = new SongDao();
        
        try {
            // 파일 업로드 경로 설정
            String uploadPath = request.getServletContext().getRealPath("/images/song");
            
            //  가장 중요! 디렉토리가 없으면 생성
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                System.out.println("업로드 디렉토리 생성: " + created + " - " + uploadPath);
            }
            System.out.println("업로드 경로: " + uploadPath);
            System.out.println("디렉토리 존재: " + uploadDir.exists());
            System.out.println("쓰기 권한: " + uploadDir.canWrite());
            int maxSize = 5 * 1024 * 1024; // 5MB
            

            // ✅ MultipartRequest 생성 시 예외 처리 강화
            MultipartRequest multi = null;
            try {
                multi = new MultipartRequest(
                    request, uploadPath, maxSize, "UTF-8", new DefaultFileRenamePolicy()
                );
                System.out.println("MultipartRequest 생성 완료");
            } catch (Exception e) {
                System.err.println("❌ MultipartRequest 생성 실패!");
                System.err.println("원인: " + e.getMessage());
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
                request.setAttribute("t_url", "Song?t_gubun=list");
                return;
            }
            
            String noStr = multi.getParameter("t_no");
            int songId = Integer.parseInt(noStr);
            
            
            // 기존 곡 정보 조회
            SongDto existingSong = dao.getSongView(songId);
            
            String title = multi.getParameter("t_song_title");
            String artist = multi.getParameter("t_artist_name");
            String genre = multi.getParameter("t_genre");
            String lyrics = multi.getParameter("t_lyrics");
            
            title = CommonUtil.setQuote(title);
            artist = CommonUtil.setQuote(artist);
            if (lyrics != null && !lyrics.trim().isEmpty()) {
                lyrics = CommonUtil.setQuote(lyrics);
            }
            
            // 새 이미지가 업로드되었는지 확인
            String coverImage = multi.getFilesystemName("t_cover_image");
            System.out.println("업로드된 새 파일: " + coverImage);
            
            // 새 이미지가 없으면 기존 이미지 유지
            if(coverImage == null && existingSong != null) {
                coverImage = existingSong.getSong_cover_image();
                System.out.println("기존 이미지 유지: " + coverImage);
            } else if (coverImage != null) {
                System.out.println("✅ 새 이미지로 교체: " + coverImage);
            }
            
            SongDto dto = new SongDto();
            dto.setSong_id(songId);
            dto.setSong_title(title);
            dto.setArtist_name(artist);
            dto.setGenre(genre);
            dto.setLyrics(lyrics);
            dto.setSong_cover_image(coverImage);
            
            boolean result = dao.updateSong(dto);
            
            if (result) {
                updateStreamingLinks(multi, dao, songId);
            }
            
            String msg = result ? "곡이 수정되었습니다." : "수정 실패!";
            request.setAttribute("t_msg", msg);
            request.setAttribute("t_url", "Song?t_gubun=list");
            
        } catch (Exception e) {
            System.err.println("SongUpdate 오류: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("t_msg", "수정 중 오류가 발생했습니다.");
            request.setAttribute("t_url", "Song?t_gubun=list");
        }
    }
    
    private void updateStreamingLinks(MultipartRequest multi, SongDao dao, int songId) {
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