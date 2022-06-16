package jspstudy.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/FrontController") //wep.xmld �������� *.do Ȯ���ڸ� ���� �ڵ����� FrontController �� �Ѿ� ���� ����� �����س�����
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// HttpServeletRequest : Header����, Parameter, Cookie, URI, URL ���� ������ �о���̴� �޼ҵ带 ���� Ŭ����. Body�� Stream�� �о���̴� �޼ҵ带 ������ ����
		
		//URI(Uniform Resource Identifier) : ������ ��ġ�� �� �� �ִ� �ĺ���  //URL(Uniform Resource Locator) : ���� ������ ��ġ
		String uri = request.getRequestURI();
		String pj =request.getContextPath();
		//��ü��ο��� ������Ʈ �̸��� �� ������ ������ ����
		String command = uri.substring(pj.length());
		///member/memberList.do
		
		//"/" �� �����ڷ� ��� .�������� �տ� �ִ� ���ڿ��� �����Ѵ�. 
		String[] subpath = command.split("/");
		String location =subpath[1];			//�ι�° ���ڿ� ���� ��) member
		
		//�����ּ��� �ι�° / ���� member�� MemberController�� doGet �޼��尡 ����ǰ� board�� BorderController�� �����̵ȴ�. 
		if(location.equals("member")) {	
			MemberController mc = new MemberController();
			mc.doGet(request, response);
			
		}else if(location.equals("board")) {
			BoardController bc = new BoardController();
			bc.doGet(request, response);
			
		}
	
	}
		
		

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
