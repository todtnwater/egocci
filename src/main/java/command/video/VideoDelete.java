package command.video;

import common.CommonExecute;
import dao.VideoDao;
import jakarta.servlet.http.HttpServletRequest;

public class VideoDelete implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        try {
            int videoId = Integer.parseInt(request.getParameter("t_no"));
            VideoDao dao = new VideoDao();
            boolean result = dao.deleteVideo(videoId);
            request.setAttribute("t_msg", result ? "영상이 삭제되었습니다." : "삭제 실패!");
            request.setAttribute("t_url", "Video?t_gubun=list");
        } catch (Exception e) {
            System.err.println("[VideoDelete] 오류: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("t_msg", "삭제 중 오류가 발생했습니다.");
            request.setAttribute("t_url", "Video?t_gubun=list");
        }
    }
}
