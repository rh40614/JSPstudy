package jspstudy.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/ServletTest")
public class ServletTest extends HttpServlet {  					//servlet은 자바를 이용하여 클래스로만든 웹페이지. httpservlet을 상속하면서 웹페이지로서 기능하게 된다. 
	private static final long serialVersionUID = 1L;				//자바 코드안에 html코드 작성가능
       
   //기본생성자 
    public ServletTest() {
        super();
        
    }


	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// response.getWriter().append("Served at: ").append(request.getContextPath()).append("first servlet"); 
		
		//글자 깨짐방지
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		//printwriter 클래스의getwriter기능을 이용하면 출력가능. 참조변수 out을 만들어서 println을 이용한다. 
		
		PrintWriter out = response.getWriter();
		
		out.println("<html>"
				+ "<head>"
				+ "<title>서블릿</title>"
				+ "</head>"
				+ "<body>"
				+ "<h1>안녕하세요</h1>"
				+ "</body>"
				+ "</html>");
	
	}

	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
