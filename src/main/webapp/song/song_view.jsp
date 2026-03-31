<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.SongDto" %>
<%@ page import="java.util.ArrayList" %>
<%
    SongDto dto = (SongDto) request.getAttribute("t_dto");
    ArrayList<String[]> streamingLinks = (ArrayList<String[]>) request.getAttribute("streamingLinks");

    String level = (String)session.getAttribute("sessionLevel");
    boolean isAdmin = "top".equals(level);
%>

<%@ include file="../header.jsp" %>

<script>
function goToList() {
    location.href = "Song?t_gubun=list";
}

function goToUpdate(songId) {
    if (!songId || isNaN(songId)) {
        alert("유효하지 않은 곡 ID입니다.");
        return;
    }

    var form = document.createElement("form");
    form.method = "POST";
    form.action = "Song";

    var gubun = document.createElement("input");
    gubun.type = "hidden";
    gubun.name = "t_gubun";
    gubun.value = "updateForm";
    form.appendChild(gubun);

    var no = document.createElement("input");
    no.type = "hidden";
    no.name = "t_no";
    no.value = songId;
    form.appendChild(no);

    document.body.appendChild(form);
    form.submit();
}

function deleteSong(songId) {
    if (!songId || isNaN(songId)) {
        alert("유효하지 않은 곡 ID입니다.");
        return;
    }

    if (!confirm("정말 이 곡을 삭제하시겠습니까?")) {
        return;
    }

    var form = document.createElement("form");
    form.method = "POST";
    form.action = "Song";

    var gubun = document.createElement("input");
    gubun.type = "hidden";
    gubun.name = "t_gubun";
    gubun.value = "delete";
    form.appendChild(gubun);

    var no = document.createElement("input");
    no.type = "hidden";
    no.name = "t_no";
    no.value = songId;
    form.appendChild(no);

    document.body.appendChild(form);
    form.submit();
}
</script>

<style>
.song-view-container {
    max-width: 1100px;
    margin: 0 auto;
    padding: 30px 20px 60px;
}

.song-view-card {
    background: rgba(255,255,255,0.04);
    border: 1px solid rgba(255,255,255,0.08);
    border-radius: 18px;
    padding: 30px;
    box-shadow: 0 8px 30px rgba(0,0,0,0.15);
}

.song-header {
    display: flex;
    gap: 30px;
    align-items: flex-start;
    margin-bottom: 35px;
    flex-wrap: wrap;
}

.song-cover {
    width: 320px;
    max-width: 100%;
    aspect-ratio: 1 / 1;
    border-radius: 16px;
    overflow: hidden;
    background: rgba(255,255,255,0.05);
    border: 1px solid rgba(255,255,255,0.08);
    display: flex;
    align-items: center;
    justify-content: center;
}

.song-cover img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.song-cover-placeholder {
    color: #aaa;
    font-size: 14px;
}

.song-info {
    flex: 1;
    min-width: 280px;
}

.song-title {
    margin: 0 0 10px 0;
    font-size: 32px;
    font-weight: 700;
    line-height: 1.3;
}

.song-artist {
    font-size: 18px;
    color: #ccc;
    margin-bottom: 22px;
}

.song-meta {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
    gap: 12px;
    margin-bottom: 22px;
}

.meta-item {
    background: rgba(255,255,255,0.04);
    border-radius: 12px;
    padding: 12px 14px;
    border: 1px solid rgba(255,255,255,0.06);
}

.meta-label {
    display: block;
    font-size: 12px;
    color: #999;
    margin-bottom: 6px;
}

.meta-value {
    font-size: 15px;
    color: #f5f5f5;
    word-break: break-word;
}

.song-actions {
    display: flex;
    gap: 10px;
    flex-wrap: wrap;
    margin-top: 20px;
}

.song-actions button {
    border: none;
    border-radius: 10px;
    padding: 11px 18px;
    cursor: pointer;
    font-size: 14px;
}

.btn-list {
    background: #444;
    color: #fff;
}

