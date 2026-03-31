package command.gallery;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import common.CommonExecute;
import common.CommonUtil;
import dao.GalleryDao;
import dto.GalleryDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

public class GallerySave implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String level = (String) session.getAttribute("sessionLevel");
        if (level == null || !"top".equals(level)) {
            request.setAttribute("t_msg", "관리자 권한이 필요합니다.");
            request.setAttribute("t_url", "Gallery?t_gubun=list");
            return;
        }

        try {
            String title       = CommonUtil.setQuote(request.getParameter("t_title"));
            String description = request.getParameter("t_description");
            String orderStr    = request.getParameter("t_display_order");

            int displayOrder = 0;
            if (orderStr != null && !orderStr.trim().isEmpty()) {
                try { displayOrder = Integer.parseInt(orderStr.trim()); } catch (Exception e) {}
            }

            // 이미지 업로드 (필수)
            Part filePart = request.getPart("t_image");
            if (filePart == null || filePart.getSize() == 0) {
                request.setAttribute("t_msg", "이미지를 선택해주세요.");
                request.setAttribute("t_url", "Gallery?t_gubun=writeForm");
                return;
            }

            String originalName = filePart.getSubmittedFileName();
            String savedName = System.currentTimeMillis() + "_" + originalName;
            File tempFile = File.createTempFile("gallery_", "_" + originalName);
            try (InputStream is = filePart.getInputStream()) {
                Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            boolean uploaded = CommonUtil.uploadGalleryToSFTP(tempFile, savedName);
            tempFile.delete();

            if (!uploaded) {
                request.setAttribute("t_msg", "이미지 업로드에 실패했습니다.");
                request.setAttribute("t_url", "Gallery?t_gubun=writeForm");
                return;
            }

            GalleryDto dto = new GalleryDto();
            dto.setTitle(title);
            dto.setImage_file(savedName);
            dto.setDescription((description != null && !description.trim().isEmpty()) ? CommonUtil.setQuote(description) : null);
            dto.setDisplay_order(displayOrder);

            GalleryDao dao = new GalleryDao();
            boolean result = dao.saveGallery(dto);

            request.setAttribute("t_msg", result ? "사진이 등록되었습니다." : "등록 실패!");
            request.setAttribute("t_url", "Gallery?t_gubun=list");

        } catch (Exception e) {
            System.err.println("[GallerySave] 오류: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("t_msg", "등록 중 오류가 발생했습니다.");
            request.setAttribute("t_url", "Gallery?t_gubun=list");
        }
    }
}
