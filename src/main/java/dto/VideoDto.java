package dto;

public class VideoDto {
    private int video_id;
    private String video_type;
    private String title;
    private String youtube_url;
    private String thumbnail_url;
    private String description;
    private int display_order;
    private boolean is_active;
    private boolean is_main;
    private String created_at;
    private String updated_at;

    public VideoDto() {}

    public int getVideo_id() { return video_id; }
    public void setVideo_id(int video_id) { this.video_id = video_id; }

    public String getVideo_type() { return video_type; }
    public void setVideo_type(String video_type) { this.video_type = video_type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getYoutube_url() { return youtube_url; }
    public void setYoutube_url(String youtube_url) { this.youtube_url = youtube_url; }

    public String getThumbnail_url() { return thumbnail_url; }
    public void setThumbnail_url(String thumbnail_url) { this.thumbnail_url = thumbnail_url; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDisplay_order() { return display_order; }
    public void setDisplay_order(int display_order) { this.display_order = display_order; }

    public boolean getIs_active() { return is_active; }
    public void setIs_active(boolean is_active) { this.is_active = is_active; }

    public boolean getIs_main() { return is_main; }
    public void setIs_main(boolean is_main) { this.is_main = is_main; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }

    // 유튜브 URL에서 video ID 추출 (썸네일 자동 생성용)
    public String getYoutubeVideoId() {
        if (youtube_url == null || youtube_url.trim().isEmpty()) {
            return "";
        }
        // https://www.youtube.com/watch?v=XXXXX
        if (youtube_url.contains("v=")) {
            String id = youtube_url.substring(youtube_url.indexOf("v=") + 2);
            if (id.contains("&")) {
                id = id.substring(0, id.indexOf("&"));
            }
            return id;
        }
        // https://youtu.be/XXXXX
        if (youtube_url.contains("youtu.be/")) {
            String id = youtube_url.substring(youtube_url.indexOf("youtu.be/") + 9);
            if (id.contains("?")) {
                id = id.substring(0, id.indexOf("?"));
            }
            return id;
        }
        return "";
    }

    // 유튜브 썸네일 URL 자동 생성
    public String getAutoThumbnail() {
        String videoId = getYoutubeVideoId();
        if (videoId.isEmpty()) {
            return "";
        }
        return "https://img.youtube.com/vi/" + videoId + "/mqdefault.jpg";
    }

    // 유튜브 embed URL 생성
    public String getEmbedUrl() {
        String videoId = getYoutubeVideoId();
        if (videoId.isEmpty()) {
            return "";
        }
        return "https://www.youtube.com/embed/" + videoId;
    }
}