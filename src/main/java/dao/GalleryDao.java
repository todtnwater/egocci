package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import common.DBConnection;
import dto.GalleryDto;

public class GalleryDao {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 1. 전체 목록 조회
    public ArrayList<GalleryDto> getGalleryList() {
        ArrayList<GalleryDto> list = new ArrayList<>();
        try {
            con = DBConnection.getConnection();
            String sql = "SELECT * FROM gallery WHERE is_active = true " +
                         "ORDER BY display_order ASC, created_at DESC";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapGallery(rs));
            }
        } catch (Exception e) {
            System.err.println("getGalleryList 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return list;
    }

    // 2. 상세 조회
    public GalleryDto getGalleryView(int galleryId) {
        GalleryDto dto = null;
        try {
            con = DBConnection.getConnection();
            String sql = "SELECT * FROM gallery WHERE gallery_id = ? AND is_active = true";
            ps = con.prepareStatement(sql);
            ps.setInt(1, galleryId);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = mapGallery(rs);
            }
        } catch (Exception e) {
            System.err.println("getGalleryView 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return dto;
    }

    // 3. 저장
    public boolean saveGallery(GalleryDto dto) {
        try {
            con = DBConnection.getConnection();
            String sql = "INSERT INTO gallery (title, image_file, description, display_order) " +
                         "VALUES (?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getTitle());
            ps.setString(2, dto.getImage_file());
            ps.setString(3, dto.getDescription());
            ps.setInt(4, dto.getDisplay_order());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("saveGallery 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
    }

    // 4. 삭제 (Soft Delete)
    public boolean deleteGallery(int galleryId) {
        try {
            con = DBConnection.getConnection();
            String sql = "UPDATE gallery SET is_active = false WHERE gallery_id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, galleryId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("deleteGallery 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
    }

    private GalleryDto mapGallery(ResultSet rs) throws Exception {
        GalleryDto dto = new GalleryDto();
        dto.setGallery_id(rs.getInt("gallery_id"));
        dto.setTitle(rs.getString("title"));
        dto.setImage_file(rs.getString("image_file"));
        dto.setDescription(rs.getString("description"));
        dto.setDisplay_order(rs.getInt("display_order"));
        dto.setIs_active(rs.getBoolean("is_active"));
        dto.setCreated_at(rs.getString("created_at"));
        return dto;
    }
}
