package dto;

public class GalleryDto {
    private int gallery_id;
    private String title;
    private String image_file;
    private String description;
    private int display_order;
    private boolean is_active;
    private String created_at;
    private String updated_at;

    public GalleryDto() {}

    public int getGallery_id() { return gallery_id; }
    public void setGallery_id(int gallery_id) { this.gallery_id = gallery_id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImage_file() { return image_file; }
    public void setImage_file(String image_file) { this.image_file = image_file; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDisplay_order() { return display_order; }
    public void setDisplay_order(int display_order) { this.display_order = display_order; }

    public boolean getIs_active() { return is_active; }
    public void setIs_active(boolean is_active) { this.is_active = is_active; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }
}
