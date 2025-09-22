package command.song;

import javax.servlet.http.HttpServletRequest;

import common.CommonExecute;
import common.CommonUtil;
import dao.SongDao;
import dao.AlbumDao;
import dto.SongDto;
import dto.AlbumDto;

public class SongSave implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        SongDao songDao = new SongDao();
        AlbumDao albumDao = new AlbumDao();
        
        String albumChoice = request.getParameter("album_choice");
        String albumId = "";
        
        try {
            // 1. 앨범 처리 (신규 or 기존)
            if ("new".equals(albumChoice)) {
                // 신규 앨범 생성
                String albumTitle = request.getParameter("t_new_album_title");
                String albumType = request.getParameter("t_album_type");
                String albumNumber = request.getParameter("t_album_number");
                String releaseDate = request.getParameter("t_release_date");
                String albumDescription = request.getParameter("t_album_description");
                
                // 앨범 커버 이미지 처리 (일단 빈 값으로)
                String albumCoverImage = "";
                
                albumTitle = CommonUtil.setQuote(albumTitle);
                if(albumDescription != null) {
                    albumDescription = CommonUtil.setQuote(albumDescription);
                }
                
                AlbumDto albumDto = new AlbumDto();
                albumDto.setAlbum_title(albumTitle);
                albumDto.setAlbum_type(albumType);
                albumDto.setAlbum_number(albumNumber);
                albumDto.setRelease_date(releaseDate);
                albumDto.setAlbum_cover_image(albumCoverImage);
                albumDto.setAlbum_description(albumDescription);
                
                int albumResult = albumDao.saveAlbum(albumDto);
                
                if (albumResult == 1) {
                    albumId = albumDao.getLastAlbumId();
                } else {
                    request.setAttribute("t_msg", "앨범 생성 실패!");
                    request.setAttribute("t_url", "SongWrite");
                    return;
                }
                
            } else if ("existing".equals(albumChoice)) {
                // 기존 앨범 선택
                albumId = request.getParameter("t_album_id");
            }
            
            // 2. 곡 정보 처리
            String songId = songDao.getNo();
            String songTitle = request.getParameter("t_song_title");
            String artistName = request.getParameter("t_artist_name");
            String genre = request.getParameter("t_genre");
            String trackNumber = request.getParameter("t_track_number");
            String duration = request.getParameter("t_duration");
            String lyrics = request.getParameter("t_lyrics");
            String isTitleTrack = request.getParameter("t_is_title_track");
            
            // 곡 커버 이미지 처리 (일단 빈 값으로)
            String songCoverImage = "";
            
            songTitle = CommonUtil.setQuote(songTitle);
            if(lyrics != null) {
                lyrics = CommonUtil.setQuote(lyrics);
            }
            
            SongDto songDto = new SongDto();
            songDto.setSong_id(songId);
            songDto.setAlbum_id(albumId);
            songDto.setSong_title(songTitle);
            songDto.setArtist_name(artistName);
            songDto.setGenre(genre);
            songDto.setLyrics(lyrics);
            songDto.setSong_cover_image(songCoverImage);
            
            // 숫자 필드 처리
            if (trackNumber != null && !trackNumber.equals("")) {
                songDto.setTrack_number(Integer.parseInt(trackNumber));
            }
            
            if (duration != null && !duration.equals("")) {
                songDto.setDuration(duration);
            }
            
            songDto.setIs_title_track("true".equals(isTitleTrack));
            
            int songResult = songDao.songSave(songDto);
            
            // 3. 스트리밍 링크 처리 (선택사항)
            if (songResult == 1) {
                saveStreamingLinks(request, songId);
                
                String msg = "곡이 성공적으로 등록되었습니다.";
                request.setAttribute("t_msg", msg);
                request.setAttribute("t_url", "SongList");
            } else {
                request.setAttribute("t_msg", "곡 등록 실패!");
                request.setAttribute("t_url", "SongWrite");
            }
            
        } catch (Exception e) {
            System.out.println("SongSave 오류: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("t_msg", "시스템 오류가 발생했습니다: " + e.getMessage());
            request.setAttribute("t_url", "SongWrite");
        }
    }
    
    // 스트리밍 링크 저장 메서드 (확장 버전)
    private void saveStreamingLinks(HttpServletRequest request, String songId) {
        String spotifyUrl = request.getParameter("t_spotify_url");
        String melonUrl = request.getParameter("t_melon_url");
        String youtubeUrl = request.getParameter("t_youtube_url");
        String appleMusicUrl = request.getParameter("t_apple_music_url");
        String bugsUrl = request.getParameter("t_bugs_url");
        String genieUrl = request.getParameter("t_genie_url");
        String soundcloudUrl = request.getParameter("t_soundcloud_url");
        String tiktokUrl = request.getParameter("t_tiktok_url");
        
        SongDao songDao = new SongDao();
        
        try {
            // 각 플랫폼별 링크 저장
            if (spotifyUrl != null && !spotifyUrl.trim().equals("")) {
                songDao.saveStreamingLink(songId, "1", spotifyUrl); // Spotify
            }
            
            if (melonUrl != null && !melonUrl.trim().equals("")) {
                songDao.saveStreamingLink(songId, "2", melonUrl); // Melon
            }
            
            if (appleMusicUrl != null && !appleMusicUrl.trim().equals("")) {
                songDao.saveStreamingLink(songId, "3", appleMusicUrl); // Apple Music
            }
            
            if (youtubeUrl != null && !youtubeUrl.trim().equals("")) {
                songDao.saveStreamingLink(songId, "4", youtubeUrl); // YouTube
            }
            
            if (bugsUrl != null && !bugsUrl.trim().equals("")) {
                songDao.saveStreamingLink(songId, "5", bugsUrl); // Bugs
            }
            
            if (genieUrl != null && !genieUrl.trim().equals("")) {
                songDao.saveStreamingLink(songId, "6", genieUrl); // Genie
            }
            
            if (soundcloudUrl != null && !soundcloudUrl.trim().equals("")) {
                songDao.saveStreamingLink(songId, "7", soundcloudUrl); // SoundCloud
            }
            
            if (tiktokUrl != null && !tiktokUrl.trim().equals("")) {
                songDao.saveStreamingLink(songId, "8", tiktokUrl); // TikTok
            }
            
        } catch (Exception e) {
            System.out.println("스트리밍 링크 저장 오류: " + e.getMessage());
        }
    }
}