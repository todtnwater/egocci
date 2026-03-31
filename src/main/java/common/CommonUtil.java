package common;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class CommonUtil {

	// SFTP 서버 정보
	private static final String SFTP_HOST = "XXX.XXX.XXX.XXX";
	private static final int SFTP_PORT = 22;
	private static final String SFTP_USER = "XX";
	private static final String SFTP_PASS = "XXXX";
	private static final String SFTP_ROOM_PATH      = "/home/ss/upload/room/";
	private static final String SFTP_NOTICE_PATH    = "/home/ss/upload/notice/";
	private static final String SFTP_SONG_PATH      = "/home/ss/upload/song/";
	private static final String SFTP_SCHEDULED_PATH = "/home/ss/upload/scheduled/";
	private static final String SFTP_GALLERY_PATH   = "/home/ss/upload/gallery/";

	// 오늘 날짜 [yyyy-MM-dd]
	public static String getToday() {
		Date date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		String today = sd.format(date);
		return today;
	}

	// 오늘 날짜 + 시간
	public static String getTodayTime() {
		Date date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String today = sd.format(date);
		return today;
	}

	// 세션 정보
	public static String getSessionInfo(HttpServletRequest request, String gubun) {
		String result = "";
		HttpSession session = request.getSession();

		if(gubun.equals("id")) {
			result = (String)session.getAttribute("sessionId");
		} else if(gubun.equals("name")) {
			result = (String)session.getAttribute("sessionName");
		} else if(gubun.equals("level")) {
			result = (String)session.getAttribute("sessionLevel");
		} else if(gubun.equals("position")) {
			result = (String)session.getAttribute("sessionPosition");
		} else if(gubun.equals("membership")) {
			result = (String)session.getAttribute("sessionMembership");
		}

		if(result == null) {
			result = "";
		}
		return result;
	}

	// 작은따옴표 html 문자표로 바꾸기 [DB로 저장 시 사용]
	public static String setSquot(String str) {
		return str.replaceAll("'", "&#39;");
	}

	// 작은따옴표 html 문자표로 바꾸기 [DB에서 불러올 때 사용]
	public static String getSquot(String str) {
		return str.replaceAll("&#39;", "'");
	}

	// 큰따옴표 html 문자표로 바꾸기 [DB에서 불러올 때 사용]
	public static String getDquot(String str) {
		return str.replaceAll("\"", "&quot;");
	}

	// 페이지 설정 [최진원]
	public static String getPageDisplay(int current_page, int total_page, int pageNumber_count) {
		String strList = "";
		int pagenumber;
		int startpage;
		int endpage;
		int curpage;

		startpage = current_page;
		pagenumber = pageNumber_count;
		endpage = startpage + pagenumber - 1;

		if(total_page > 0) {
			strList += "<a href=javascript:goPage('1')>◀◀</a>\r\n";

			if(startpage - 1 > 0) {
				curpage = startpage - 1;
				strList += "<a href=javascript:goPage('"+curpage+"')>◀</a>\r\n";
			}

			if(total_page < pagenumber) {
				endpage = total_page;
				for(int k=1; k<=endpage; k++) {
					if(startpage == k) {
						strList += "<a href = javascript:goPage('"+startpage+"') class='active'>"+startpage+"</a>\r\n";
					} else {
						strList += "<a href = javascript:goPage('"+k+"')>"+k+"</a>\r\n";
					}
				}
			} else {
				if(endpage >= total_page) {
					endpage = total_page;
					for(int k=endpage-pagenumber+1; k<=endpage; k++) {
						if(startpage == k) {
							strList += "<a href = javascript:goPage('"+startpage+"') class='active'>"+startpage+"</a>\r\n";
						} else {
							strList += "<a href = javascript:goPage('"+k+"')>"+k+"</a>\r\n";
						}
					}
				} else {
					if(endpage < total_page) {
						for(int k = startpage; k <= endpage; k++) {
							if(startpage == k) {
								strList += "<a href = javascript:goPage('"+startpage+"') class='active'>"+startpage+"</a>\r\n";
							} else {
								strList += "<a href = javascript:goPage('"+k+"')>"+k+"</a>\r\n";
							}
						}
					}
				}
			}

			if(startpage + 1 <= total_page) {
				curpage = startpage + 1;
				strList += "<a href=javascript:goPage('"+curpage+"')>▶</a>\r\n";
			}

			strList += "<a href=javascript:goPage('"+total_page+"')>▶▶</a>\r\n";
		}

		return strList;
	}

	// 대문자로
	public static String toUpper(String obj) {
		String ori = obj;
		String upper = ori.toUpperCase();
		return upper;
	}

	// Notice 설정 [이건희]
	public static String setQuote(String str) {
        if (str == null) {
			return "";
		}
        String result = str.replaceAll("'", "&#39;");
        result = result.replaceAll("\"", "&quot;");
        result = result.replaceAll("\n", "<br>");
        return result;
	}

    // 공지사항 첨부파일 임시 저장 경로 (로컬 - SFTP 업로드 전)
	public static String getNoticeTempDir(HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
        String attachDir = rootPath + "temp/notice/";
        File file = new File(attachDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return attachDir;
	}

	// 공지사항 첨부파일 저장 경로 (기존 로컬 저장 - 다운로드용으로만 사용)
	public static String getNoticeDir(HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
        String attachDir = rootPath + "attach/notice/";
        File file = new File(attachDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return attachDir;
	}

	// 객실 이미지 임시 저장 경로 (로컬)
	public static String getRoomImageTempDir(HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String tempDir = rootPath + "temp/room/";
		File file = new File(tempDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return tempDir;
	}

	// SFTP로 파일 업로드 (객실용)
	public static boolean uploadRoomToSFTP(File localFile, String remoteFileName) {
		return uploadToSFTP(localFile, remoteFileName, SFTP_ROOM_PATH);
	}

	// SFTP로 파일 업로드 (공지사항용)
	public static boolean uploadNoticeToSFTP(File localFile, String remoteFileName) {
		return uploadToSFTP(localFile, remoteFileName, SFTP_NOTICE_PATH);
	}

	// SFTP로 파일 업로드 (곡 커버 이미지용)
	public static boolean uploadSongToSFTP(File localFile, String remoteFileName) {
		return uploadToSFTP(localFile, remoteFileName, SFTP_SONG_PATH);
	}

	// SFTP로 파일 업로드 (공연 포스터용)
	public static boolean uploadScheduledToSFTP(File localFile, String remoteFileName) {
		return uploadToSFTP(localFile, remoteFileName, SFTP_SCHEDULED_PATH);
	}

	// SFTP로 파일 업로드 (갤러리용)
	public static boolean uploadGalleryToSFTP(File localFile, String remoteFileName) {
		return uploadToSFTP(localFile, remoteFileName, SFTP_GALLERY_PATH);
	}

	// SFTP로 파일 업로드 (공통)
	private static boolean uploadToSFTP(File localFile, String remoteFileName, String remotePath) {
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		try {
			JSch jsch = new JSch();
			session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
			session.setPassword(SFTP_PASS);

			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;

			// 원격 디렉토리 생성 (없으면)
			try {
				channelSftp.cd(remotePath);
			} catch (Exception e) {
				// 디렉토리가 없으면 생성
				String[] dirs = remotePath.split("/");
				String path = "";
				for (String dir : dirs) {
					if (dir.length() > 0) {
						path += "/" + dir;
						try {
							channelSftp.cd(path);
						} catch (Exception ex) {
							channelSftp.mkdir(path);
							channelSftp.cd(path);
						}
					}
				}
			}

			// 파일 업로드
			FileInputStream fis = new FileInputStream(localFile);
			channelSftp.put(fis, remoteFileName);
			fis.close();

			System.out.println(">>> SFTP 업로드 성공: " + remoteFileName + " -> " + remotePath);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(">>> SFTP 업로드 실패: " + e.getMessage());
			return false;
		} finally {
			if (channelSftp != null) {
				channelSftp.disconnect();
			}
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
		}
	}

	// SFTP에서 파일 삭제 (공지사항용)
	public static boolean deleteNoticeFromSFTP(String remoteFileName) {
		return deleteFromSFTP(remoteFileName, SFTP_NOTICE_PATH);
	}

	// SFTP에서 파일 삭제 (객실용)
	public static boolean deleteRoomFromSFTP(String remoteFileName) {
		return deleteFromSFTP(remoteFileName, SFTP_ROOM_PATH);
	}

	// SFTP에서 파일 삭제 (곡 커버 이미지용)
	public static boolean deleteSongFromSFTP(String remoteFileName) {
		return deleteFromSFTP(remoteFileName, SFTP_SONG_PATH);
	}

	// SFTP에서 파일 삭제 (공연 포스터용)
	public static boolean deleteScheduledFromSFTP(String remoteFileName) {
		return deleteFromSFTP(remoteFileName, SFTP_SCHEDULED_PATH);
	}

	// SFTP에서 파일 삭제 (갤러리용)
	public static boolean deleteGalleryFromSFTP(String remoteFileName) {
		return deleteFromSFTP(remoteFileName, SFTP_GALLERY_PATH);
	}

	// SFTP에서 파일 삭제 (공통)
	private static boolean deleteFromSFTP(String remoteFileName, String remotePath) {
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		try {
			JSch jsch = new JSch();
			session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
			session.setPassword(SFTP_PASS);

			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;

			// 파일 삭제
			channelSftp.cd(remotePath);
			channelSftp.rm(remoteFileName);

			System.out.println(">>> SFTP 삭제 성공: " + remoteFileName + " from " + remotePath);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(">>> SFTP 삭제 실패: " + e.getMessage());
			return false;
		} finally {
			if (channelSftp != null) {
				channelSftp.disconnect();
			}
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
		}
	}

	// SFTP에서 파일 다운로드 (공지사항용)
	public static boolean downloadNoticeFromSFTP(String remoteFileName, String localPath) {
		return downloadFromSFTP(remoteFileName, localPath, SFTP_NOTICE_PATH);
	}

	// SFTP에서 파일 다운로드 (객실용)
	public static boolean downloadRoomFromSFTP(String remoteFileName, String localPath) {
		return downloadFromSFTP(remoteFileName, localPath, SFTP_ROOM_PATH);
	}

	// SFTP에서 파일 다운로드 (공통)
	private static boolean downloadFromSFTP(String remoteFileName, String localPath, String remotePath) {
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		try {
			JSch jsch = new JSch();
			session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
			session.setPassword(SFTP_PASS);

			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;

			// 파일 다운로드
			channelSftp.cd(remotePath);
			channelSftp.get(remoteFileName, localPath);

			System.out.println(">>> SFTP 다운로드 성공: " + remoteFileName + " -> " + localPath);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(">>> SFTP 다운로드 실패: " + e.getMessage());
			return false;
		} finally {
			if (channelSftp != null) {
				channelSftp.disconnect();
			}
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
		}
	}
}
