package controller;

import java.io.IOException;
import java.io.PrintWriter;

import command.member.MemberLogin;
import command.member.MemberLogout;
import command.member.MemberSave;
import common.CommonExecute;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Member")
public class Member extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Member() {
        super();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");

		String gubun = request.getParameter("t_gubun");
		if(gubun == null) {
			gubun = "login";
		}
		String viewPage = "";
		if(gubun.equals("login")) {
			viewPage = "member/login.jsp";
		} else if (gubun.equals("join")) {
			viewPage = "member/join.jsp";
		} else if (gubun.equals("joinsave")) {
			CommonExecute mem = new MemberSave();
			mem.execute(request);
			viewPage = "common_alert.jsp";
		} else if(gubun.equals("memberLogin")) {
		    CommonExecute mem = new MemberLogin();
		    mem.execute(request);

		    String msg = (String)request.getAttribute("t_msg");
		    response.setContentType("text/html; charset=utf-8");
		    PrintWriter out = response.getWriter();
		    out.print(msg);
		    return;
		} else if(gubun.equals("logout")) {
			CommonExecute mem = new MemberLogout();
			mem.execute(request);
			viewPage = "common_alert.jsp";
		}
		RequestDispatcher rd = request.getRequestDispatcher(viewPage);
		rd.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
