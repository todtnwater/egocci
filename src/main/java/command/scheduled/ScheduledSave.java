package command.scheduled;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import common.CommonExecute;
import common.CommonUtil;
import dao.ScheduledDao;
import dto.ScheduledDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

public class ScheduledSave implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String level = (String) session.getAttribute("sessionLevel");

        if (level == null || !"top".equals(level)) {
            request.setAttribute("t_msg", "관리자 권한이 필요합니다.");
            request.setAttribute("t_url", "Scheduled");
            return;
        }

        try {
            // 포스터 이미지 → 임시 저장 후 SFTP 업로드
            String posterImage = null;
            Part filePart = request.getPart("t_poster_image");
            if (filePart != null && filePart.getSize() > 0) {
                String originalName = filePart.getSubmittedFileName();
                if (originalName != null && !originalName.trim().isEmpty()) {
                    String savedName = System.currentTimeMillis() + "_" + originalName;
                    File tempFile = File.createTempFile("sc_", "_" + originalName);
                    try (InputStream is = filePart.getInputStream()) {
                        Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    boolean uploaded = CommonUtil.uploadScheduledToSFTP(tempFile, savedName);
                    tempFile.delete();
                    if (uploaded) {
                        posterImage = savedName;
                        System.out.println("[ScheduledSave] SFTP 업로드 성공: " + savedName);
                    } else {
                        System.out.println("[ScheduledSave] SFTP 업로드 실패");
                    }
                }
            }

            String title       = CommonUtil.setQuote(request.getParameter("t_title"));
            String venue       = CommonUtil.setQuote(request.getParameter("t_venue"));
            String date        = request.getParameter("t_performance_date");
            String time        = request.getParameter("t_performance_time");
            String ticketUrl   = request.getParameter("t_ticket_url");
            String description = request.getParameter("t_description");
            if (description != null && !description.trim().isEmpty()) {
                description = CommonUtil.setQuote(description);
            }

            ScheduledDto dto = new ScheduledDto();
            dto.setTitle(title);
            dto.setVenue(venue);
            dto.setPerformance_date(date);
            dto.setPerformance_time((time != null && !time.trim().isEmpty()) ? time : null);
            dto.setTicket_url((ticketUrl != null && !ticketUrl.trim().isEmpty()) ? ticketUrl : null);
            dto.setPoster_image(posterImage);
            dto.setDescription(description);
            dto.setDisplay_order(0);

            ScheduledDao dao = new ScheduledDao();
            int id = dao.savePerformance(dto);

            if (id > 0) {
                request.setAttribute("t_msg", "공연이 등록되었습니다.");
                request.setAttribute("t_url", "Scheduled");
            } else {
                request.setAttribute("t_msg", "공연 등록에 실패했습니다.");
                request.setAttribute("t_url", "Scheduled?t_gubun=writeForm");
            }

        } catch (Exception e) {
            System.err.println("[ScheduledSave] 오류: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("t_msg", "시스템 오류가 발생했습니다.");
            request.setAttribute("t_url", "Scheduled?t_gubun=writeForm");
        }
    }
}
