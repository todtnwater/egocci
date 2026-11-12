package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import common.DBConnection;
import dto.ScheduledDto;

public class ScheduledDao {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 1. 전체 공연 목록 조회 (날짜 오름차순)
    public ArrayList<ScheduledDto> getPerformanceList() {
        ArrayList<ScheduledDto> list = new ArrayList<>();
        try {
            con = DBConnection.getConnection();
            String sql = "SELECT * FROM scheduled_performances " +
                         "WHERE is_active = 1 " +
                         "ORDER BY performance_date ASC, display_order ASC";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            System.err.println("getPerformanceList 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return list;
    }

    // 2. 공연 상세 조회
    public ScheduledDto getPerformanceView(int performanceId) {
        ScheduledDto dto = null;
        try {
            con = DBConnection.getConnection();
            String sql = "SELECT * FROM scheduled_performances WHERE performance_id = ? AND is_active = 1";
            ps = con.prepareStatement(sql);
            ps.setInt(1, performanceId);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = mapRow(rs);
            }
        } catch (Exception e) {
            System.err.println("getPerformanceView 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return dto;
    }

    // 3. 공연 등록
    public int savePerformance(ScheduledDto dto) {
        int performanceId = 0;
        try {
            con = DBConnection.getConnection();
            String sql = "INSERT INTO scheduled_performances " +
                         "(title, venue, performance_date, performance_time, ticket_url, poster_image, description, display_order) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getTitle());
            ps.setString(2, dto.getVenue());
            ps.setString(3, dto.getPerformance_date());
            ps.setString(4, dto.getPerformance_time());
            ps.setString(5, dto.getTicket_url());
            ps.setString(6, dto.getPoster_image());
            ps.setString(7, dto.getDescription());
            ps.setInt(8, dto.getDisplay_order());

            int result = ps.executeUpdate();
            if (result > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    performanceId = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            System.err.println("savePerformance 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return performanceId;
    }

    // 4. 공연 수정
    public boolean updatePerformance(ScheduledDto dto) {
        try {
            con = DBConnection.getConnection();
            String sql = "UPDATE scheduled_performances " +
                         "SET title = ?, venue = ?, performance_date = ?, performance_time = ?, " +
                         "ticket_url = ?, poster_image = ?, description = ? " +
                         "WHERE performance_id = ? AND is_active = 1";
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getTitle());
            ps.setString(2, dto.getVenue());
            ps.setString(3, dto.getPerformance_date());
            ps.setString(4, dto.getPerformance_time());
            ps.setString(5, dto.getTicket_url());
            ps.setString(6, dto.getPoster_image());
            ps.setString(7, dto.getDescription());
            ps.setInt(8, dto.getPerformance_id());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("updatePerformance 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
    }

    // 5. 공연 삭제 (Soft Delete)
    public boolean deletePerformance(int performanceId) {
        try {
            con = DBConnection.getConnection();
            String sql = "UPDATE scheduled_performances SET is_active = 0 WHERE performance_id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, performanceId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("deletePerformance 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
    }

    // 유틸: ResultSet → ScheduledDto
    private ScheduledDto mapRow(ResultSet rs) throws Exception {
        ScheduledDto dto = new ScheduledDto();
        dto.setPerformance_id(rs.getInt("performance_id"));
        dto.setTitle(rs.getString("title"));
        dto.setVenue(rs.getString("venue"));
        dto.setPerformance_date(rs.getString("performance_date"));
        dto.setPerformance_time(rs.getString("performance_time"));
        dto.setTicket_url(rs.getString("ticket_url"));
        dto.setPoster_image(rs.getString("poster_image"));
        dto.setDescription(rs.getString("description"));
        dto.setIs_active(rs.getBoolean("is_active"));
        dto.setDisplay_order(rs.getInt("display_order"));
        dto.setCreated_at(rs.getString("created_at"));
        return dto;
    }
}
