package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import common.DBConnection;
import dto.SongDto;

public class SongDao {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 1. 앨범별 곡 목록 조회
    public ArrayList<SongDto> getSongsByAlbum(String albumType) {
        ArrayList<SongDto> songs = new ArrayList<>();

        try {
            con = DBConnection.getConnection();
            if (con == null) {
                System.err.println("DB 연결 실패");
                return songs;
            }

            String sql = "SELECT s.*, a.album_title, a.album_type, a.album_number " +
                         "FROM songs s " +
                         "INNER JOIN albums a ON s.album_id = a.album_id " +
                         "WHERE s.is_active = true AND a.album_type = ? " +
                         "ORDER BY " +
                         "  a.release_date DESC, " +
                         "  s.track_number ASC";

            ps = con.prepareStatement(sql);
            ps.setString(1, albumType);
            rs = ps.executeQuery();

            while (rs.next()) {
                SongDto song = new SongDto();
                song.setSong_id(rs.getInt("song_id"));
                song.setSong_title(rs.getString("song_title"));
                song.setArtist_name(rs.getString("artist_name"));
                song.setSong_cover_image(rs.getString("song_cover_image"));
                song.setAlbum_title(rs.getString("album_title"));
                song.setAlbum_type(rs.getString("album_type"));
                song.setAlbum_number(rs.getString("album_number"));
                song.setCreated_at(rs.getString("created_at"));
                songs.add(song);
            }

        } catch (Exception e) {
            System.err.println("getSongsByAlbum 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }

        return songs;
    }

    // 2. 곡 상세 조회
    public SongDto getSongView(int songId) {
        SongDto song = null;

        try {
            con = DBConnection.getConnection();
            if (con == null) {
                return null;
            }

            String sql = "SELECT s.*, a.album_title, a.album_type, a.album_number, a.release_date, a.album_description " +
                         "FROM songs s " +
                         "INNER JOIN albums a ON s.album_id = a.album_id " +
                         "WHERE s.song_id = ? AND s.is_active = true";

            ps = con.prepareStatement(sql);
            ps.setInt(1, songId);
            rs = ps.executeQuery();

            if (rs.next()) {
                song = mapFullSongInfo(rs);
            }

        } catch (Exception e) {
            System.err.println("getSongView 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }

        return song;
    }

    // 3. 곡 저장
    public int saveSong(SongDto song) {
        int songId = 0;
        try {
            con = DBConnection.getConnection();

            String sql = "INSERT INTO songs (" +
                         "album_id, song_title, artist_name, genre, " +
                         "lyrics, behind_note, track_number, duration, " +
                         "is_title_track, song_cover_image, display_order" +
                         ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, song.getAlbum_id());
            ps.setString(2, song.getSong_title());
            ps.setString(3, song.getArtist_name());
            ps.setString(4, song.getGenre());
            ps.setString(5, song.getLyrics());
            ps.setString(6, song.getBehind_note());
            ps.setInt(7, song.getTrack_number());
            ps.setString(8, song.getDuration());
            ps.setBoolean(9, song.getIs_title_track());
            ps.setString(10, song.getSong_cover_image());
            ps.setInt(11, song.getDisplay_order());

            int result = ps.executeUpdate();

            if (result > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    songId = rs.getInt(1);
                }
            }

        } catch (Exception e) {
            System.err.println("saveSong 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }

        return songId;
    }

    // 4. 곡 수정
    public boolean updateSong(SongDto song) {
        try {
            con = DBConnection.getConnection();

            String sql = "UPDATE songs " +
                         "SET album_id = ?, song_title = ?, artist_name = ?, genre = ?, " +
                         "lyrics = ?, behind_note = ?, song_cover_image = ? " +
                         "WHERE song_id = ? AND is_active = true";

            ps = con.prepareStatement(sql);
            ps.setInt(1, song.getAlbum_id());
            ps.setString(2, song.getSong_title());
            ps.setString(3, song.getArtist_name());
            ps.setString(4, song.getGenre());
            ps.setString(5, song.getLyrics());
            ps.setString(6, song.getBehind_note());
            ps.setString(7, song.getSong_cover_image());
            ps.setInt(8, song.getSong_id());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("updateSong 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
    }

    // 5. 곡 삭제 (Soft Delete)
    public boolean deleteSong(int songId) {
        try {
            con = DBConnection.getConnection();
            String sql = "UPDATE songs SET is_active = false WHERE song_id = ?";

            ps = con.prepareStatement(sql);
            ps.setInt(1, songId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("deleteSong 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
    }

    // 6. 스트리밍 링크 저장
    public boolean saveStreamingLink(int songId, int platformId, String streamingUrl) {
        try {
            con = DBConnection.getConnection();
            String sql = "INSERT INTO song_streaming_links (song_id, platform_id, streaming_url) " +
                         "VALUES (?, ?, ?) " +
                         "ON DUPLICATE KEY UPDATE streaming_url = VALUES(streaming_url)";

            ps = con.prepareStatement(sql);
            ps.setInt(1, songId);
            ps.setInt(2, platformId);
            ps.setString(3, streamingUrl);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("saveStreamingLink 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
    }

    // 7. 스트리밍 링크 조회
    public ArrayList<String[]> getStreamingLinks(int songId) {
        ArrayList<String[]> links = new ArrayList<>();

        try {
            con = DBConnection.getConnection();
            String sql = "SELECT streaming_platforms.platform_name, song_streaming_links.streaming_url " +
                         "FROM song_streaming_links " +
                         "INNER JOIN streaming_platforms ON song_streaming_links.platform_id = streaming_platforms.platform_id " +
                         "WHERE song_streaming_links.song_id = ? AND song_streaming_links.is_active = true " +
                         "ORDER BY streaming_platforms.display_order";

            ps = con.prepareStatement(sql);
            ps.setInt(1, songId);
            rs = ps.executeQuery();

            while (rs.next()) {
                String[] link = new String[2];
                link[0] = rs.getString("platform_name");
                link[1] = rs.getString("streaming_url");
                links.add(link);
            }

        } catch (Exception e) {
            System.err.println("getStreamingLinks 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }

        return links;
    }

    // 8. 최신곡 조회
    public SongDto getLatestSong() {
        SongDto song = null;

        try {
            con = DBConnection.getConnection();
            if (con == null) {
                return null;
            }

            String sql = "SELECT s.*, a.album_title, a.album_type, a.album_number, a.release_date " +
                         "FROM songs s " +
                         "INNER JOIN albums a ON s.album_id = a.album_id " +
                         "WHERE s.is_active = true " +
                         "ORDER BY a.release_date DESC, s.track_number ASC " +
                         "LIMIT 1";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                song = mapFullSongInfo(rs);
            }

        } catch (Exception e) {
            System.err.println("getLatestSong 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }

        return song;
    }

    // 9. 인기곡 조회
    public ArrayList<SongDto> getPopularSongs(int limit) {
        ArrayList<SongDto> songs = new ArrayList<>();

        try {
            con = DBConnection.getConnection();
            if (con == null) {
                return songs;
            }

            String sql = "SELECT s.song_id, s.song_title, s.artist_name, s.song_cover_image, " +
                         "s.is_title_track, a.album_title, a.album_type, a.album_number " +
                         "FROM songs s " +
                         "INNER JOIN albums a ON s.album_id = a.album_id " +
                         "WHERE s.is_active = true " +
                         "ORDER BY a.release_date DESC, s.track_number ASC " +
                         "LIMIT ?";

            ps = con.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();

            while (rs.next()) {
                SongDto song = mapBasicSongInfo(rs);
                songs.add(song);
            }

        } catch (Exception e) {
            System.err.println("getPopularSongs 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }

        return songs;
    }

    // 10. 최신 앨범 조회
    public ArrayList<SongDto> getRecentAlbums(int limit) {
        ArrayList<SongDto> albums = new ArrayList<>();

        try {
            con = DBConnection.getConnection();
            if (con == null) {
                return albums;
            }

            String sql = "SELECT DISTINCT a.album_id, a.album_title, a.album_type, a.album_number, " +
                         "a.release_date, a.album_cover_image, " +
                         "(SELECT s.song_cover_image " +
                         " FROM songs s " +
                         " WHERE s.album_id = a.album_id AND s.is_title_track = true " +
                         " LIMIT 1) as title_track_image " +
                         "FROM albums a " +
                         "WHERE a.is_active = true " +
                         "ORDER BY a.release_date DESC " +
                         "LIMIT ?";

            ps = con.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();

            while (rs.next()) {
                SongDto album = new SongDto();
                album.setAlbum_id(rs.getInt("album_id"));
                album.setAlbum_title(rs.getString("album_title"));
                album.setAlbum_type(rs.getString("album_type"));
                album.setAlbum_number(rs.getString("album_number"));
                album.setRelease_date(rs.getString("release_date"));

                String albumCover = rs.getString("album_cover_image");
                String titleTrackImage = rs.getString("title_track_image");
                album.setSong_cover_image(albumCover != null ? albumCover : titleTrackImage);

                albums.add(album);
            }

        } catch (Exception e) {
            System.err.println("getRecentAlbums 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }

        return albums;
    }

    private SongDto mapBasicSongInfo(ResultSet rs) throws Exception {
        SongDto song = new SongDto();
        song.setSong_id(rs.getInt("song_id"));
        song.setSong_title(rs.getString("song_title"));
        song.setArtist_name(rs.getString("artist_name"));
        song.setSong_cover_image(rs.getString("song_cover_image"));
        song.setAlbum_title(rs.getString("album_title"));
        song.setAlbum_type(rs.getString("album_type"));
        song.setAlbum_number(rs.getString("album_number"));

        try {
            song.setIs_title_track(rs.getBoolean("is_title_track"));
        } catch (Exception e) {
        }

        try {
        } catch (Exception e) {
        }

        try {
            song.setCreated_at(rs.getString("created_at"));
        } catch (Exception e) {
        }

        return song;
    }

    private SongDto mapFullSongInfo(ResultSet rs) throws Exception {
        SongDto song = mapBasicSongInfo(rs);

        try {
            song.setAlbum_id(rs.getInt("album_id"));
            song.setGenre(rs.getString("genre"));
            song.setLyrics(rs.getString("lyrics"));
            song.setBehind_note(rs.getString("behind_note"));
            song.setDuration(rs.getString("duration"));
            song.setTrack_number(rs.getInt("track_number"));
            song.setRelease_date(rs.getString("release_date"));
            song.setAlbum_description(rs.getString("album_description"));
            song.setCreated_at(rs.getString("created_at"));
            song.setUpdated_at(rs.getString("updated_at"));
        } catch (Exception e) {
        }

        return song;
    }
}