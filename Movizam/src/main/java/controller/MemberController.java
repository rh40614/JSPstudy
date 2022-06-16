package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.jasper.tagplugins.jstl.core.Out;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import domain.MemberVo;
import domain.PageMaker;
import domain.SearchCriteria;
import service.MemberDao;


@WebServlet("/memberController")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTf-8");
		response.setContentType("text/html;charset =UTF-8");
			
		String uri = request.getRequestURI();
		String pj = request.getContextPath();
		String command = uri.substring(pj.length());
		System.out.println("command :"+command);
		
		//회원이미지 경로 
		String uploadPath = "D:\\open API (A)\\dev\\Movizam\\src\\main\\webapp\\image\\";	
		String saveFolder = "profile";
		String saveFullPath = uploadPath+saveFolder;
		
		
		
		//회원가입
		if(command.equals("/member/memberJoinAction.do")) {
			
			request.setCharacterEncoding("UTf-8");
			
			//회원가입 폼으로 넘겨받는 데이터
			String memberId = request.getParameter("memberId");
			String memberPwd =request.getParameter("memberPwd");
			String memberNickname =request.getParameter("memberNickname");
			String memberName =request.getParameter("memberName");
			String memberAdr =request.getParameter("memberAdr");
			String memberPhone =request.getParameter("memberPhone");
			String memberEmail =request.getParameter("memberEmail");
			String memberIp = InetAddress.getLocalHost().getHostAddress();
			
			String birth_yy = request.getParameter("birth_yy");
			String birth_mm = request.getParameter("birth_mm");
			String birth_dd = request.getParameter("birth_dd");
			
			
			MemberDao md = new MemberDao();
			
			int value = md.insertMember(memberId, memberPwd, memberNickname, memberName, memberPhone, memberAdr, memberEmail, memberIp, birth_yy, birth_mm, birth_dd);
			
			
			if(value == 1) {
				response.sendRedirect(request.getContextPath()+"/main/main.do"); 	
			
				System.out.println("회원가입성공");
						
			}else{
				response.sendRedirect(request.getContextPath()+"/member/memberJoin.do");
				System.out.println("회원가입실패");
			}
			
		
		}else if(command.equals("/member/memberJoin.do")) {
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberJoin.jsp");
			rd.forward(request, response);
			
		}else if(command.equals("/member/memberLogin.do")) {
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberLogin.jsp");
			rd.forward(request, response);
		
		}
		
		//로그인
		else if(command.equals("/member/memberLoginAction.do")){
			
			String memberId = request.getParameter("memberId");
			String memberPwd = request.getParameter("memberPwd");
			String memberIp = InetAddress.getLocalHost().getHostAddress();
			
			MemberDao md = new MemberDao();
			MemberVo mv =md.memberLogin(memberId, memberPwd);
			
			//---로그인 성공----
			///환영메세지
			String message=md.randomGreetingMessage();
			
			//세션형성
			HttpSession session = request.getSession(); 
			if(mv!= null) {
				session.setAttribute("midx", mv.getMidx());
				session.setAttribute("memberId",mv.getMemberId());
				session.setAttribute("memberPwd",mv.getMemberPwd());
				session.setAttribute("memberNickname", mv.getMemberNickname());
				session.setAttribute("adminYN", mv.getAdminYN());
				//회원이미지 
				session.setAttribute("memberImage", mv.getMemberImage());
				//환영인사도 같이 담아가기
				session.setAttribute("message", message);
			
				//로그인정보 입력
				int midx= (int)session.getAttribute("midx");
				System.out.println("왘");
				
				int loginInfo = md.loginInfoInsert(memberIp, midx);
				
				if(loginInfo == 1) {
					System.out.println("로그인정보 입력완료");
				
				}else {
					System.out.println("로그인정보 입력실패");
				}
				
				//로그인실패값 클리어
//				md.clearLoginFailCount(memberId);
				
			
			
//			System.out.println("saveURI: "+session.getAttribute("saveURI"));
//			System.out.println("nickname session :"+session.getAttribute("memberNickname"));
//			System.out.println("message :"+message);
//			System.out.println("message session :"+session.getAttribute("message"));
				
			//System.out.println(session.getAttribute("saveURI"));
				
				//session 값을 가지고 온곳으로 돌아가기
				if(session.getAttribute("saveURI")!= null){
					
					response.sendRedirect((String)session.getAttribute("saveURI"));
					
					
				}else {//홈페이지에서 로그인한거면 홈페이지로 연결(인덱스 페이지는 jsp이기때문에 데이터를 가지고 sendredirect로 이동할 수없다.  가상주소 사용해서 이동하기)
					response.sendRedirect(request.getContextPath()+ "/main/main.do");
				}
			
				
				
			}else {System.out.println("로그인 실패");
			//로그인 실패하면 카운트 올리고 다시 로그인페이지로 이동
			
			//int loginfail = md.plusLoginFailCount(memberId);
//			if(loginfail == 1) {
//				System.out.println("로그인실패 카운트 업");
//			
//			}else {
//				System.out.println("로그인 실패카운트 실패");
//			}
//			md.loginLock(memberId);
			response.sendRedirect(request.getContextPath()+ "/member/memberLogin.do");
			
			}
			
	
		//로그아웃
		}else if(command.equals("/member/memberLogout.do")) {
			
			HttpSession session = request.getSession();
			session.invalidate(); //설정된 모든 세션제거

			response.sendRedirect(request.getContextPath()+ "/main/main.do");
		
		//회원정보찾기 화면
		}else if(command.equals("/member/memberInfo_search.do")){
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberInfo_search.jsp");
			rd.forward(request, response);
		//회원정보 찾기	
		}else if(command.equals("/member/memberInfo_searchAction.do")){
			
			String memberName = request.getParameter("memberName");
			String birth_yy = request.getParameter("birth_yy");
			String birth_mm = request.getParameter("birth_mm");
			String birth_dd = request.getParameter("birth_dd");
			String memberPhone = request.getParameter("memberPhone");
			
			int yy = Integer.parseInt(birth_yy);
			int mm = Integer.parseInt(birth_mm);
			int dd = Integer.parseInt(birth_dd);
			
			MemberDao md = new MemberDao();
			MemberVo mv =new MemberVo();
			mv = md.memberInfoReturn(memberName, yy, mm, dd, memberPhone);
			
			
			request.setAttribute("mv",mv);
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberInfo_search.jsp");
			rd.forward(request, response);
		
			
		//회원탈퇴 
		}else if(command.equals("/member/memberDelete.do")) {

			RequestDispatcher rd = request.getRequestDispatcher("/member/memberDelete.jsp");
			rd.forward(request, response);
			
		//회원탈퇴성공 확인 페이지
		}else if(command.equals("/member/memberDeleteConfirm.do")) {
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberDeleteConfirm.jsp");
			rd.forward(request, response);
							
		//회원탈퇴 처리	
		}else if(command.equals("/member/memberDeleteAction.do")) {
			
			String memberId = request.getParameter("memberId");
			String memberPwd = request.getParameter("memberPwd");
			
			MemberDao md = new MemberDao();
			int value = md.memberDelete(memberId, memberPwd);
			
			if(value == 1 ) {
				HttpSession session = request.getSession();
				session.invalidate(); //설정된 모든 세션제거
				
				response.sendRedirect(request.getContextPath()+"/member/memberDeleteConfirm.do"); 	
				
				System.out.println("회원탈퇴 성공");
			}else {
				response.sendRedirect(request.getContextPath()+"/member/memberDelete.do");
				System.out.println("회원탈퇴실패");
				
			}
		
		
		//회원정보보기
		}else if(command.equals("/member/memberInfo.do")) {

			MemberDao md =new MemberDao();
			MemberVo mv = new MemberVo();

			//이미 로그인된 상태임으로 세션에서 midx 가져오기
			HttpSession session = request.getSession();
			int midx= (int)session.getAttribute("midx");
			
			mv = md.memberSelectOne(midx);
			
			request.setAttribute("mv", mv);
			
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberInfo.jsp");
			rd.forward(request, response);	
			
		//회원정보 수정페이지로 이동 	
		}else if(command.equals("/member/memberEdit.do")) {
			
			MemberDao md =new MemberDao();
			MemberVo mv = new MemberVo();

			
			//이미 로그인된 상태임으로 세션에서 midx 가져오기
			HttpSession session = request.getSession();
			int midx= (int)session.getAttribute("midx");
			
			mv = md.memberSelectOne(midx);
			
			request.setAttribute("mv", mv);
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberEdit.jsp");
			rd.forward(request, response);
			
			
		//회원정보 수정하기 	
		}else if(command.equals("/member/memberEditAction.do")) {	
			
			MemberDao md =new MemberDao();
			
			MultipartRequest multi =null;
			  
			int sizeLimit = 1024*1024*15;
			 
			multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy());
			 
			Enumeration files = multi.getFileNames(); 
			String file =(String)files.nextElement();
			
			
			HttpSession session = request.getSession();
			int midx= (int)session.getAttribute("midx");
			
			String memberPwd = multi.getParameter("memberPwd");
			String memberNickname = multi.getParameter("memberNickname");
			String memberName = multi.getParameter("memberName");
			String birth_yy = multi.getParameter("birth_yy");
			String birth_mm = multi.getParameter("birth_mm");
			String birth_dd = multi.getParameter("birth_dd");
			String memberAdr = multi.getParameter("memberAdr");
			String memberPhone = multi.getParameter("memberPhone");
			String memberEmail = multi.getParameter("memberEmail");
			
			String memberImage= multi.getFilesystemName(file); //회원이미지
			
			//String midx = request.getParameter("midx"); //midx는 위에서 session으로 받아옴 
			
			int birth_yy_ = Integer.parseInt(birth_yy);
			int birth_mm_ = Integer.parseInt(birth_mm);
			int birth_dd_ = Integer.parseInt(birth_dd);
			
			
			 System.out.println(memberPwd); 
			 System.out.println(memberNickname);
			 System.out.println(memberName); 
			 System.out.println(memberAdr);
			 System.out.println(memberPhone); 
			 System.out.println(memberEmail);
			 
			 System.out.println(birth_yy); 
			 System.out.println(birth_mm);
			 System.out.println(birth_dd);
			 
			 System.out.println(birth_yy_); 
			 System.out.println(birth_mm_);
			 System.out.println(birth_dd_);
			 

			
			
			int value = md.memberEdit( memberPwd, memberNickname, memberName, birth_yy_, birth_mm_, birth_dd_, memberAdr, memberPhone, memberEmail, memberImage, midx);
		
			if(value==1) {
				System.out.println("회원정보 수정성공");
				response.sendRedirect(request.getContextPath()+"/member/memberInfo.do");
			}else{
				System.out.println("회원정보 수정실패");
				response.sendRedirect(request.getContextPath()+"/member/memberEdit.do");
			
			}	
			
			
			
			
		//회원목록
		}else if(command.equals("/member/memberList.do")) {
			
			MemberDao md =new MemberDao();
			
			//초기 페이지 파라미터는 null이면 1로 잡아준다. 			
			String page =request.getParameter("page");
			if(page == null) {page= "1";}
			int pagex =Integer.parseInt(page);
			
			//키워드 없을 경우 빈값. 서치 타입은 회원이름을 기본값으로 설정
			String keyword =request.getParameter("keyword");
			String searchType =request.getParameter("searchType");
			if(keyword == null) keyword= "";
			if(searchType == null) searchType= "memberName";
			
			//페이지랑 검색어 둘다 초기화설정해야함으로 SearchCreteria 쓰기
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(pagex);
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			//페이징설정하려면 총회원의 수 필요
			//(매개변수인 scri로 사용자가 검색을 안하면 페이지 목록이 10개단위. 검색하면 검색한 키워드의 게시물 개수만큼의 목록 개수가 나온다.)
			int cnt = md.MemberTotal(scri);
			
			PageMaker pm = new PageMaker();
			//검색했을때 조건에 맞는 회원의 수로 페이징
			pm.setScri(scri);
			//총 회원의 수를 받아서 페이징
			pm.setTotalCount(cnt);
			
			
			//회원정보 가지고 오기
			ArrayList<MemberVo> alist =md.memberSelectAll(scri);
			
			request.setAttribute("alist", alist);
			request.setAttribute("pm", pm);
			//System.out.println("pm share: "+pm);
			//System.out.println("alist: " + alist);
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberList.jsp");
			rd.forward(request, response);
			
			
			
	//회원정보보기(관리자)
	}else if(command.equals("/member/memberInfoAdmin.do")) {

		MemberDao md =new MemberDao();
		MemberVo mv = new MemberVo();

		//관리자의 midx 가 아니라 회원을 누르면 회원에 해당하는 정보 연결하기 
		//HttpSession session = request.getSession();
		//int midx= (int)session.getAttribute("midx");
		
		String midx = request.getParameter("midx");
		int midxx = Integer.parseInt(midx);
		
		mv = md.memberSelectOne(midxx);
		
		request.setAttribute("mv", mv);
		
		
		RequestDispatcher rd = request.getRequestDispatcher("/member/memberInfoAdmin.jsp");
		rd.forward(request, response);	
		
	//회원정보 수정페이지로 이동 (관리자)
	}else if(command.equals("/member/memberEditAdmin.do")) {
		
		MemberDao md =new MemberDao();
		MemberVo mv = new MemberVo();

		
		String midx = request.getParameter("midx");
		int midxx = Integer.parseInt(midx);
		
		mv = md.memberSelectOne(midxx);
		
		request.setAttribute("mv", mv);
		//System.out.println("mv screen: "+ mv);
		//System.out.println("midxx: "+ midxx);
		
		RequestDispatcher rd = request.getRequestDispatcher("/member/memberEditAdmin.jsp");
		rd.forward(request, response);
		
		
	//회원정보 수정하기 (관리자)	
	}else if(command.equals("/member/memberEditAdminAction.do")) {	
		
		MemberDao md =new MemberDao();
		
		MultipartRequest multi =null;
		  
		int sizeLimit = 1024*1024*15;
		 
		multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy());
		 
		Enumeration files = multi.getFileNames(); 
		String file =(String)files.nextElement();
		
		// 회원이 수정하는 것과 관리자가 수정하는 것은 midx 값이 다르므로 따로 구현
		String midx = request.getParameter("midx");
		int midxx = Integer.parseInt(midx);
		String memberPwd = multi.getParameter("memberPwd");
		String memberNickname = multi.getParameter("memberNickname");
		String memberName = multi.getParameter("memberName");
		String birth_yy = multi.getParameter("birth_yy");
		String birth_mm = multi.getParameter("birth_mm");
		String birth_dd = multi.getParameter("birth_dd");
		String memberAdr = multi.getParameter("memberAdr");
		String memberPhone = multi.getParameter("memberPhone");
		String memberEmail = multi.getParameter("memberEmail");
		
		String memberImage= multi.getFilesystemName(file); //회원이미지
		
	
		
		int birth_yy_ = Integer.parseInt(birth_yy);
		int birth_mm_ = Integer.parseInt(birth_mm);
		int birth_dd_ = Integer.parseInt(birth_dd);
		
		