.btn-edit {
    background: #1f7aec;
    color: #fff;
}

.btn-delete {
    background: #d64545;
    color: #fff;
}

.section-block {
    margin-top: 28px;
}

.section-block h3 {
    margin-bottom: 14px;
    font-size: 20px;
}

.section-content {
    background: rgba(255,255,255,0.03);
    border: 1px solid rgba(255,255,255,0.06);
    border-radius: 14px;
    padding: 18px;
}

.section-content pre {
    margin: 0;
    white-space: pre-wrap;
    word-break: break-word;
    line-height: 1.7;
    font-family: inherit;
}

.empty-text {
    color: #999;
    margin: 0;
}

.streaming-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.streaming-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 14px;
    padding: 12px 14px;
    border-radius: 10px;
    background: rgba(255,255,255,0.04);
    border: 1px solid rgba(255,255,255,0.06);
    flex-wrap: wrap;
}

.streaming-platform {
    font-weight: 600;
}

.streaming-links {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
}

.streaming-link {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 44px;
    height: 44px;
    background: rgba(255,255,255,0.2);
    border-radius: 50%;
    text-decoration: none;
    color: white;
    font-size: 13px;
    transition: all 0.3s ease;
}

.streaming-link img {
    width: 26px;
    height: 26px;
    border-radius: 50%;
    object-fit: contain;
}

.streaming-link:hover {
    background: rgba(255,255,255,0.35);
    transform: scale(1.1);
    text-decoration: none;
}

.not-found-box {
    max-width: 700px;
    margin: 80px auto;
    text-align: center;
    padding: 40px 20px;
    background: rgba(255,255,255,0.04);
    border-radius: 18px;
    border: 1px solid rgba(255,255,255,0.08);
}

@media (max-width: 768px) {
    .song-view-card {
        padding: 20px;
    }

    .song-title {
        font-size: 26px;
    }

    .song-header {
        gap: 20px;
    }

    .song-cover {
        width: 100%;
    }
}
</style>

