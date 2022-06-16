package jspstudy.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/FrontController") //wep.xmld 페이지에 *.do 확장자를 쓰면 자동으로 FrontController 로 넘어 오게 기능을 구현해놓았음
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// HttpServeletRequest : Header정보, Parameter, Cookie, URI, URL 등의 정보를 읽어들이는 메소드를 가진 클래스. Body의 Stream을 읽어들이는 메소드를 가지고 있음
		
		//URI(Uniform Resource Identifier) : 파일의 위치를 알 수 있는 식별자  //URL(Uniform Resource Locator) : 실제 파일의 위치
		String uri = request.getRequestURI();
		String pj =request.getContextPath();
		//전체경로에서 프로젝트 이름을 뺀 나머지 가상경로 추출
		String command = uri.substring(pj.length());
		///member/memberList.do
		
		//"/" 를 구분자로 사용 .구분자의 앞에 있는 문자열을 추출한다. 
		String[] subpath = command.split("/");
		String location =subpath[1];			//두번째 문자열 추출 예) member
		
		//가상주소의 두번째 / 앞이 member면 MemberController의 doGet 메서드가 실행되고 board면 BorderController가 실행이된다. 
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
