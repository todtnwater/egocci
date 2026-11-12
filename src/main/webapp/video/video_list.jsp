<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.VideoDto" %>
<%
    ArrayList<VideoDto> mvList   = (ArrayList<VideoDto>)request.getAttribute("mvList");
    ArrayList<VideoDto> liveList = (ArrayList<VideoDto>)request.getAttribute("liveList");
    ArrayList<VideoDto> etcList  = (ArrayList<VideoDto>)request.getAttribute("etcList");
    VideoDto mainVideo = (VideoDto)request.getAttribute("mainVideo");
    int mainVideoId = (mainVideo != null) ? mainVideo.getVideo_id() : -1;
    String level = (String)session.getAttribute("sessionLevel");
    boolean isAdmin = "top".equals(level);
    int INITIAL_SHOW = 9;
    int mvCount   = (mvList   != null) ? mvList.size()   : 0;
    int liveCount = (liveList != null) ? liveList.size() : 0;
    int etcCount  = (etcList  != null) ? etcList.size()  : 0;
%>

<%@ include file="../header.jsp" %>

<form name="videoForm">
    <input type="hidden" name="t_gubun">
    <input type="hidden" name="t_no">
</form>

<style>
.video-card.hidden { display: none; }
.load-more-btn {
    display: block;
    margin: 24px auto 0;
    padding: 10px 32px;
    background: rgba(255,255,255,0.15);
    color: #fff;
    border: 1px solid rgba(255,255,255,0.3);
    border-radius: 24px;
    font-size: 14px;
    cursor: pointer;
    transition: background 0.2s;
}
.load-more-btn:hover { background: rgba(255,255,255,0.25); }
</style>

<script>
function goVideoWrite() {
    videoForm.t_gubun.value = "writeForm";
    videoForm.method = "post";
    videoForm.action = "Video";
    videoForm.submit();
}
function goVideoUpdate(videoId) {
    videoForm.t_gubun.value = "updateForm";
    videoForm.t_no.value = videoId;
    videoForm.method = "post";
    videoForm.action = "Video";
    videoForm.submit();
}
function goVideoDelete(videoId) {
    if (!confirm("정말 삭제하시겠습니까?")) return;
    videoForm.t_gubun.value = "delete";
    videoForm.t_no.value = videoId;
    videoForm.method = "post";
    videoForm.action = "Video";
    videoForm.submit();
}
function goVideoSetMain(videoId) {
    if (!confirm("이 영상을 메인으로 설정하시겠습니까?")) return;
    videoForm.t_gubun.value = "setMain";
    videoForm.t_no.value = videoId;
    videoForm.method = "post";
    videoForm.action = "Video";
    videoForm.submit();
}

function switchTab(tabName) {
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(c => c.style.display = 'none');
    document.getElementById('tab-' + tabName).style.display = 'block';
    event.currentTarget.classList.add('active');
}

function showMore(tabName) {
    document.querySelectorAll('#tab-' + tabName + ' .video-card.hidden')
            .forEach(el => el.classList.remove('hidden'));
    document.getElementById('btn-more-' + tabName).style.display = 'none';
}

function openVideoModal(embedUrl, title) {
    document.getElementById('modal-title').textContent = title;
    document.getElementById('modal-iframe').src = embedUrl + '?autoplay=1';
    document.getElementById('video-modal').style.display = 'flex';
    document.body.style.overflow = 'hidden';
}
function closeVideoModal() {
    document.getElementById('modal-iframe').src = '';
    document.getElementById('video-modal').style.display = 'none';
    document.body.style.overflow = '';
}
</script>

