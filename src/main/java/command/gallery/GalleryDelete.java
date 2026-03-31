package command.gallery;

import common.CommonExecute;
import common.CommonUtil;
import dao.GalleryDao;
import dto.GalleryDto;
import jakarta.servlet.http.HttpServletRequest;

public class GalleryDelete implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        try {
            int galleryId = Integer.parseInt(request.getParameter("t_no"));
            GalleryDao dao = new GalleryDao();

            // 삭제 전 이미지 파일명 가져오기
            GalleryDto existing = dao.getGalleryView(galleryId);

            boolean result = dao.deleteGallery(galleryId);

            // SFTP 이미지 삭제
            if (result && existing != null && existing.getImage_file() != null) {
                CommonUtil.deleteGalleryFromSFTP(existing.getImage_file());
            }

            request.setAttribute("t_msg", result ? "사진이 삭제되었습니다." : "삭제 실패!");
            request.setAttribute("t_url", "Gallery?t_gubun=list");

        } catch (Exception e) {
            System.err.println("[GalleryDelete] 오류: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("t_msg", "삭제 중 오류가 발생했습니다.");
            request.setAttribute("t_url", "Gallery?t_gubun=list");
        }
    }
}
