package dto;

public class SongDto {
    // 기본 곡 정보
    private int song_id;
    private int album_id;
    private String song_title;
    private String artist_name;
    private String genre;
    private String lyrics;
    private String behind_note;
    private String song_cover_image;
    private int track_number;
    private String duration;  // TIME 타입은 String으로 받아서 처리
    private boolean is_title_track;
    private int display_order;

    // 앨범 관련 정보
    private String album_title;
    private String album_type;
    private String album_number;
    private String album_cover_image;
    private String album_description;
    private String release_date;
    private int total_tracks;

    // 시스템 정보
    private String created_at;
    private String updated_at;

    public SongDto() {}

    // Getter/Setter
    public int getSong_id() { return song_id; }
    public void setSong_id(int song_id) { this.song_id = song_id; }

    public int getAlbum_id() { return album_id; }
    public void setAlbum_id(int album_id) { this.album_id = album_id; }

    public String getSong_title() { return song_title; }
    public void setSong_title(String song_title) { this.song_title = song_title; }

    public String getArtist_name() { return artist_name; }
    public void setArtist_name(String artist_name) { this.artist_name = artist_name; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getLyrics() { return lyrics; }
    public void setLyrics(String lyrics) { this.lyrics = lyrics; }

    public String getBehind_note() { return behind_note; }
    public void setBehind_note(String behind_note) { this.behind_note = behind_note; }

    public String getSong_cover_image() { return song_cover_image; }
    public void setSong_cover_image(String song_cover_image) { this.song_cover_image = song_cover_image; }

    public int getTrack_number() { return track_number; }
    public void setTrack_number(int track_number) { this.track_number = track_number; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public boolean getIs_title_track() { return is_title_track; }
    public void setIs_title_track(boolean is_title_track) { this.is_title_track = is_title_track; }

    public int getDisplay_order() { return display_order; }
    public void setDisplay_order(int display_order) { this.display_order = display_order; }

    public String getAlbum_title() { return album_title; }
    public void setAlbum_title(String album_title) { this.album_title = album_title; }

    public String getAlbum_type() { return album_type; }
    public void setAlbum_type(String album_type) { this.album_type = album_type; }

    public String getAlbum_number() { return album_number; }
    public void setAlbum_number(String album_number) { this.album_number = album_number; }

    public String getAlbum_cover_image() { return album_cover_image; }
    public void setAlbum_cover_image(String album_cover_image) { this.album_cover_image = album_cover_image; }

    public String getAlbum_description() { return album_description; }
    public void setAlbum_description(String album_description) { this.album_description = album_description; }

    public String getRelease_date() { return release_date; }
    public void setRelease_date(String release_date) { this.release_date = release_date; }

    public int getTotal_tracks() { return total_tracks; }
    public void setTotal_tracks(int total_tracks) { this.total_tracks = total_tracks; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }
}