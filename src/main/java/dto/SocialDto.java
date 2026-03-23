package dto;

public class SocialDto {
    private String link_id;
    private String platform_name;
    private String platform_url;
    private String platform_icon;
    private int display_order;
    private boolean is_active;
    private String created_at;
    private String updated_at;

    public SocialDto() {}

    // Getter/Setter
    public String getLink_id() { return link_id; }
    public void setLink_id(String link_id) { this.link_id = link_id; }

    public String getPlatform_name() { return platform_name; }
    public void setPlatform_name(String platform_name) { this.platform_name = platform_name; }

    public String getPlatform_url() { return platform_url; }
    public void setPlatform_url(String platform_url) { this.platform_url = platform_url; }

    public String getPlatform_icon() { return platform_icon; }
    public void setPlatform_icon(String platform_icon) { this.platform_icon = platform_icon; }

    public int getDisplay_order() { return display_order; }
    public void setDisplay_order(int display_order) { this.display_order = display_order; }

    public boolean getIs_active() { return is_active; }
    public void setIs_active(boolean is_active) { this.is_active = is_active; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }
}