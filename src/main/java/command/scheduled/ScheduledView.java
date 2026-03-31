package command.scheduled;

import common.CommonExecute;
import dao.ScheduledDao;
import dto.ScheduledDto;
import jakarta.servlet.http.HttpServletRequest;

public class ScheduledView implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        try {
            String noStr = request.getParameter("t_no");
            if (noStr == null || noStr.trim().isEmpty()) {
                request.setAttribute("t_msg", "공연 ID가 없습니다.");
                request.setAttribute("t_url", "Scheduled");
                return;
            }
            int performanceId = Integer.parseInt(noStr);
            ScheduledDao dao = new ScheduledDao();
            ScheduledDto dto = dao.getPerformanceView(performanceId);
            request.setAttribute("t_dto", dto);
        } catch (Exception e) {
            System.err.println("ScheduledView 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
