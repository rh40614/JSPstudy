package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/frontController")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uri = request.getRequestURI();
		String pj = request.getContextPath();
		
		String command = uri.substring(pj.length());
		
		String [] subpath = command.split("/");
		String location =subpath[1];	
		
		
		//�� Controller�� ����
		if(location.equals("member")) {
			MemberController mc = new MemberController();
			mc.doGet(request, response);
			
		}else if(location.equals("board")) {
			BoardController bc = new BoardController();
			bc.doGet(request, response);
			
		}else if(location.equals("film")) {
			FilmController fc = new FilmController();
			fc.doGet(request, response);
			
		}else if(location.equals("main")) {			//index.jsp ���������� ó���Ұ��� main.jsp�� ����  MainController�� �̿��ؼ� ó���� ���ֵ����Ѵ�. 
			MainController mc = new MainController();
			mc.doGet(request, response);
	
		}else if(location.equals("reply")) {		
			ReplyController rc = new ReplyController();
			rc.doGet(request, response);
	
		}
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
