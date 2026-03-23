package command.song;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import common.CommonExecute;
import common.CommonUtil;
import dao.SongDao;
import dto.SongDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

public class SongUpdate implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String level = (String) session.getAttribute("sessionLevel");

        if (level == null || !"top".equals(level)) {
            request.setAttribute("t_msg", "관리자 권한이 필요합니다.");
            request.setAttribute("t_url", "Song?t_gubun=list");
            return;
        }

        SongDao dao = new SongDao();

        try {
            int songId = Integer.parseInt(request.getParameter("t_no"));
            SongDto existingSong = dao.getSongView(songId);

            // 새 이미지 → 임시 저장 후 SFTP 업로드
            String coverImage = null;
            Part filePart = request.getPart("t_cover_image");
            if (filePart != null && filePart.getSize() > 0) {
                String originalName = filePart.getSubmittedFileName();
                if (originalName != null && !originalName.trim().isEmpty()) {
                    String savedName = System.currentTimeMillis() + "_" + originalName;
                    File tempFile = File.createTempFile("song_", "_" + originalName);
                    try (InputStream is = filePart.getInputStream()) {
                        Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    boolean uploaded = CommonUtil.uploadSongToSFTP(tempFile, savedName);
                    tempFile.delete();
                    if (uploaded) {
                        coverImage = savedName;
                        System.out.println("[SongUpdate] 새 이미지 SFTP 업로드 성공: " + savedName);
                    }
                }
            }

            // 새 이미지 없으면 기존 유지
            if (coverImage == null && existingSong != null) {
                coverImage = existingSong.getSong_cover_image();
                System.out.println("[SongUpdate] 기존 이미지 유지: " + coverImage);
            }

            String title  = CommonUtil.setQuote(request.getParameter("t_song_title"));
            String artist = CommonUtil.setQuote(request.getParameter("t_artist_name"));
            String genre  = request.getParameter("t_genre");
            String lyrics = request.getParameter("t_lyrics");
            if (lyrics != null && !lyrics.trim().isEmpty()) {
                lyrics = CommonUtil.setQuote(lyrics);
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
                updateStreamingLinks(request, dao, songId);
            }

            request.setAttribute("t_msg", result ? "곡이 수정되었습니다." : "수정 실패!");
            request.setAttribute("t_url", "Song?t_gubun=list");

        } catch (Exception e) {
            System.err.println("[SongUpdate] 오류: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("t_msg", "수정 중 오류가 발생했습니다.");
            request.setAttribute("t_url", "Song?t_gubun=list");
        }
    }

    private void updateStreamingLinks(HttpServletRequest request, SongDao dao, int songId) {
        String spotifyUrl    = request.getParameter("t_spotify_url");
        String melonUrl      = request.getParameter("t_melon_url");
        String appleMusicUrl = request.getParameter("t_apple_music_url");
        String youtubeUrl    = request.getParameter("t_youtube_url");
        String soundcloudUrl = request.getParameter("t_soundcloud_url");

        if (spotifyUrl    != null && !spotifyUrl.trim().isEmpty()) {
			dao.saveStreamingLink(songId, 1, spotifyUrl.trim());
		}
        if (melonUrl      != null && !melonUrl.trim().isEmpty()) {
			dao.saveStreamingLink(songId, 2, melonUrl.trim());
		}
        if (appleMusicUrl != null && !appleMusicUrl.trim().isEmpty()) {
			dao.saveStreamingLink(songId, 3, appleMusicUrl.trim());
		}
        if (youtubeUrl    != null && !youtubeUrl.trim().isEmpty()) {
			dao.saveStreamingLink(songId, 4, youtubeUrl.trim());
		}
        if (soundcloudUrl != null && !soundcloudUrl.trim().isEmpty()) {
			dao.saveStreamingLink(songId, 7, soundcloudUrl.trim());
		}
    }
}
