package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import command.member.MemberCheckId;
import command.member.MemberLogin;
import command.member.MemberLogout;
import command.member.MemberSave;
import common.CommonExecute;
import common.CommonUtil;
import dao.MemberDao;
import dto.MemberDto;

/**
 * Servlet implementation class Member
 */
@WebServlet("/Member")
public class Member extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Member() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String gubun = request.getParameter("t_gubun");
		if(gubun == null)gubun = "login";
		String viewPage = "";
		//로그인
		if(gubun.equals("login")) {
			viewPage = "member/login.jsp";
		//회원가입 폼
		} else if (gubun.equals("join")) {
			viewPage = "member/join.jsp";
		//회원가입 들어가기
		} else if (gubun.equals("joinsave")) {
			CommonExecute mem = new MemberSave();
			mem.execute(request);
			viewPage = "common_alert.jsp";
		//로그인
		} else if(gubun.equals("memberLogin")) {
		    CommonExecute mem = new MemberLogin();
		    mem.execute(request);
		    
		    // AJAX 응답
		    String msg = (String)request.getAttribute("t_msg");
		    response.setContentType("text/html; charset=utf-8");
		    PrintWriter out = response.getWriter();
		    out.print(msg);  
		    return;  // forward 하지 않음
		//로그아웃
		} else if(gubun.equals("logout")) {
			CommonExecute mem = new MemberLogout();
			mem.execute(request);
			viewPage = "common_alert.jsp";
		// 내정보 폼
		}
		RequestDispatcher rd = request.getRequestDispatcher(viewPage);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