<div class="song-view-container">
<% if (dto != null) { %>
    <div class="song-view-card">
        <div class="song-header">
            <div class="song-cover">
                <% if (dto.getSong_cover_image() != null && !dto.getSong_cover_image().trim().isEmpty()) { %>
                    <img src="<%=request.getContextPath()%>/image/song/<%=dto.getSong_cover_image()%>" alt="<%=dto.getSong_title()%> 커버 이미지">
                <% } else { %>
                    <div class="song-cover-placeholder">등록된 커버 이미지가 없습니다.</div>
                <% } %>
            </div>

            <div class="song-info">
                <h2 class="song-title"><%= dto.getSong_title() != null ? dto.getSong_title() : "-" %></h2>
                <div class="song-artist"><%= dto.getArtist_name() != null ? dto.getArtist_name() : "-" %></div>

                <div class="song-meta">
                    <div class="meta-item">
                        <span class="meta-label">앨범명</span>
                        <span class="meta-value"><%= dto.getAlbum_title() != null ? dto.getAlbum_title() : "-" %></span>
                    </div>

                    <div class="meta-item">
                        <span class="meta-label">앨범 타입</span>
                        <span class="meta-value"><%= dto.getAlbum_type() != null ? dto.getAlbum_type() : "-" %></span>
                    </div>
   
                    <div class="meta-item">
                        <span class="meta-label">장르</span>
                        <span class="meta-value"><%= dto.getGenre() != null ? dto.getGenre() : "-" %></span>
                    </div>

                    <div class="meta-item">
                        <span class="meta-label">재생 시간</span>
                        <span class="meta-value"><%= dto.getDuration() != null ? dto.getDuration() : "-" %></span>
                    </div>

                    <div class="meta-item">
                        <span class="meta-label">앨범 발매일</span>
                        <span class="meta-value"><%= dto.getRelease_date() != null && !dto.getRelease_date().trim().isEmpty() ? dto.getRelease_date() : "-" %></span>
                    </div>
                    <div class="meta-item">
                        <span class="meta-value"><%= dto.getIs_title_track() ? "타이틀곡" : "수록곡" %></span>
                    </div>
                </div>

                <%if(dto.getAlbum_description() != null && !dto.getAlbum_description().trim().isEmpty()) {%>
                <div style="margin-top:14px;background:rgba(255,255,255,0.04);border-radius:12px;padding:14px 16px;border:1px solid rgba(255,255,255,0.06);">
                    <span style="display:block;font-size:12px;color:#999;margin-bottom:6px;">앨범 설명</span>
                    <span style="font-size:14px;color:#f0f0f0;line-height:1.7;white-space:pre-wrap;"><%=dto.getAlbum_description()%></span>
                </div>
                <%}%>

                <div class="song-actions">
                    <button type="button" class="btn-list" onclick="goToList()">목록으로</button>

                    <% if (isAdmin) { %>
                        <button type="button" class="btn-edit" onclick="goToUpdate(<%=dto.getSong_id()%>)">수정</button>
                        <button type="button" class="btn-delete" onclick="deleteSong(<%=dto.getSong_id()%>)">삭제</button>
                    <% } %>
                </div>
            </div>
        </div>

        <div class="section-block">
            <h3>비하인드</h3>
            <div class="section-content">
                <% if (dto.getBehind_note() != null && !dto.getBehind_note().trim().isEmpty()) { %>
                    <pre><%= dto.getBehind_note().replaceAll("(?i)<br\\s*/?>", "\n") %></pre>
                <% } else { %>
                    <p class="empty-text">비하인드 정보가 없습니다.</p>
                <% } %>
            </div>
        </div>

        <div class="section-block">
            <h3>가사</h3>
            <div class="section-content">
                <% if (dto.getLyrics() != null && !dto.getLyrics().trim().isEmpty()) { %>
                    <pre><%= dto.getLyrics().replaceAll("(?i)<br\\s*/?>", "\n") %></pre>
                <% } else { %>
                    <p class="empty-text">가사 정보가 없습니다.</p>
                <% } %>
            </div>
        </div>

        <div class="section-block">
            <h3>스트리밍 링크</h3>
            <div class="section-content">
                <% if (streamingLinks != null && !streamingLinks.isEmpty()) { %>
                    <div class="streaming-links">
                        <% for (String[] link : streamingLinks) {
                            if (link == null || link.length < 2) continue;
                            String iconPath = "";
                            if (link[0].contains("Spotify"))         iconPath = "Spotify.png";
                            else if (link[0].contains("Melon"))      iconPath = "Melon.png";
                            else if (link[0].contains("Apple"))      iconPath = "Applemusic.png";
                            else if (link[0].contains("YouTube"))    iconPath = "Youtube.png";
                            else if (link[0].contains("SoundCloud")) iconPath = "Soundcloud.png";
                            else if (link[0].contains("Bugs"))       iconPath = "Bugs.png";
                            else if (link[0].contains("Genie"))      iconPath = "Ginie.png";
                            else if (link[0].contains("TikTok"))     iconPath = "Tiktok.png";
                        %>
                        <a href="<%=link[1]%>" target="_blank" class="streaming-link" title="<%=link[0]%>">
                            <%if(!iconPath.equals("")) {%>
                            <img src="<%=request.getContextPath()%>/image/song/<%=iconPath%>" alt="<%=link[0]%>">
                            <%} else {%>
                            <%=link[0]%>
                            <%}%>
                        </a>
                        <% } %>
                    </div>
                <% } else { %>
                    <p class="empty-text">등록된 스트리밍 링크가 없습니다.</p>
                <% } %>
            </div>
        </div>
    </div>
<% } else { %>
    <div class="not-found-box">
        <h2>곡 정보를 찾을 수 없습니다.</h2>
        <p style="color:#999; margin:12px 0 24px;">삭제되었거나 잘못된 접근일 수 있습니다.</p>
        <button type="button" class="btn-list" onclick="goToList()">목록으로</button>
    </div>
<% } %>
</div>

<%@ include file="../footer.jsp" %>