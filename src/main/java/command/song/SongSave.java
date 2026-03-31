package command.song;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import common.CommonExecute;
import common.CommonUtil;
import dao.AlbumDao;
import dao.SongDao;
import dto.AlbumDto;
import dto.SongDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

public class SongSave implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        System.out.println("=== SongSave 실행됨 ===");
        HttpSession session = request.getSession();
        String level = (String) session.getAttribute("sessionLevel");

        if (level == null || !"top".equals(level)) {
            request.setAttribute("t_msg", "관리자 권한이 필요합니다.");
            request.setAttribute("t_url", "Song?t_gubun=list");
            return;
        }

        SongDao songDao = new SongDao();
        AlbumDao albumDao = new AlbumDao();

        try {
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
                        System.out.println("[SongSave] SFTP 업로드 성공: " + savedName);
                    } else {
                        System.out.println("[SongSave] SFTP 업로드 실패");
                    }
                }
            }

            int albumId = 0;
            String albumChoice = request.getParameter("album_choice");

            if ("new".equals(albumChoice)) {
                String albumType        = request.getParameter("t_album_type");
                String newAlbumTitle    = request.getParameter("t_new_album_title");
                String albumNumber      = request.getParameter("t_album_number");
                String releaseDate      = request.getParameter("t_release_date");
                String albumDescription = request.getParameter("t_album_description");

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
                String albumIdStr = request.getParameter("t_album_id");
                if (albumIdStr != null && !albumIdStr.trim().isEmpty()) {
                    albumId = Integer.parseInt(albumIdStr);
                }
            }

            if (albumId == 0) {
                request.setAttribute("t_msg", "앨범을 선택하거나 생성해주세요.");
                request.setAttribute("t_url", "Song?t_gubun=writeForm");
                return;
            }

            String songTitle    = request.getParameter("t_song_title");
            String artistName   = request.getParameter("t_artist_name");
            String genre        = request.getParameter("t_genre");
            String trackNumber  = request.getParameter("t_track_number");
            String duration     = request.getParameter("t_duration");
            String lyrics       = request.getParameter("t_lyrics");
            String behindNote   = request.getParameter("t_behind_note");
            String isTitleTrack = request.getParameter("t_is_title_track");

            songTitle  = CommonUtil.setQuote(songTitle);
            artistName = CommonUtil.setQuote(artistName);

            if (lyrics != null && !lyrics.trim().isEmpty()) {
                lyrics = lyrics.replaceAll("(?i)<br\\s*/?>", "\n");
                lyrics = CommonUtil.setQuote(lyrics);
            }
            if (behindNote != null && !behindNote.trim().isEmpty()) {
                behindNote = behindNote.replaceAll("(?i)<br\\s*/?>", "\n");
                behindNote = CommonUtil.setQuote(behindNote);
            }

            SongDto songDto = new SongDto();
            songDto.setAlbum_id(albumId);
            songDto.setSong_title(songTitle);
            songDto.setArtist_name(artistName);
            songDto.setGenre(genre);
            songDto.setLyrics(lyrics);
            songDto.setBehind_note(behindNote);
            songDto.setSong_cover_image(coverImage);

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
                String trimmedDuration = duration.trim();
                if (trimmedDuration.matches("\\d{1,3}:\\d{2}(:\\d{2})?")) {
                    songDto.setDuration(trimmedDuration);
                } else {
                    System.out.println("[SongSave] duration 형식 오류, 무시됨: " + trimmedDuration);
                }
            }

            songDto.setIs_title_track("true".equals(isTitleTrack));
            songDto.setDisplay_order(0);

            int songId = songDao.saveSong(songDto);
            System.out.println("[SongSave] 저장된 곡 ID: " + songId);

            if (songId > 0) {
                saveStreamingLinks(request, songDao, songId);
                request.setAttribute("t_msg", "곡이 성공적으로 등록되었습니다.");
                request.setAttribute("t_url", "Song?t_gubun=list");
            } else {
                request.setAttribute("t_msg", "곡 등록에 실패했습니다.");
                request.setAttribute("t_url", "Song?t_gubun=writeForm");
            }

        } catch (Exception e) {
            System.err.println("[SongSave] 오류: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("t_msg", "시스템 오류가 발생했습니다.");
            request.setAttribute("t_url", "Song?t_gubun=writeForm");
        }
    }

    private void saveStreamingLinks(HttpServletRequest request, SongDao dao, int songId) {
        String spotifyUrl    = request.getParameter("t_spotify_url");
        String melonUrl      = request.getParameter("t_melon_url");
        String appleMusicUrl = request.getParameter("t_apple_music_url");
        String youtubeUrl    = request.getParameter("t_youtube_url");
        String soundcloudUrl = request.getParameter("t_soundcloud_url");

        if (spotifyUrl != null && !spotifyUrl.trim().isEmpty()) {
            dao.saveStreamingLink(songId, 1, spotifyUrl.trim());
        }
        if (melonUrl != null && !melonUrl.trim().isEmpty()) {
            dao.saveStreamingLink(songId, 2, melonUrl.trim());
        }
        if (appleMusicUrl != null && !appleMusicUrl.trim().isEmpty()) {
            dao.saveStreamingLink(songId, 3, appleMusicUrl.trim());
        }
        if (youtubeUrl != null && !youtubeUrl.trim().isEmpty()) {
            dao.saveStreamingLink(songId, 4, youtubeUrl.trim());
        }
        if (soundcloudUrl != null && !soundcloudUrl.trim().isEmpty()) {
            dao.saveStreamingLink(songId, 7, soundcloudUrl.trim());
        }
    }
}