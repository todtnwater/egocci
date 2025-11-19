package dao;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.DBConnection;
import dto.MemberDto;

public class MemberDao {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 로그인 - DTO 반환으로 변경
    public MemberDto memberLogin(String email, String password) {
        MemberDto dto = null;
        try {
            con = DBConnection.getConnection();
            String sql = "SELECT name, role FROM egocci_member WHERE email=? AND password=? AND exited=0";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                dto = new MemberDto();
                dto.setName(rs.getString("name"));
                dto.setRole(rs.getString("role"));
                dto.setEmail(email);
            }
        } catch(Exception e) {
            System.out.println("memberLogin 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return dto;
    }

    // 회원가입 - role 기본값 추가
    public int memberSave(MemberDto dto) {
        int result = 0;
        try {
            con = DBConnection.getConnection();
            String sql = "INSERT INTO egocci_member (email, password, name, phone1, phone2, phone3, gender, role) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, 'user')";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getEmail());
            ps.setString(2, dto.getPassword());
            ps.setString(3, dto.getName());
            ps.setString(4, dto.getPhone1());
            ps.setString(5, dto.getPhone2());
            ps.setString(6, dto.getPhone3());
            ps.setString(7, dto.getGender());
            
            result = ps.executeUpdate();
        } catch(Exception e) {
            System.out.println("memberSave 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return result;
    }

    // ID 중복 체크
    public int memberCheckId(String email) {
        int count = 0;
        try {
            con = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM egocci_member WHERE email=?";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                count = rs.getInt(1);
            }
        } catch(Exception e) {
            System.out.println("memberCheckId 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return count;
    }

    // SHA-256 암호화
    public String encryptSHA256(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        
        StringBuilder builder = new StringBuilder();
        for (byte b : md.digest()) {
            builder.append(String.format("%02X", b));
        }
        return builder.toString();
    }
}