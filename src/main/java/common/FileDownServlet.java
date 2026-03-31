package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FileDownServlet
 */
@WebServlet("/FileDownServlet")
public class FileDownServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileDownServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		String savePath = request.getParameter("t_fileDir");
		String fileName = request.getParameter("t_fileName");

		String localPath = "";

		if(savePath.equals("notice")) {
			// 공지사항 파일인 경우 SFTP에서 다운로드
			localPath = CommonUtil.getNoticeDir(request);
			File localFile = new File(localPath, fileName);

			// 로컬에 파일이 없으면 SFTP에서 다운로드
			if(!localFile.exists()) {
				boolean downloadSuccess = CommonUtil.downloadNoticeFromSFTP(fileName, localPath + fileName);
				if(!downloadSuccess) {
					response.setContentType("text/html;charset=UTF-8");
					out.println("<script language='javascript'>alert('파일을 다운로드할 수 없습니다.');history.back();</script>");
					return;
				}
			}
		} else {
			localPath = savePath;
		}

	    String orgfilename = fileName;

	    InputStream in = null;
	    OutputStream os = null;
	    File file = null;
	    boolean skip = false;
	    String client = "";

		try{
	        try{
	            file = new File(localPath, fileName);
	            in = new FileInputStream(file);
	        }catch(FileNotFoundException fe){
	            skip = true;
	        }

	        client = request.getHeader("User-Agent");
	        response.reset();
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Description", "JSP Generated Data");

	        if(!skip){

	            // IE
	            if(client.indexOf("MSIE") != -1){
	                response.setHeader ("Content-Disposition", "attachment; filename="+orgfilename);

	            }else{
	                // 한글 파일명 처리
	                orgfilename = new String(orgfilename.getBytes("utf-8"),"iso-8859-1");

	                response.setHeader("Content-Disposition", "attachment; filename=\"" + orgfilename + "\"");
	                response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
	            }

	            response.setHeader ("Content-Length", ""+file.length() );
	            os = response.getOutputStream();
	            byte b[] = new byte[(int)file.length()];
	            int leng = 0;

	            while( (leng = in.read(b)) > 0 ){
	                os.write(b,0,leng);
	            }

	            // 다운로드 후 임시로 다운로드한 파일 삭제 (공지사항 파일인 경우)
	            if(savePath.equals("notice")) {
	            	file.delete();
	            }

	        }else{
	            response.setContentType("text/html;charset=UTF-8");
	            out.println("<script language='javascript'>alert('파일을 찾을 수 없습니다');history.back();</script>");
	        }

	        if(in != null) {
				in.close();
			}
	        if(os != null) {
				os.close();
			}

	    }catch(Exception e){
	    	System.out.println("첨부 파일 다운 오류~ 파일명:"+fileName);
	    	e.printStackTrace();
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}