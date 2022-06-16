package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.Criteria;
import domain.PageMaker;
import domain.ReplyVo;
import domain.SearchCriteria;
import service.ReplyDao;


@WebServlet("/ReplyController")
public class ReplyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		request.setCharacterEncoding("UTf-8");
		response.setContentType("text/html;charset =UTF-8");
			
		String uri = request.getRequestURI();
		String pj =request.getContextPath();	
			
		String command = uri.substring(pj.length());
		System.out.println("command:"+command);
		
		
		
		
		//댓글 작성
		if(command.equals("/reply/replyWriteAction.do")) {
		
			String bidx =request.getParameter("bidx");
			int bidx_ = Integer.parseInt(bidx);
			String replyContent = request.getParameter("replyContent");
			String ip =InetAddress.getLocalHost().getHostAddress();	
			
			HttpSession session = request.getSession();
			int midx= (int)session.getAttribute("midx");
			String memberNickname = (String)session.getAttribute("memberNickname");
			
			
			ReplyDao rpd = new ReplyDao();
			int value = rpd.ReplyWrite(memberNickname, replyContent, ip, midx, bidx_);
			
			if(value==1) {	
				System.out.println("댓글 작성성공");
				/* response.sendRedirect((String)session.getAttribute("saveURI")); */
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx); 
			}else {		
				System.out.println("댓글 작성실패");
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
			}
		
		//댓글 삭제
		}else if(command.equals("/reply/replyDelete.do")) {
		
			String ridx = request.getParameter("ridx");
			String bidx = request.getParameter("bidx");
			int ridx_ = Integer.parseInt(ridx);
			
			
			ReplyDao rpd = new ReplyDao();
			int value = rpd.replyDelete(ridx_);
			
			
			if(value==1) {	
				System.out.println("댓글 삭제완료");
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx); 
			}else {		
				System.out.println("댓글 삭제 실패");
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
			}
			
		//댓글 수정
		}else if(command.equals("/reply/replyEdit.do")) {

			String ridx = request.getParameter("ridx");
			//System.out.println("ridx" + ridx);
			
			int ridx_ = Integer.parseInt(ridx);
			String bidx = request.getParameter("bidx");
			//System.out.println("bidx"+ bidx);
			String replyContent_E = request.getParameter("replyContent_E");
			//System.out.println("replyContent_E"+replyContent_E);
			String ip =InetAddress.getLocalHost().getHostAddress();	
			
			ReplyDao rpd = new ReplyDao();
			
			int value = rpd.ReplyEdit(replyContent_E, ip, ridx_);
			
			
			if(value==1) {	
				System.out.println("댓글 수정완료");
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx); 
			}else {		
				System.out.println("댓글 수정실패");
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
			}
			
		//대댓글
		}else if(command.equals("/reply/replyComment.do")) {
		
			//1. 값
			String ridx = request.getParameter("ridx");
			String bidx = request.getParameter("bidx");
			String replyWriter = request.getParameter("replyWriter");
			String replyContent_R = request.getParameter("replyContent_R");
			String replyWdate = request.getParameter("replyWdate");
				
			String originRidx = request.getParameter("originRidx");
			String depth = request.getParameter("depth");	
			String level_ = request.getParameter("level_");			
			String ip =InetAddress.getLocalHost().getHostAddress();	
			
			System.out.println(originRidx);
			System.out.println(depth);
			System.out.println(level_);
			
			
			
			HttpSession session = request.getSession();			//로그인 한 사람만 답변을 달 수있다.
			int midx= (int)session.getAttribute("midx");
			
			
			//2. 담기
			ReplyVo rv = new ReplyVo(); 
			rv.setRidx(Integer.parseInt(ridx));
			rv.setBidx(Integer.parseInt(bidx));
			rv.setReplyWriter(replyWriter);
			rv.setReplyContent(replyContent_R);
			rv.setReplyWdate(replyWdate);
			rv.setOriginRidx(Integer.parseInt(originRidx));
			rv.setDepth(Integer.parseInt(depth));
			rv.setLevel_(Integer.parseInt(level_));
			rv.setReplyIp(ip);
			rv.setMidx(midx);
			
			ReplyDao rpd = new ReplyDao();
			
			int value = rpd.replyComment(rv);
			
			
			if(value==1) {	
				System.out.println("대댓글 작성성공");
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx); 
			}else {		
				System.out.println("대댓글 작성실패");
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
			}
		
		//댓글관리(관리자)
		}else if(command.equals("/reply/manageReplyAdmin.do")) {
			
			String page = request.getParameter("page");
			if(page== null) page="1";
			int pagex=Integer.parseInt(page);
			
			//검색창에 값을 입력하는 경우. 값을 받기 안하면 null
			String keyword = request.getParameter("keyword");
			if(keyword == null) {	//검색안하면
				keyword="";
			}
			String searchType = request.getParameter("searchType");
			if(searchType == null ) {	//boardList에서 searchType을 제목과 작성자 두가지로 설정하였는데 사용자가 설정하지 않고 검색하려고 하면 기본값을 제목으로 한다. 
				searchType="replyContent";
			}
			
			
			//처리
			SearchCriteria scri = new SearchCriteria();	
			scri.setPage(pagex); 		
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			ReplyDao rpd = new ReplyDao();
			int cnt = rpd.ReplyTotal(scri);
			
			PageMaker pm =new PageMaker();
			pm.setScri(scri);		
			pm.setTotalCount(cnt);
			
			ArrayList<ReplyVo> alist= rpd.ReplySelectAll_manage(scri);
			
			request.setAttribute("alist", alist);
			request.setAttribute("pm", pm);
			//이동
			RequestDispatcher rd =request.getRequestDispatcher("/reply/manageReplyAdmin.jsp");
			rd.forward(request, response);
	
		//댓글 관리(일반회원)	
		}else if(command.equals("/reply/manageReply.do")) {
			
			HttpSession session = request.getSession();
			int midx= (int)session.getAttribute("midx");
			
			String page = request.getParameter("page");
			if(page== null) page="1";
			int pagex=Integer.parseInt(page);
			
			//검색창에 값을 입력하는 경우. 값을 받기 안하면 null
			String keyword = request.getParameter("keyword");
			if(keyword == null) {	//검색안하면
				keyword="";
			}
			String searchType = request.getParameter("searchType");
			if(searchType == null ) {	//boardList에서 searchType을 제목과 작성자 두가지로 설정하였는데 사용자가 설정하지 않고 검색하려고 하면 기본값을 제목으로 한다. 
				searchType="replyContent";
			}
			
			
			//처리
			SearchCriteria scri = new SearchCriteria();	
			scri.setPage(pagex); 		
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			ReplyDao rpd = new ReplyDao();
			int cnt = rpd.ReplyTotal(scri);
			
			PageMaker pm =new PageMaker();
			pm.setScri(scri);		
			pm.setTotalCount(cnt);
			
			ArrayList<ReplyVo> alist= rpd.ReplySelectAll_normal(scri, midx);
			
			request.setAttribute("alist", alist);
			request.setAttribute("pm", pm);
			//이동
			RequestDispatcher rd =request.getRequestDispatcher("/reply/manageReply.jsp");
			rd.forward(request, response);
	
			
		}else if(command.equals("/reply/replyBlindOn.do")) {
		
			String ridx = request.getParameter("ridx");
			
			ReplyDao rpd = new ReplyDao();
			
			int value = rpd.blindYNon(Integer.parseInt(ridx));
			
			if(value==1) {	
				System.out.println("댓글 블라인드 완료");
				response.sendRedirect(request.getContextPath()+"/reply/manageReplyAdmin.do"); 
			}else {		
				System.out.println("댓글 블라인드 실패");
				response.sendRedirect(request.getContextPath()+"/reply/manageReplyAdmin.do");
			}
			
		}else if(command.equals("/reply/replyBlindOff.do")) {
		
			String ridx = request.getParameter("ridx");
			
			ReplyDao rpd = new ReplyDao();
			
			int value = rpd.blindYNoff(Integer.parseInt(ridx));
			
			if(value==1) {	
				System.out.println("댓글 블라인드 완료");
				response.sendRedirect(request.getContextPath()+"/reply/manageReplyAdmin.do"); 
			}else {		
				System.out.println("댓글 블라인드 실패");
				response.sendRedirect(request.getContextPath()+"/reply/manageReplyAdmin.do");
			}
		}
	
	}


	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
