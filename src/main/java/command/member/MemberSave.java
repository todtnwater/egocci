package command.member;

import javax.servlet.http.HttpServletRequest;
import common.CommonExecute;
import dao.MemberDao;
import dto.MemberDto;

public class MemberSave implements CommonExecute {
    @Override
    public void execute(HttpServletRequest request) {
        try {
            MemberDao dao = new MemberDao();
            String email = request.getParameter("t_id");
            String password = request.getParameter("t_pw");
            String name = request.getParameter("t_name");
            String phone1 = request.getParameter("t_phone1");
            String phone2 = request.getParameter("t_phone2");
            String phone3 = request.getParameter("t_phone3");
            String gender = request.getParameter("t_gender");
            
            // 암호화 - 실패하면 예외 발생
            String encryptedPw = dao.encryptSHA256(password);
            
            MemberDto dto = new MemberDto(email, encryptedPw, name, phone1, phone2, phone3, gender);
            int result = dao.memberSave(dto);
            
            String msg = "";
            if(result > 0) {
                msg = "회원가입이 완료되었습니다.";
            } else {
                msg = "회원가입에 실패했습니다.";
            }
            
            request.setAttribute("t_msg", msg);
            request.setAttribute("t_url", "Member?t_gubun=login");
            
        } catch(Exception e) {
            System.out.println("MemberSave 오류");
            e.printStackTrace();
            request.setAttribute("t_msg", "회원가입 처리 중 오류가 발생했습니다.");
            request.setAttribute("t_url", "Member?t_gubun=join");
        }
    }
}