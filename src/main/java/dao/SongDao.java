package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import dto.SongDto;
import common.DBConnection;

public class SongDao {
	Connection 			con = null;
	PreparedStatement 	ps  = null;
	ResultSet 			rs  = null;	
    
    // 1. 앨범별 곡 목록 조회 (HTML 구조에 맞춰)
    public ArrayList<SongDto> getSongsByAlbum(String album_type) {
        ArrayList<SongDto> dtos = new ArrayList<>();
        
        try {
            con = DBConnection.getConnection();
            String sql = "SELECT s.*, a.album_title, a.album_type, a.album_number " +
                        "FROM songs s JOIN albums a ON s.album_id = a.album_id " +
                        "WHERE s.is_active = TRUE AND a.album_type = ? " +
                        "ORDER BY a.display_order, s.track_number";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, album_type);
            rs = ps.executeQuery();
            
            while(rs.next()) {
                SongDto dto = new SongDto();
                dto.setSong_id(rs.getString("song_id"));
                dto.setSong_title(rs.getString("song_title"));
                dto.setArtist_name(rs.getString("artist_name"));
                dto.setSong_cover_image(rs.getString("song_cover_image"));
                dto.setAlbum_title(rs.getString("album_title"));
                dto.setAlbum_type(rs.getString("album_type"));
                dto.setAlbum_number(rs.getString("album_number"));
                dtos.add(dto);
            }
            
        } catch(Exception e) {
            System.out.println("getSongsByAlbum 오류: " + e.getMessage());
        }finally {
			DBConnection.closeDB(con, ps, rs);
		}		
        
