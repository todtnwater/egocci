package command.gallery;

import java.util.ArrayList;

import common.CommonExecute;
import dao.GalleryDao;
import dto.GalleryDto;
import jakarta.servlet.http.HttpServletRequest;

public class GalleryList implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        GalleryDao dao = new GalleryDao();
        ArrayList<GalleryDto> list = dao.getGalleryList();
        request.setAttribute("galleryList", list);
    }
}
