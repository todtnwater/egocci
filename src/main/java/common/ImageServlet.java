package common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/image/*")
public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String SFTP_HOST = "XXX.XXX.XXX.XXX";
    private static final int SFTP_PORT = 22;
    private static final String SFTP_USER = "XXXXXX";
    private static final String SFTP_PASS = "XXXXXX";
    private static final String SFTP_BASE_PATH = "/XXXXXX/";

    // 메모리 캐시: 파일 경로 → 바이트 배열
    // ConcurrentHashMap으로 멀티스레드 동시 접근 안전하게 처리
    private static final Map<String, byte[]> imageCache = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String fileName = pathInfo.substring(1); // 앞의 / 제거

        // 캐시에 있으면 SFTP 없이 바로 응답
        if (imageCache.containsKey(fileName)) {
            byte[] cachedData = imageCache.get(fileName);
            String mimeType = getServletContext().getMimeType(fileName);
            if (mimeType == null) mimeType = "application/octet-stream";
            response.setContentType(mimeType);
            response.setContentLength(cachedData.length);
            response.setHeader("Cache-Control", "public, max-age=31536000");
            response.setDateHeader("Expires", System.currentTimeMillis() + 31536000000L);
            response.getOutputStream().write(cachedData);
            System.out.println(">>> [ImageServlet] 캐시 히트: " + fileName);
            return;
        }

        // 캐시 미스 → SFTP로 가져오기
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
            session.setPassword(SFTP_PASS);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect(5000); // 연결 타임아웃 5초 명시

            channel = session.openChannel("sftp");
            channel.connect(3000); // 채널 타임아웃 3초 명시
            channelSftp = (ChannelSftp) channel;

            String remotePath = SFTP_BASE_PATH + fileName;
            inputStream = channelSftp.get(remotePath);

            // 바이트 배열로 읽어서 캐시에 저장
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, bytesRead);
            }
            byte[] fileData = byteBuffer.toByteArray();

            // 캐시에 저장 (10MB 이하 파일만 캐시)
            if (fileData.length <= 10 * 1024 * 1024) {
                imageCache.put(fileName, fileData);
                System.out.println(">>> [ImageServlet] 캐시 저장: " + fileName + " (" + fileData.length + " bytes)");
            }

            // 응답 전송
            String mimeType = getServletContext().getMimeType(fileName);
            if (mimeType == null) mimeType = "application/octet-stream";
            response.setContentType(mimeType);
            response.setContentLength(fileData.length);
            response.setHeader("Cache-Control", "public, max-age=31536000");
            response.setDateHeader("Expires", System.currentTimeMillis() + 31536000000L);

            outputStream = response.getOutputStream();
            outputStream.write(fileData);

            System.out.println(">>> [ImageServlet] SFTP 서빙 성공: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(">>> [ImageServlet] 이미지 서빙 실패: " + fileName + " - " + e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "이미지를 찾을 수 없습니다.");
        } finally {
            try { if (inputStream != null) inputStream.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (outputStream != null) outputStream.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (channelSftp != null) channelSftp.disconnect(); } catch (Exception e) { e.printStackTrace(); }
            try { if (channel != null) channel.disconnect(); } catch (Exception e) { e.printStackTrace(); }
            try { if (session != null) session.disconnect(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    // 캐시 전체 비우기 (관리용 - 필요시 호출)
    public static void clearCache() {
        imageCache.clear();
        System.out.println(">>> [ImageServlet] 캐시 전체 초기화");
    }
}
