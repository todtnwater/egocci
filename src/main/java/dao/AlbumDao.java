package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import common.DBConnection;
import dto.AlbumDto;

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
                dto.setAlbum_id(rs.getInt("album_id"));
                dto.setAlbum_title(rs.getString("album_title"));
                dto.setAlbum_number(rs.getString("album_number"));
                dto.setAlbum_type(rs.getString("album_type"));
                dtos.add(dto);
            }

        } catch(Exception e) {
            System.out.println("getAlbumsByType 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }

        return dtos;
    }

    // 신규 앨범 저장
    public int saveAlbum(AlbumDto dto) {
        int albumId = 0;

        try {
            con = DBConnection.getConnection();
            String sql = "INSERT INTO albums (album_title, album_type, album_number, release_date, album_description) " +
                        "VALUES (?, ?, ?, ?, ?)";

            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getAlbum_title());
            ps.setString(2, dto.getAlbum_type());
            ps.setString(3, dto.getAlbum_number());
            ps.setString(4, dto.getRelease_date());
            ps.setString(5, dto.getAlbum_description());

            int result = ps.executeUpdate();

            if(result > 0) {
                rs = ps.getGeneratedKeys();
                if(rs.next()) {
                    albumId = rs.getInt(1);
                }
            }

        } catch(Exception e) {
            System.out.println("saveAlbum 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }

        return albumId;
    }
}