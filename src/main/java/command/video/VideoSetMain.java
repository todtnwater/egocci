package command.video;
 
import common.CommonExecute;
import dao.VideoDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
 
public class VideoSetMain implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String level = (String) session.getAttribute("sessionLevel");
        if (level == null || !"top".equals(level)) {
            request.setAttribute("t_msg", "관리자 권한이 필요합니다.");
            request.setAttribute("t_url", "Video?t_gubun=list");
            return;
        }
 
        try {
            int videoId = Integer.parseInt(request.getParameter("t_no"));
            VideoDao dao = new VideoDao();
            boolean result = dao.setMainVideo(videoId);
            request.setAttribute("t_msg", result ? "메인 영상이 변경되었습니다." : "메인 영상 설정 실패!");
            request.setAttribute("t_url", "Video?t_gubun=list");
        } catch (Exception e) {
            System.err.println("[VideoSetMain] 오류: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("t_msg", "처리 중 오류가 발생했습니다.");
            request.setAttribute("t_url", "Video?t_gubun=list");
        }
    }
}
 