<div class="content-row">
    <main class="main-content">
        <div class="content-title">
            <h2>Video</h2>
        </div>

        <%if(isAdmin) {%>
        <div class="admin-menu" style="margin-bottom:20px;">
            <a href="javascript:goVideoWrite()" class="btn-submit">영상 등록</a>
        </div>
        <%}%>

        <div class="video-tabs">
            <button class="tab-btn active" onclick="switchTab('mv')">MV</button>
            <button class="tab-btn" onclick="switchTab('live')">Live</button>
            <button class="tab-btn" onclick="switchTab('etc')">기타</button>
        </div>

        <!-- MV 탭 -->
        <div id="tab-mv" class="tab-content">
            <%if(mvList != null && !mvList.isEmpty()) {%>
            <div class="video-grid">
                <%
                int mvIdx = 0;
                for(VideoDto dto : mvList) {
                    String thumb = (dto.getThumbnail_url() != null && !dto.getThumbnail_url().isEmpty())
                                   ? dto.getThumbnail_url() : dto.getAutoThumbnail();
                    boolean isMain = (dto.getVideo_id() == mainVideoId);
                    boolean hidden = (mvIdx >= INITIAL_SHOW);
                    mvIdx++;
                %>
                <div class="video-card<%=hidden ? " hidden" : ""%>">
                    <div class="video-thumb" onclick="openVideoModal('<%=dto.getEmbedUrl()%>', '<%=dto.getTitle().replace("'", "\\'")%>')">
                        <img src="<%=thumb%>" alt="<%=dto.getTitle()%>" onerror="this.src='<%=request.getContextPath()%>/image/song/default-song.jpg'">
                        <div class="play-overlay">
                            <span class="material-icons play-icon">play_circle</span>
                        </div>
                        <%if(isMain) {%>
                        <div style="position:absolute;top:6px;left:6px;background:#e53e3e;color:#fff;font-size:11px;padding:2px 8px;border-radius:4px;font-weight:bold;">🏠 메인</div>
                        <%}%>
                    </div>
                    <div class="video-info">
                        <p class="video-title"><%=dto.getTitle()%></p>
                        <%if(dto.getDescription() != null && !dto.getDescription().isEmpty()) {%>
                        <p class="video-desc"><%=dto.getDescription()%></p>
                        <%}%>
                        <%if(isAdmin) {%>
                        <div class="video-admin-btns">
                            <%if(!isMain) {%>
                            <button class="btn-submit" onclick="goVideoSetMain(<%=dto.getVideo_id()%>)">메인 설정</button>
                            <%}%>
                            <button class="btn-warning" onclick="goVideoUpdate(<%=dto.getVideo_id()%>)">수정</button>
                            <button class="btn-danger" onclick="goVideoDelete(<%=dto.getVideo_id()%>)">삭제</button>
                        </div>
                        <%}%>
                    </div>
                </div>
                <%}%>
            </div>
            <%if(mvCount > INITIAL_SHOW) {%>
            <button id="btn-more-mv" class="load-more-btn" onclick="showMore('mv')">
                더 보기 (<%=mvCount - INITIAL_SHOW%>개 더)
            </button>
            <%}%>
            <%} else {%>
            <p class="no-content">등록된 MV가 없습니다.</p>
            <%}%>
        </div>

        <!-- Live 탭 -->
        <div id="tab-live" class="tab-content" style="display:none;">
            <%if(liveList != null && !liveList.isEmpty()) {%>
            <div class="video-grid">
                <%
                int liveIdx = 0;
                for(VideoDto dto : liveList) {
                    String thumb = (dto.getThumbnail_url() != null && !dto.getThumbnail_url().isEmpty())
                                   ? dto.getThumbnail_url() : dto.getAutoThumbnail();
                    boolean isMain = (dto.getVideo_id() == mainVideoId);
                    boolean hidden = (liveIdx >= INITIAL_SHOW);
                    liveIdx++;
                %>
                <div class="video-card<%=hidden ? " hidden" : ""%>">
                    <div class="video-thumb" onclick="openVideoModal('<%=dto.getEmbedUrl()%>', '<%=dto.getTitle().replace("'", "\\'")%>')">
                        <img src="<%=thumb%>" alt="<%=dto.getTitle()%>" onerror="this.src='<%=request.getContextPath()%>/image/song/default-song.jpg'">
                        <div class="play-overlay">
                            <span class="material-icons play-icon">play_circle</span>
                        </div>
                        <%if(isMain) {%>
                        <div style="position:absolute;top:6px;left:6px;background:#e53e3e;color:#fff;font-size:11px;padding:2px 8px;border-radius:4px;font-weight:bold;">🏠 메인</div>
                        <%}%>
                    </div>
                    <div class="video-info">
                        <p class="video-title"><%=dto.getTitle()%></p>
                        <%if(dto.getDescription() != null && !dto.getDescription().isEmpty()) {%>
                        <p class="video-desc"><%=dto.getDescription()%></p>
                        <%}%>
                        <%if(isAdmin) {%>
                        <div class="video-admin-btns">
                            <%if(!isMain) {%>
                            <button class="btn-submit" onclick="goVideoSetMain(<%=dto.getVideo_id()%>)">메인 설정</button>
                            <%}%>
                            <button class="btn-warning" onclick="goVideoUpdate(<%=dto.getVideo_id()%>)">수정</button>
                            <button class="btn-danger" onclick="goVideoDelete(<%=dto.getVideo_id()%>)">삭제</button>
                        </div>
                        <%}%>
                    </div>
                </div>
                <%}%>
            </div>
            <%if(liveCount > INITIAL_SHOW) {%>
            <button id="btn-more-live" class="load-more-btn" onclick="showMore('live')">
                더 보기 (<%=liveCount - INITIAL_SHOW%>개 더)
            </button>
            <%}%>
            <%} else {%>
            <p class="no-content">등록된 라이브 영상이 없습니다.</p>
            <%}%>
        </div>

        <!-- 기타 탭 -->
        <div id="tab-etc" class="tab-content" style="display:none;">
            <%if(etcList != null && !etcList.isEmpty()) {%>
            <div class="video-grid">
                <%
                int etcIdx = 0;
                for(VideoDto dto : etcList) {
                    String thumb = (dto.getThumbnail_url() != null && !dto.getThumbnail_url().isEmpty())
                                   ? dto.getThumbnail_url() : dto.getAutoThumbnail();
                    boolean isMain = (dto.getVideo_id() == mainVideoId);
                    boolean hidden = (etcIdx >= INITIAL_SHOW);
                    etcIdx++;
                %>
                <div class="video-card<%=hidden ? " hidden" : ""%>">
                    <div class="video-thumb" onclick="openVideoModal('<%=dto.getEmbedUrl()%>', '<%=dto.getTitle().replace("'", "\\'")%>')">
                        <img src="<%=thumb%>" alt="<%=dto.getTitle()%>" onerror="this.src='<%=request.getContextPath()%>/image/song/default-song.jpg'">
                        <div class="play-overlay">
                            <span class="material-icons play-icon">play_circle</span>
                        </div>
                        <%if(isMain) {%>
                        <div style="position:absolute;top:6px;left:6px;background:#e53e3e;color:#fff;font-size:11px;padding:2px 8px;border-radius:4px;font-weight:bold;">🏠 메인</div>
                        <%}%>
                    </div>
                    <div class="video-info">
                        <p class="video-title"><%=dto.getTitle()%></p>
                        <%if(dto.getDescription() != null && !dto.getDescription().isEmpty()) {%>
                        <p class="video-desc"><%=dto.getDescription()%></p>
                        <%}%>
                        <%if(isAdmin) {%>
                        <div class="video-admin-btns">
                            <%if(!isMain) {%>
                            <button class="btn-submit" onclick="goVideoSetMain(<%=dto.getVideo_id()%>)">메인 설정</button>
                            <%}%>
                            <button class="btn-warning" onclick="goVideoUpdate(<%=dto.getVideo_id()%>)">수정</button>
                            <button class="btn-danger" onclick="goVideoDelete(<%=dto.getVideo_id()%>)">삭제</button>
                        </div>
                        <%}%>
                    </div>
                </div>
                <%}%>
            </div>
            <%if(etcCount > INITIAL_SHOW) {%>
            <button id="btn-more-etc" class="load-more-btn" onclick="showMore('etc')">
                더 보기 (<%=etcCount - INITIAL_SHOW%>개 더)
            </button>
            <%}%>
            <%} else {%>
            <p class="no-content">등록된 기타 영상이 없습니다.</p>
            <%}%>
        </div>

    </main>
</div>

<!-- 유튜브 재생 모달 -->
<div id="video-modal" style="display:none;position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.85);z-index:9999;align-items:center;justify-content:center;flex-direction:column;">
    <div style="position:relative;width:90%;max-width:900px;">
        <button onclick="closeVideoModal()" style="position:absolute;top:-40px;right:0;background:none;border:none;color:#fff;font-size:28px;cursor:pointer;">✕</button>
        <p id="modal-title" style="color:#fff;text-align:center;margin-bottom:10px;font-size:16px;"></p>
        <div style="position:relative;padding-bottom:56.25%;height:0;overflow:hidden;">
            <iframe id="modal-iframe" src="" frameborder="0" allowfullscreen
                style="position:absolute;top:0;left:0;width:100%;height:100%;border-radius:8px;"></iframe>
        </div>
    </div>
</div>

<%@ include file="../footer.jsp" %>
