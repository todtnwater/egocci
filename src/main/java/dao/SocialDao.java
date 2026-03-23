package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import common.DBConnection;
import dto.SocialDto;

public class SocialDao {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 활성화된 소셜 링크 목록 가져오기
    public ArrayList<SocialDto> getActiveSocialLinks() {
        ArrayList<SocialDto> dtos = new ArrayList<>();

        try {
            con = DBConnection.getConnection();
            if(con == null) {
                System.out.println("데이터베이스 연결 실패 - getActiveSocialLinks");
                return dtos;
            }

            String sql = "SELECT * FROM social_links WHERE is_active = TRUE ORDER BY display_order";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next()) {
                SocialDto dto = new SocialDto();
                dto.setLink_id(rs.getString("link_id"));
                dto.setPlatform_name(rs.getString("platform_name"));
                dto.setPlatform_url(rs.getString("platform_url"));
                dto.setPlatform_icon(rs.getString("platform_icon"));
                dto.setDisplay_order(rs.getInt("display_order"));
                dto.setIs_active(rs.getBoolean("is_active"));
                dtos.add(dto);
            }

        } catch(Exception e) {
            System.out.println("getActiveSocialLinks 오류: " + e.getMessage());
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }

        return dtos;
    }
}