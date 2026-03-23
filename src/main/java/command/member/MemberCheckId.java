package command.member;

import java.io.IOException;
import java.io.PrintWriter;

import dao.MemberDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/MemberCheckId")
public class MemberCheckId extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public MemberCheckId() {
        super();
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberDao dao  = new MemberDao();
		String id = request.getParameter("t_id");

		int count = dao.memberCheckId(id);

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		if(count == 0) {
			out.print("사용가능");
		} else {
			out.print("사용불가");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
