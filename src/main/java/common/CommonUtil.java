package common;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CommonUtil {
    // 공지사항 첨부파일 - HttpServletRequest 파라미터 추가
    public static String getNoticeDir(HttpServletRequest request) {
        String dir = request.getSession().getServletContext().getRealPath("/") + "attach/notice/";
        return dir;
    }
    
    // 제품이미지 첨부파일 - HttpServletRequest 파라미터 추가
    public static String getProductDir(HttpServletRequest request) {
        String dir = request.getSession().getServletContext().getRealPath("/") + "attach/product/";
        return dir;
    }
	
	// 자료실 첨부파일
	public static String getPdsDir() {
		return "C:/Users/1026w/Desktop/JSL/track_23_work/kss_project/Java/hompage_jsp_jsl_김성수/src/main/webapp/attach/pds/";
	}
	
	// "'" 변환 &#39;
	public static String setQuote(String str) {
		str = str.replaceAll("'", "&#39;");
		return str;
	}
	
	// " " " 변환 &quot;
		public static String getQuote(String str) {
			str = str.replaceAll("\"", "&quot;");
			return str;
		}
	
	//리스트 페이지
	// 페이지 설정
	public static String getPageDisplay(int current_page,int total_page, int pageNumber_count){
		int pagenumber;    //화면에 보여질 페이지 인덱스수
		int startpage;     //화면에 보여질 시작 페이지 번호
		int endpage;       //화면에 보여질 마지막 페이지 번호
		int curpage;       //이동하고자 하는 페이지 번호
		
		String strList=""; //리턴될 페이지 인덱스 리스트

		pagenumber = pageNumber_count;   //한 화면의 페이지 인덱스수
		
		//시작 페이지 번호 구하기
		startpage = ((current_page - 1)/ pagenumber) * pagenumber + 1;
		//마지막 페이지 번호 구하기
		endpage = (((startpage -1) + pagenumber) / pagenumber)*pagenumber;
		//총페이지수가 계산된 마지막 페이지 번호보다 작을 경우
		//총페이지수가 마지막 페이지 번호가 됨
		
		if(total_page <= endpage)  endpage = total_page;
					
		//첫번째 페이지 인덱스 화면이 아닌경우
		if(current_page > pagenumber){
			curpage = startpage -1;  //시작페이지 번호보다 1적은 페이지로 이동
			strList = strList +"<a href=javascript:goPageView('"+curpage+"') ><i class='fa fa-angle-double-left'></i></a>";
		}
						
		//시작페이지 번호부터 마지막 페이지 번호까지 화면에 표시
		curpage = startpage;
		while(curpage <= endpage){
			if(curpage == current_page){
				strList = strList +"<a class='active'>"+current_page+"</a>";
			} else {
				strList = strList +"<a href=javascript:goPageView('"+curpage+"')>"+curpage+"</a>";
			}
			curpage++;
		}
		//뒤에 페이지가 더 있는 경우
		if(total_page > endpage){
			curpage = endpage -1;
			strList = strList + "<a href=javascript:goPageView('"+curpage+"') ><i class='fa fa-angle-double-right'></i></a>";
		}
		return strList;
	}			
	
	// 오늘날짜  yyyy-MM-dd
	public static String getToday(){
		Date date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		
		String today = sd.format(date);
		return today;
	}
	
	// 오늘날짜 시분초 yyyy-MM-dd HH:mm:ss
	public static String getTodayTime(){
		Date date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String today = sd.format(date);
		return today;
	}

	public static String getSessionInfo(HttpServletRequest request, String gubun) {
	    String result = "";
	    HttpSession session = request.getSession();
	    
	    if(gubun.equals("id")) {
	        result = (String)session.getAttribute("sessionId");
	    } else if(gubun.equals("name")) {
	        result = (String)session.getAttribute("sessionName");
	    } else if(gubun.equals("level")) {
	        result = (String)session.getAttribute("sessionLevel");
	    }
	    if(result == null) result ="";
	    
	    return result;
	}

	
}




