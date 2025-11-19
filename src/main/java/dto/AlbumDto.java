package dto;

public class AlbumDto {
    private int album_id;
    private String album_title;
    private String album_type;
    private String album_number;
    private String release_date;
    private String album_cover_image;
    private String album_description;
    private int total_tracks;
    private int display_order;
    private boolean is_active;
    private String created_at;
    private String updated_at;
    
    public AlbumDto() {}
    
    public int getAlbum_id() { return album_id; }
    public void setAlbum_id(int album_id) { this.album_id = album_id; }
    
    public String getAlbum_title() { return album_title; }
    public void setAlbum_title(String album_title) { this.album_title = album_title; }
    
    public String getAlbum_type() { return album_type; }
    public void setAlbum_type(String album_type) { this.album_type = album_type; }
    
    public String getAlbum_number() { return album_number; }
    public void setAlbum_number(String album_number) { this.album_number = album_number; }
    
    public String getRelease_date() { return release_date; }
    public void setRelease_date(String release_date) { this.release_date = release_date; }
    
    public String getAlbum_cover_image() { return album_cover_image; }
    public void setAlbum_cover_image(String album_cover_image) { this.album_cover_image = album_cover_image; }
    
    public String getAlbum_description() { return album_description; }
    public void setAlbum_description(String album_description) { this.album_description = album_description; }
    
    public int getTotal_tracks() { return total_tracks; }
    public void setTotal_tracks(int total_tracks) { this.total_tracks = total_tracks; }
    
    public int getDisplay_order() { return display_order; }
    public void setDisplay_order(int display_order) { this.display_order = display_order; }
    
    public boolean getIs_active() { return is_active; }
    public void setIs_active(boolean is_active) { this.is_active = is_active; }
    
    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }
    
    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }
}