package command.member;

import common.CommonExecute;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class MemberLogout implements CommonExecute {

	@Override
	public void execute(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String loginName = (String)session.getAttribute("sessionName");

		session.invalidate();
		String msg;
		if(loginName == null) {
			msg = "로그인 유지시간이 초과되어 로그아웃 되었습니다.";
		} else {
			msg = loginName + "님 로그아웃 되었습니다.";
		}
		request.setAttribute("t_msg", msg);
		request.setAttribute("t_url", "Index");
	}

}