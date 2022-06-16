package jspstudy.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jspstudy.domain.MemberVo;
import jspstudy.service.MemberDao;


@WebServlet("/MemberController") // 웹서블릿에 .do라고 하는것을 등록 // 톰캣서버는 .jsp만 처리하지만 서블릿에 .do 확장자로들어오는 것은 이 클래스 memberCONTROLLER가 해석하세요하고등록. 
//가상경로는 서버가아닌 서블릿이 처리하는 것
public class MemberController extends HttpServlet { 	//httpServlet을 상속받아서 자바 언어로 웹화면을 출력할 수 있도록함
	private static final long serialVersionUID = 1L;
    
   

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//주소의 full경로를 추출하는 메서드
		String uri = request.getRequestURI();
		//프로젝트 이름 추출 
		String pj =request.getContextPath();
		//프로젝트 이름을 뺀 나머지 가상경로 추출.substring으로 구해놓은 pj만큼을 uri에서 자름
		String command = uri.substring(pj.length());
		System.out.println("command:"+command);
		
		
		
		//가상경로가 command와같으면 실행
		if(command.equals("/member/memberJoinAction.do")) {	
				
			//PrintWriter out =response.getWriter();
			
			//memberJoinOk.jsp의 기능을 가져옴
			request.setCharacterEncoding("UTf-8");
			//값 받아오기
			String memberId =request.getParameter("memberId");//html 에서 input에 담아둔 값을 담고 있는 객체의 이름을 써준다. 
			String memberpwd =request.getParameter("memberpwd");
			String memberName =request.getParameter("memberName");
			String memberPhone =request.getParameter("memberPhone");
			String memberJumin =request.getParameter("memberJumin");
			String memberAdress =request.getParameter("memberAdress");
			String memberemail =request.getParameter("memberemail");
			String memberGender =request.getParameter("memberGender");
			//여러개의 값을 담은 객체의 이름을 호출하면 배열형태의 값을리턴한다. getParameterValues 사용
			String[] memberHobby =request.getParameterValues("memberHobby"); //중복선택이 가능한것들은 배열로 받기
			//hobby는 동시에 여러값을 받아서 출력함으로 반복문을 사용하고, hobby하는 변수를 만들어 변수에 배열로 반복한 값을 담아서 반복할 수 있도록한다.
			String hobby="";
			for(int i = 0; i<memberHobby.length; i++){
				hobby +=","+memberHobby[i];
				//out.println(memberHobby[i]+"<br>");
			}hobby =hobby.substring(1);
		//	out.println(hobby);
			//InetAdress 클래스의 호스트 정보를 불러오는 기능에 주소를 뽑는 기능
			String ip = InetAddress.getLocalHost().getHostAddress();
			
			
			
			
			//연결객체와 문자열 쿼리화 할수있도록 dao 생성
			MemberDao md = new MemberDao();	
			
			int value = md.insertMember( memberId, memberpwd,  memberName, memberemail, memberGender, memberAdress, memberPhone, memberJumin, hobby, ip);
			
		//	out.println(value);  //1이 뜨면 1행이 성공. 데이터 베이스안에 값이 들어가게 된다. 
			//1값이 뜨면서 디비에 데이터가 올라가면 회원가입이 완료된것이므로 회원가입이 완료되면 메인페이지로 옮길 수 있도록한다. //실패하면 다시 하도록 이동.
			
			
			if (value==1){
				response.sendRedirect(request.getContextPath()+"/member/memberList.do"); 	
				//sendredirect는 요청을 받으면 특정 url로가서 응답을받으라고 다시 보내면 다시 요청이가서 응답하는 방식
				//out.println("<script>alert('회원가입이 완료되었습니다.'); //location.href='"+request.getContextPath()+"/index.jsp'</script>");
			}else{
				response.sendRedirect(request.getContextPath()+"/member/memberJoin.do");
				//out.println("<script>alert('회원가입실패'); location.href='./memberJoin.jsp'</script>");
			}
		
		}else if (command.equals("/member/memberJoin.do")) {
				//회원가입 페이지로 들어오면 처리함
			System.out.println("test");
			//넘어온 페이지를 이동시키는 방법의 메서드중하나. 가상경로로 넘어와서 내부적으로는 실제페이지가 나타나게끔 지정해주는것
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberJoin.jsp");
			rd.forward(request, response); //forward 방식은 외부(클라이언트)는 모르게 내부에서 이동하는 방식. 클라는 가상주소를 가르키고 forward를 이용해서 가상주소를 진짜주소로 가리키게 한다. 
				
		}else if(command.equals("/member/memberList.do")) {
				
			//memberList.jsp있던 객체 생성을 여기로 가져온다. 부하를 줄이기 위해 
			MemberDao md = new MemberDao(); 
			ArrayList<MemberVo> alist =md.memberSelectAll();
			System.out.println(alist);
			//alist에 담은 값을 화면에 출력하기
			request.setAttribute("alist",alist);
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberList.jsp");
			rd.forward(request, response); 
		
			//로그인
		}else if(command.equals("/member/memberLogin.do")) {
			//로그인 페이지로 연결
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberLogin.jsp");
			rd.forward(request, response); 
			
		}else if(command.equals("/member/memberLoginAction.do")) {
			//1. 넘어온 값을 받는다. 
			String memberId = request.getParameter("memberId"); //input의 name값으로 받기
			String memberPwd = request.getParameter("memberPwd");
			
			//2. 처리하기 
			MemberDao md = new MemberDao();
			MemberVo mv = md.memberLogin(memberId, memberPwd);
			
			//세션으로 넘기기 웹컨테이너에 유지하는 값
			HttpSession session  = request.getSession();
			if(mv != null){
				session.setAttribute("midx", mv.getMidx());
				session.setAttribute("memberId", mv.getMemberId());
				session.setAttribute("memberName", mv.getMembername()); 
			
			
			//3. 이동한다. 
			System.out.println("세션생성. 로그인성공");
			//response.sendRedirect(request.getContextPath()+ "/member/memberList.do");
			
			//session 값이 있다면 (게시글 작성 등 어떠한 기능을 이용하려는 와중에 로그인을 하지않아서 넘어왔을 경우)
			if(session.getAttribute("saveURI")!= null)
			{
				response.sendRedirect((String)session.getAttribute("saveURI"));
			}
			
			}else {System.out.println("로그인 실패");
			response.sendRedirect(request.getContextPath()+ "/member/memberLogin.do");
			}
		
		}else if(command.equals("/member/memberLogout.do")) {
			
			HttpSession session = request.getSession();
			session.invalidate(); //설정된 모든 세션제거

			response.sendRedirect(request.getContextPath()+"/");
		}
		
	}
		
		
		
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