//		 System.out.println(memberPwd); 
//		 System.out.println(memberNickname);
//		 System.out.println(memberName); 
//		 System.out.println(memberAdr);
//		 System.out.println(memberPhone); 
//		 System.out.println(memberEmail);
		 
//		System.out.println(midx);
//		System.out.println(midxx);
//		
//		System.out.println(birth_yy); 
//		System.out.println(birth_mm);
//		System.out.println(birth_dd);
//		 
//		System.out.println(birth_yy_); 
//		System.out.println(birth_mm_);
//		System.out.println(birth_dd_);
		 

		
		
		int value = md.memberEdit( memberPwd, memberNickname, memberName, birth_yy_, birth_mm_, birth_dd_, memberAdr, memberPhone, memberEmail, memberImage, midxx);
	
		if(value==1) {
			System.out.println("회원정보 수정성공");
			response.sendRedirect(request.getContextPath()+"/member/memberInfoAdmin.do?midx="+midxx);
		}else{
			System.out.println("회원정보 수정실패");
			response.sendRedirect(request.getContextPath()+"/member/memberEditAdmin.do?midx="+midxx);
		
		}	
		
			
			
		
	}else if(command.equals("/member/memberAdminOn.do")) {
		
		String midx = request.getParameter("midx");
		
		MemberDao md = new MemberDao();
		
		int value = md.AdminYNon(Integer.parseInt(midx));
		
		
		if(value==1) {
			System.out.println("관리자로 권한 변경되었습니다.");
			response.sendRedirect(request.getContextPath()+"/member/memberList.do");
		}else{
			System.out.println("권한 변경 실패");
			response.sendRedirect(request.getContextPath()+"/member/memberList.do");
		
		}
		
	//관리자 권한 해제
	}else if(command.equals("/member/memberAdminOff.do")) {
		
		String midx = request.getParameter("midx");
		
		MemberDao md = new MemberDao();
		
		int value = md.AdminYNoff(Integer.parseInt(midx));
		
		
		if(value==1) {
			System.out.println("일반회원으로 권한이 변경되었습니다.");
			response.sendRedirect(request.getContextPath()+"/member/memberList.do");
		}else{
			System.out.println("권한 변경 실패");
			response.sendRedirect(request.getContextPath()+"/member/memberList.do");
		
		}	
	}
		
			
			
			
				
		
	}
			
	
			
			  
			
			
			
			
		
		
		
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
