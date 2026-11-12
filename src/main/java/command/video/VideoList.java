package command.video;

import java.util.ArrayList;

import common.CommonExecute;
import dao.VideoDao;
import dto.VideoDto;
import jakarta.servlet.http.HttpServletRequest;

public class VideoList implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        VideoDao dao = new VideoDao();
        ArrayList<VideoDto> mvList   = dao.getVideosByType("mv");
        ArrayList<VideoDto> liveList = dao.getVideosByType("live");
        ArrayList<VideoDto> etcList  = dao.getVideosByType("etc");
        VideoDto mainVideo = dao.getMainVideo();
        request.setAttribute("mvList",    mvList);
        request.setAttribute("liveList",  liveList);
        request.setAttribute("etcList",   etcList);
        request.setAttribute("mainVideo", mainVideo);
    }
}