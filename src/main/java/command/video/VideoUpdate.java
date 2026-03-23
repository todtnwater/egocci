package command.video;

import common.CommonExecute;
import common.CommonUtil;
import dao.VideoDao;
import dto.VideoDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class VideoUpdate implements CommonExecute {
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
            int videoId        = Integer.parseInt(request.getParameter("t_no"));
            String videoType   = request.getParameter("t_video_type");
            String title       = CommonUtil.setQuote(request.getParameter("t_title"));
            String youtubeUrl  = request.getParameter("t_youtube_url");
            String thumbUrl    = request.getParameter("t_thumbnail_url");
            String description = request.getParameter("t_description");
            String orderStr    = request.getParameter("t_display_order");

            int displayOrder = 0;
            if (orderStr != null && !orderStr.trim().isEmpty()) {
                try { displayOrder = Integer.parseInt(orderStr.trim()); } catch (Exception e) {}
            }

            VideoDto dto = new VideoDto();
            dto.setVideo_id(videoId);
            dto.setVideo_type(videoType);
            dto.setTitle(title);
            dto.setYoutube_url(youtubeUrl);
            dto.setThumbnail_url((thumbUrl != null && !thumbUrl.trim().isEmpty()) ? thumbUrl : null);
            dto.setDescription((description != null && !description.trim().isEmpty()) ? CommonUtil.setQuote(description) : null);
            dto.setDisplay_order(displayOrder);

            VideoDao dao = new VideoDao();
            boolean result = dao.updateVideo(dto);

            request.setAttribute("t_msg", result ? "영상이 수정되었습니다." : "수정 실패!");
            request.setAttribute("t_url", "Video?t_gubun=list");

        } catch (Exception e) {
            System.err.println("[VideoUpdate] 오류: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("t_msg", "수정 중 오류가 발생했습니다.");
            request.setAttribute("t_url", "Video?t_gubun=list");
        }
    }
}
