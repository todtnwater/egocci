package command.scheduled;

import common.CommonExecute;
import dao.ScheduledDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class ScheduledDelete implements CommonExecute {
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
            int performanceId = Integer.parseInt(request.getParameter("t_no"));
            ScheduledDao dao = new ScheduledDao();
            boolean result = dao.deletePerformance(performanceId);

            request.setAttribute("t_msg", result ? "공연이 삭제되었습니다." : "삭제에 실패했습니다.");
            request.setAttribute("t_url", "Scheduled");

        } catch (Exception e) {
            System.err.println("[ScheduledDelete] 오류: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("t_msg", "삭제 중 오류가 발생했습니다.");
            request.setAttribute("t_url", "Scheduled");
        }
    }
}
