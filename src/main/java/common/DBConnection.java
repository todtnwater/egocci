package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {

    public static void closeDB(Connection con,
                               PreparedStatement ps,
                               ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (ps != null) {
            try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (con != null) {
            try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static Connection getConnection() {
        Connection con = null;

        // 1. MySQL 드라이버 로딩
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("MySQL 드라이버 없음!");
            return null;
        }

        // 2. MySQL DB 접속 정보 - 권한 설정 완료된 상태
        String db_url = "jdbc:mysql://175.213.151.10:3306/egocci_music?useSSL=false&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true";
        String db_user = "ss";
        String db_password = "iotiot";

        try {
            con = DriverManager.getConnection(db_url, db_user, db_password);
            System.out.println("MySQL 연결 성공! - egocci_music DB");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("MySQL 계정 설정 오류!");
            System.out.println("오류 상세: " + e.getMessage());
        }

        return con;
    }
    
    // 테스트 메서드 - 개발용
    public static void testConnectionWithQuery() {
        Connection testCon = getConnection();
        if (testCon != null) {
            try {
                // 테이블 존재 확인
                PreparedStatement ps = testCon.prepareStatement("SHOW TABLES");
                ResultSet rs = ps.executeQuery();
                
                System.out.println("=== egocci_music 데이터베이스 테이블 목록 ===");
                while (rs.next()) {
                    System.out.println("- " + rs.getString(1));
                }
                
                // streaming_platforms 데이터 확인
                ps = testCon.prepareStatement("SELECT COUNT(*) as cnt FROM streaming_platforms");
                rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("streaming_platforms 테이블 데이터 수: " + rs.getInt("cnt"));
                }
                
                closeDB(testCon, ps, rs);
                System.out.println("✓ 데이터베이스 연결 및 테이블 접근 성공!");
                
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("✗ 쿼리 실행 실패");
            }
        } else {
            System.out.println("✗ 데이터베이스 연결 실패");
        }
    }
}