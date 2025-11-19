package command.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import common.CommonExecute;
import dao.MemberDao;
import dto.MemberDto;

public class MemberLogin implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("utf-8");
            
            MemberDao dao = new MemberDao();
            String email = request.getParameter("t_id");
            String password = request.getParameter("t_pw");
            
            // 암호화
            String encryptedPw = dao.encryptSHA256(password);
            
            // 로그인 시도 - DTO 반환
            MemberDto memberDto = dao.memberLogin(email, encryptedPw);
            
            String msg = "", url = "";
            if(memberDto == null) {
                msg = "ID나 비밀번호가 정확하지 않습니다.";
                url = "Member?t_gubun=login";
            } else {
                msg = memberDto.getName() + "님 환영합니다.";
                url = "Index";
                
                HttpSession session = request.getSession();
                session.setAttribute("sessionId", email);
                session.setAttribute("sessionName", memberDto.getName());
                session.setAttribute("userName", memberDto.getName());
                session.setAttribute("sessionLevel", memberDto.getRole()); // DB의 role 사용
                
                session.setMaxInactiveInterval(60 * 60 * 4);
            }
            
            request.setAttribute("t_msg", msg);
            request.setAttribute("t_url", url);
            
        } catch(Exception e) {
            System.out.println("MemberLogin 오류");
            e.printStackTrace();
            request.setAttribute("t_msg", "로그인 처리 중 오류가 발생했습니다.");
            request.setAttribute("t_url", "Member?t_gubun=login");
        }
    }
}