        return dtos;
    }
    
    // 2. 곡 상세 조회
    public SongDto getSongView(String song_id) {
        SongDto dto = null;
        
        try {
        	con = DBConnection.getConnection();
            String sql = "SELECT s.*, a.album_title, a.album_type, a.album_number, a.release_date " +
                        "FROM songs s JOIN albums a ON s.album_id = a.album_id " +
                        "WHERE s.song_id = ?";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, song_id);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                dto = new SongDto();
                dto.setSong_id(rs.getString("song_id"));
                dto.setSong_title(rs.getString("song_title"));
                dto.setArtist_name(rs.getString("artist_name"));
                dto.setGenre(rs.getString("genre"));
                dto.setLyrics(rs.getString("lyrics"));
                dto.setSong_cover_image(rs.getString("song_cover_image"));
                dto.setAlbum_title(rs.getString("album_title"));
                dto.setAlbum_type(rs.getString("album_type"));
                dto.setAlbum_number(rs.getString("album_number"));
                dto.setRelease_date(rs.getString("release_date"));
                dto.setIs_title_track(rs.getBoolean("is_title_track"));
                dto.setTrack_number(rs.getInt("track_number"));
                dto.setDuration(rs.getString("duration"));
            }
            
        } catch(Exception e) {
            System.out.println("getSongView 오류: " + e.getMessage());
        } finally {
			DBConnection.closeDB(con, ps, rs);
		}	
        
        return dto;
    }
    
    // 3. 다음 ID 생성
    public String getNo() {
        String no = "1";
        
        try {
            con = DBConnection.getConnection();
            String sql = "SELECT IFNULL(MAX(song_id), 0) + 1 as next_id FROM songs";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                no = rs.getString("next_id");
            }
        } catch(Exception e) {
            System.out.println("getNo 오류: " + e.getMessage());
        } finally {
			DBConnection.closeDB(con, ps, rs);
		}	
        
        return no;
    }
    
    // 4. 곡 저장
    public int songSave(SongDto dto) {
        int result = 0;
        
        try {
            con = DBConnection.getConnection();
            String sql = "INSERT INTO songs (song_id, album_id, song_title, artist_name, genre, lyrics, " +
                        "track_number, duration, is_title_track, song_cover_image) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getSong_id());
            ps.setString(2, dto.getAlbum_id());
            ps.setString(3, dto.getSong_title());
            ps.setString(4, dto.getArtist_name());
            ps.setString(5, dto.getGenre());
            ps.setString(6, dto.getLyrics());
            ps.setInt(7, dto.getTrack_number());
            ps.setString(8, dto.getDuration());
            ps.setBoolean(9, dto.getIs_title_track());
            ps.setString(10, dto.getSong_cover_image());
            
            result = ps.executeUpdate();
            
        } catch(Exception e) {
            System.out.println("songSave 오류: " + e.getMessage());
        } finally {
        	DBConnection.closeDB(con, ps, rs);
        }
        
        return result;
    }
    
    // 5. 곡 수정
    public int songUpdate(SongDto dto) {
        int result = 0;
        
        try {
        	con = DBConnection.getConnection();
            String sql = "UPDATE songs SET song_title = ?, artist_name = ?, genre = ?, lyrics = ? " +
                        "WHERE song_id = ?";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getSong_title());
            ps.setString(2, dto.getArtist_name());
            ps.setString(3, dto.getGenre());
            ps.setString(4, dto.getLyrics());
            ps.setString(5, dto.getSong_id());
            
            result = ps.executeUpdate();
            
        } catch(Exception e) {
            System.out.println("songUpdate 오류: " + e.getMessage());
        } finally {
        	DBConnection.closeDB(con, ps, rs);
        }
        
        return result;
    }
    
    // 6. 곡 삭제
    public int songDelete(String song_id) {
        int result = 0;
        
        try {
        	con = DBConnection.getConnection();
            String sql = "UPDATE songs SET is_active = FALSE WHERE song_id = ?";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, song_id);
            
            result = ps.executeUpdate();
            
        } catch(Exception e) {
            System.out.println("songDelete 오류: " + e.getMessage());
        } finally {
        	DBConnection.closeDB(con, ps, rs);
        }
        
        return result;
    }
    
    // 7. 스트리밍 링크 저장
    public int saveStreamingLink(String songId, String platformId, String streamingUrl) {
        int result = 0;
        
        try {
			con = DBConnection.getConnection();
            String sql = "INSERT INTO song_streaming_links (song_id, platform_id, streaming_url) " +
                        "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE streaming_url = ?";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, songId);
            ps.setString(2, platformId);
            ps.setString(3, streamingUrl);
            ps.setString(4, streamingUrl);
            
            result = ps.executeUpdate();
            
        } catch(Exception e) {
            System.out.println("saveStreamingLink 오류: " + e.getMessage());
        } finally {
			DBConnection.closeDB(con, ps, rs);
        }
        
        return result;
    }
    
    // 8. 곡별 스트리밍 링크 조회
    public ArrayList<String[]> getStreamingLinks(String songId) {
        ArrayList<String[]> links = new ArrayList<>();
        try {
			con = DBConnection.getConnection();
			String sql = "SELECT sp.platform_name, ssl.streaming_url " +
		             "FROM song_streaming_links AS ssl " +
		             "JOIN streaming_platforms AS sp ON ssl.platform_id = sp.platform_id " +
		             "WHERE ssl.song_id = ? AND ssl.is_active = 1";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, songId);
            rs = ps.executeQuery();
            
            while(rs.next()) {
                String[] link = new String[2];
                link[0] = rs.getString("platform_name");
                link[1] = rs.getString("streaming_url");
                links.add(link);
            }
            
        } catch(Exception e) {
            System.out.println("getStreamingLinks 오류: " + e.getMessage());
        } finally {
			DBConnection.closeDB(con, ps, rs);
        }
        
        return links;
    }
    
 // 9. 최신 곡 가져오기 (메인 페이지 상단용)
    public SongDto getLatestSong() {
        SongDto dto = null;
        
        try {
            con = DBConnection.getConnection();
            if(con == null) {
                System.out.println("데이터베이스 연결 실패 - getLatestSong");
                return dto;
            }
            
            String sql = "SELECT s.*, a.album_title, a.album_type, a.album_number, a.release_date " +
                        "FROM songs s JOIN albums a ON s.album_id = a.album_id " +
                        "WHERE s.is_active = TRUE " +
                        "ORDER BY s.is_title_track DESC, a.release_date DESC, s.created_at DESC " +
                        "LIMIT 1";
            
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                dto = new SongDto();
                dto.setSong_id(rs.getString("song_id"));
                dto.setSong_title(rs.getString("song_title"));
                dto.setArtist_name(rs.getString("artist_name"));
                dto.setGenre(rs.getString("genre"));
                dto.setLyrics(rs.getString("lyrics"));
                dto.setSong_cover_image(rs.getString("song_cover_image"));
                dto.setAlbum_title(rs.getString("album_title"));
                dto.setAlbum_type(rs.getString("album_type"));
                dto.setAlbum_number(rs.getString("album_number"));
                dto.setRelease_date(rs.getString("release_date"));
                dto.setIs_title_track(rs.getBoolean("is_title_track"));
                dto.setDuration(rs.getString("duration"));
            }
            
        } catch(Exception e) {
            System.out.println("getLatestSong 오류: " + e.getMessage());
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return dto;
    }

    // 10. 인기 곡들 가져오기 (슬라이더용)
    public ArrayList<SongDto> getPopularSongs(int limit) {
        ArrayList<SongDto> dtos = new ArrayList<>();
        
        try {
            con = DBConnection.getConnection();
            if(con == null) {
                System.out.println("데이터베이스 연결 실패 - getPopularSongs");
                return dtos;
            }
            
            String sql = "SELECT s.*, a.album_title, a.album_type, a.album_number " +
                        "FROM songs s JOIN albums a ON s.album_id = a.album_id " +
                        "WHERE s.is_active = TRUE " +
                        "ORDER BY s.is_title_track DESC, a.release_date DESC, s.track_number ASC " +
                        "LIMIT ?";
            
            ps = con.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            
            while(rs.next()) {
                SongDto dto = new SongDto();
                dto.setSong_id(rs.getString("song_id"));
                dto.setSong_title(rs.getString("song_title"));
                dto.setArtist_name(rs.getString("artist_name"));
                dto.setSong_cover_image(rs.getString("song_cover_image"));
                dto.setAlbum_title(rs.getString("album_title"));
                dto.setAlbum_type(rs.getString("album_type"));
                dto.setAlbum_number(rs.getString("album_number"));
                dto.setIs_title_track(rs.getBoolean("is_title_track"));
                dtos.add(dto);
            }
            
        } catch(Exception e) {
            System.out.println("getPopularSongs 오류: " + e.getMessage());
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return dtos;
    }

    // 11. 최신 앨범들 가져오기
    public ArrayList<SongDto> getRecentAlbums(int limit) {
        ArrayList<SongDto> dtos = new ArrayList<>();
        
        try {
            con = DBConnection.getConnection();
            if(con == null) {
                System.out.println("데이터베이스 연결 실패 - getRecentAlbums");
                return dtos;
            }
            
            String sql = "SELECT DISTINCT a.album_id, a.album_title, a.album_type, a.album_number, " +
                        "a.release_date, a.album_cover_image, " +
                        "(SELECT s.song_cover_image FROM songs s WHERE s.album_id = a.album_id AND s.is_title_track = TRUE LIMIT 1) as title_track_image " +
                        "FROM albums a " +
                        "WHERE a.is_active = TRUE " +
                        "ORDER BY a.release_date DESC " +
                        "LIMIT ?";
            
            ps = con.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            
            while(rs.next()) {
                SongDto dto = new SongDto();
                dto.setAlbum_id(rs.getString("album_id"));
                dto.setAlbum_title(rs.getString("album_title"));
                dto.setAlbum_type(rs.getString("album_type"));
                dto.setAlbum_number(rs.getString("album_number"));
                dto.setRelease_date(rs.getString("release_date"));
                
                // 앨범 커버 이미지가 없으면 타이틀곡 이미지 사용
                String albumCover = rs.getString("album_cover_image");
                String titleTrackImage = rs.getString("title_track_image");
                dto.setSong_cover_image(albumCover != null ? albumCover : titleTrackImage);
                
                dtos.add(dto);
            }
            
        } catch(Exception e) {
            System.out.println("getRecentAlbums 오류: " + e.getMessage());
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return dtos;
    }
}