package command.scheduled;

import java.util.ArrayList;

import common.CommonExecute;
import common.CommonUtil;
import dao.ScheduledDao;
import dto.ScheduledDto;
import jakarta.servlet.http.HttpServletRequest;

public class ScheduledList implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        try {
            ScheduledDao dao = new ScheduledDao();
            ArrayList<ScheduledDto> list = dao.getPerformanceList();

            ArrayList<ScheduledDto> upcomingList = new ArrayList<>();
            ArrayList<ScheduledDto> pastList     = new ArrayList<>();

            String today = CommonUtil.getToday();

            for (ScheduledDto dto : list) {
                if (dto.getPerformance_date() != null && dto.getPerformance_date().compareTo(today) >= 0) {
                    upcomingList.add(dto);
                } else {
                    pastList.add(dto);
                }
            }

            request.setAttribute("upcomingList", upcomingList);
            request.setAttribute("pastList", pastList);

        } catch (Exception e) {
            System.err.println("ScheduledList 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
