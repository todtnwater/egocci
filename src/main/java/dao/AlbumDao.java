package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import dto.AlbumDto;
import common.DBConnection;

public class AlbumDao {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    // AJAX용 앨범 목록 조회
    public ArrayList<AlbumDto> getAlbumsByType(String albumType) {
        ArrayList<AlbumDto> dtos = new ArrayList<>();
        
        try {
            con = DBConnection.getConnection();
            String sql = "SELECT album_id, album_title, album_number, album_type " +
                        "FROM albums WHERE album_type = ? AND is_active = TRUE " +
                        "ORDER BY display_order, release_date DESC";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, albumType);
            rs = ps.executeQuery();
            
            while(rs.next()) {
                AlbumDto dto = new AlbumDto();
                dto.setAlbum_id(rs.getString("album_id"));
                dto.setAlbum_title(rs.getString("album_title"));
                dto.setAlbum_number(rs.getString("album_number"));
                dto.setAlbum_type(rs.getString("album_type"));
                dtos.add(dto);
            }
            
        } catch(Exception e) {
            System.out.println("getAlbumsByType 오류: " + e.getMessage());
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return dtos;
    }
    
    // 신규 앨범 저장
    public int saveAlbum(AlbumDto dto) {
        int result = 0;
        
        try {
            con = DBConnection.getConnection();
            String sql = "INSERT INTO albums (album_title, album_type, album_number, release_date, album_description) " +
                        "VALUES (?, ?, ?, ?, ?)";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getAlbum_title());
            ps.setString(2, dto.getAlbum_type());
            ps.setString(3, dto.getAlbum_number());
            ps.setString(4, dto.getRelease_date());
            ps.setString(5, dto.getAlbum_description());
            
            result = ps.executeUpdate();
            
        } catch(Exception e) {
            System.out.println("saveAlbum 오류: " + e.getMessage());
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return result;
    }
    
    // 마지막 앨범 ID 가져오기
    public String getLastAlbumId() {
        String albumId = "0";
        
        try {
            con = DBConnection.getConnection();
            String sql = "SELECT LAST_INSERT_ID() as album_id";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                albumId = rs.getString("album_id");
            }
            
        } catch(Exception e) {
            System.out.println("getLastAlbumId 오류: " + e.getMessage());
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        
        return albumId;
    }
}