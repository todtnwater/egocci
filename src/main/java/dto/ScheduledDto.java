package dto;

public class ScheduledDto {
    private int performance_id;
    private String title;
    private String venue;
    private String performance_date;
    private String performance_time;
    private String ticket_url;
    private String poster_image;
    private String description;
    private boolean is_active;
    private int display_order;
    private String created_at;
    private String updated_at;

    public ScheduledDto() {}

    public int getPerformance_id() { return performance_id; }
    public void setPerformance_id(int performance_id) { this.performance_id = performance_id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getPerformance_date() { return performance_date; }
    public void setPerformance_date(String performance_date) { this.performance_date = performance_date; }

    public String getPerformance_time() { return performance_time; }
    public void setPerformance_time(String performance_time) { this.performance_time = performance_time; }

    public String getTicket_url() { return ticket_url; }
    public void setTicket_url(String ticket_url) { this.ticket_url = ticket_url; }

    public String getPoster_image() { return poster_image; }
    public void setPoster_image(String poster_image) { this.poster_image = poster_image; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean getIs_active() { return is_active; }
    public void setIs_active(boolean is_active) { this.is_active = is_active; }

    public int getDisplay_order() { return display_order; }
    public void setDisplay_order(int display_order) { this.display_order = display_order; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }
}
