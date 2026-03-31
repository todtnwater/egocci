package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import common.DBConnection;
import dto.BusinessMailDto;

public class BusinessMailDao {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public int saveMail(BusinessMailDto dto) {
        int mailId = 0;
        try {
            con = DBConnection.getConnection();

            String sql = "INSERT INTO business_mail " +
                         "(sender_email, sender_name, recipient_email, subject, content, send_status) " +
                         "VALUES (?, ?, ?, ?, ?, 'READY')";

            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getSenderEmail());
            ps.setString(2, dto.getSenderName());
            ps.setString(3, dto.getRecipientEmail());
            ps.setString(4, dto.getSubject());
            ps.setString(5, dto.getContent());

            int result = ps.executeUpdate();

            if(result > 0) {
                rs = ps.getGeneratedKeys();
                if(rs.next()) {
                    mailId = rs.getInt(1);
                }
            }
        } catch(Exception e) {
            System.out.println("saveMail 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return mailId;
    }

    public int updateSendSuccess(int mailId) {
        int result = 0;
        try {
            con = DBConnection.getConnection();

            String sql = "UPDATE business_mail " +
                         "SET send_status='SUCCESS', sent_at=NOW(), fail_reason=NULL " +
                         "WHERE mail_id=?";

            ps = con.prepareStatement(sql);
            ps.setInt(1, mailId);

            result = ps.executeUpdate();
        } catch(Exception e) {
            System.out.println("updateSendSuccess 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return result;
    }

    public int updateSendFail(int mailId, String failReason) {
        int result = 0;
        try {
            con = DBConnection.getConnection();

            String sql = "UPDATE business_mail " +
                         "SET send_status='FAIL', fail_reason=? " +
                         "WHERE mail_id=?";

            ps = con.prepareStatement(sql);
            ps.setString(1, failReason);
            ps.setInt(2, mailId);

            result = ps.executeUpdate();
        } catch(Exception e) {
            System.out.println("updateSendFail 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return result;
    }

    public ArrayList<BusinessMailDto> getMailList() {
        ArrayList<BusinessMailDto> list = new ArrayList<>();
        try {
            con = DBConnection.getConnection();

            String sql = "SELECT mail_id, sender_email, sender_name, recipient_email, subject, " +
                         "send_status, fail_reason, created_at, sent_at, is_deleted " +
                         "FROM business_mail " +
                         "WHERE is_deleted = 0 " +
                         "ORDER BY mail_id DESC";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next()) {
                BusinessMailDto dto = new BusinessMailDto();
                dto.setMailId(rs.getInt("mail_id"));
                dto.setSenderEmail(rs.getString("sender_email"));
                dto.setSenderName(rs.getString("sender_name"));
                dto.setRecipientEmail(rs.getString("recipient_email"));
                dto.setSubject(rs.getString("subject"));
                dto.setSendStatus(rs.getString("send_status"));
                dto.setFailReason(rs.getString("fail_reason"));
                dto.setCreatedAt(rs.getString("created_at"));
                dto.setSentAt(rs.getString("sent_at"));
                dto.setIsDeleted(rs.getInt("is_deleted"));
                list.add(dto);
            }
        } catch(Exception e) {
            System.out.println("getMailList 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return list;
    }

    public BusinessMailDto getMailView(int mailId) {
        BusinessMailDto dto = null;
        try {
            con = DBConnection.getConnection();

            String sql = "SELECT * FROM business_mail WHERE mail_id=? AND is_deleted=0";
            ps = con.prepareStatement(sql);
            ps.setInt(1, mailId);
            rs = ps.executeQuery();

            if(rs.next()) {
                dto = new BusinessMailDto();
                dto.setMailId(rs.getInt("mail_id"));
                dto.setSenderEmail(rs.getString("sender_email"));
                dto.setSenderName(rs.getString("sender_name"));
                dto.setRecipientEmail(rs.getString("recipient_email"));
                dto.setSubject(rs.getString("subject"));
                dto.setContent(rs.getString("content"));
                dto.setSendStatus(rs.getString("send_status"));
                dto.setFailReason(rs.getString("fail_reason"));
                dto.setCreatedAt(rs.getString("created_at"));
                dto.setSentAt(rs.getString("sent_at"));
                dto.setIsDeleted(rs.getInt("is_deleted"));
            }
        } catch(Exception e) {
            System.out.println("getMailView 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return dto;
    }

    public int deleteMail(int mailId) {
        int result = 0;
        try {
            con = DBConnection.getConnection();

            String sql = "UPDATE business_mail SET is_deleted=1 WHERE mail_id=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, mailId);

            result = ps.executeUpdate();
        } catch(Exception e) {
            System.out.println("deleteMail 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeDB(con, ps, rs);
        }
        return result;
    }
}