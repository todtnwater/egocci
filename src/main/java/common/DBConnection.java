package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

public class DBConnection {
    private static BasicDataSource dataSource = null;

    // 연결 풀 초기화
    static {
        try {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            // MySQL 8 최적화된 URL
            dataSource.setUrl("jdbc:mysql://XXX.XXX.XXX.XXX/XXXX/");
            dataSource.setUsername("XX");
            dataSource.setPassword("XXXX");

            // 연결 풀 설정
            dataSource.setMaxTotal(8);      // 최대 8개 연결
            dataSource.setMaxIdle(4);       // 유휴 4개
            dataSource.setMinIdle(2);       // 최소 2개
            dataSource.setInitialSize(2);   // 시작시 2개
            dataSource.setMaxWaitMillis(3000); // 3초 대기

            // 연결 검증 설정
            dataSource.setValidationQuery("SELECT 1");
            dataSource.setTestOnBorrow(true);
            dataSource.setTestWhileIdle(true);

            System.out.println("✓ DB 연결 풀 초기화 완료");

        } catch (Exception e) {
            System.err.println("✗ DB 연결 풀 초기화 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 연결 풀에서 연결 가져오기
    public static Connection getConnection() {
        try {
            if (dataSource != null) {
                Connection conn = dataSource.getConnection();
                return conn;
            }
        } catch (SQLException e) {
            System.err.println("연결 풀에서 연결 실패: " + e.getMessage());
        }

        // 연결 풀 실패시 기본 연결 시도
        return getDirectConnection();
    }

    // 직접 연결 (연결 풀 실패시 백업용)
    private static Connection getDirectConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://XXX.XXX.XXX.XXX/XXXX/";
            return DriverManager.getConnection(url, "XX", "XXXX");
        } catch (Exception e) {
            System.err.println("직접 연결 실패: " + e.getMessage());
            return null;
        }
    }

    public static void closeDB(Connection con, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (ps != null) {
            try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (con != null) {
            try {
                con.close(); // 연결 풀로 반환
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 연결 풀 상태 확인
    public static void printPoolStatus() {
        if (dataSource != null) {
            System.out.println("=== DB 연결 풀 상태 ===");
            System.out.println("활성 연결: " + dataSource.getNumActive());
            System.out.println("유휴 연결: " + dataSource.getNumIdle());
            System.out.println("최대 연결: " + dataSource.getMaxTotal());
        }
    }
}
