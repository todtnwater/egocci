package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import common.DBConnection;
import dto.VideoDto;

public class VideoDao {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 1. 타입별 목록 조회
    public ArrayList<VideoDto> getVideosByType(String videoType) {
        ArrayList<VideoDto> list = new ArrayList<>();
        try {
            con = DBConnection.getConnection();
            String sql = "SELECT * FROM videos WHERE is_active = true AND video_type = ? " +
                         "ORDER BY display_order ASC, created_at DESC";
            ps = con.prepareStatement(sql);
            ps.setString(1, videoType);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapVideo(rs));
            }
        } catch (Exception e) {
            System.err.println("getVideosByType 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return list;
    }

    // 2. 전체 목록 조회 (관리자용)
    public ArrayList<VideoDto> getAllVideos() {
        ArrayList<VideoDto> list = new ArrayList<>();
        try {
            con = DBConnection.getConnection();
            String sql = "SELECT * FROM videos WHERE is_active = true " +
                         "ORDER BY video_type ASC, display_order ASC, created_at DESC";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapVideo(rs));
            }
        } catch (Exception e) {
            System.err.println("getAllVideos 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return list;
    }

    // 3. 상세 조회
    public VideoDto getVideoView(int videoId) {
        VideoDto dto = null;
        try {
            con = DBConnection.getConnection();
            String sql = "SELECT * FROM videos WHERE video_id = ? AND is_active = true";
            ps = con.prepareStatement(sql);
            ps.setInt(1, videoId);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = mapVideo(rs);
            }
        } catch (Exception e) {
            System.err.println("getVideoView 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return dto;
    }

    // 4. 저장
    public boolean saveVideo(VideoDto dto) {
        try {
            con = DBConnection.getConnection();
            String sql = "INSERT INTO videos (video_type, title, youtube_url, thumbnail_url, description, display_order) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getVideo_type());
            ps.setString(2, dto.getTitle());
            ps.setString(3, dto.getYoutube_url());
            ps.setString(4, dto.getThumbnail_url());
            ps.setString(5, dto.getDescription());
            ps.setInt(6, dto.getDisplay_order());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("saveVideo 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
    }

    // 5. 수정
    public boolean updateVideo(VideoDto dto) {
        try {
            con = DBConnection.getConnection();
            String sql = "UPDATE videos SET video_type = ?, title = ?, youtube_url = ?, " +
                         "thumbnail_url = ?, description = ?, display_order = ? " +
                         "WHERE video_id = ? AND is_active = true";
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getVideo_type());
            ps.setString(2, dto.getTitle());
            ps.setString(3, dto.getYoutube_url());
            ps.setString(4, dto.getThumbnail_url());
            ps.setString(5, dto.getDescription());
            ps.setInt(6, dto.getDisplay_order());
            ps.setInt(7, dto.getVideo_id());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("updateVideo 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
    }

    // 6. 삭제 (Soft Delete)
    public boolean deleteVideo(int videoId) {
        try {
            con = DBConnection.getConnection();
            String sql = "UPDATE videos SET is_active = false WHERE video_id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, videoId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("deleteVideo 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
    }

    // 7. 메인 영상 조회
    public VideoDto getMainVideo() {
        VideoDto dto = null;
        try {
            con = DBConnection.getConnection();
            String sql = "SELECT * FROM videos WHERE is_active = true AND is_main = true LIMIT 1";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = mapVideo(rs);
            }
        } catch (Exception e) {
            System.err.println("getMainVideo 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return dto;
    }

    // 8. 메인 영상 설정 (기존 메인 해제 후 새 메인 설정)
    public boolean setMainVideo(int videoId) {
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // 기존 메인 전체 해제
            ps = con.prepareStatement("UPDATE videos SET is_main = false WHERE is_main = true");
            ps.executeUpdate();
            ps.close();

            // 새 메인 설정
            ps = con.prepareStatement("UPDATE videos SET is_main = true WHERE video_id = ? AND is_active = true");
            ps.setInt(1, videoId);
            int result = ps.executeUpdate();

            con.commit();
            return result > 0;
        } catch (Exception e) {
            System.err.println("setMainVideo 오류: " + e.getMessage());
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            return false;
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (Exception ex) {}
            DBConnection.closeDB(con, ps, rs);
        }
    }

    private VideoDto mapVideo(ResultSet rs) throws Exception {
        VideoDto dto = new VideoDto();
        dto.setVideo_id(rs.getInt("video_id"));
        dto.setVideo_type(rs.getString("video_type"));
        dto.setTitle(rs.getString("title"));
        dto.setYoutube_url(rs.getString("youtube_url"));
        dto.setThumbnail_url(rs.getString("thumbnail_url"));
        dto.setDescription(rs.getString("description"));
        dto.setDisplay_order(rs.getInt("display_order"));
        dto.setIs_active(rs.getBoolean("is_active"));
        dto.setIs_main(rs.getBoolean("is_main"));
        dto.setCreated_at(rs.getString("created_at"));
        return dto;
    }
}