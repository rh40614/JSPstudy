package mysqlProject.controller;

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

import mysqlProject.domain.Criteria;
import mysqlProject.domain.PageMaker;
import mysqlProject.domain.SearchCriteria;
import mysqlProject.domain.BoardVo;
import mysqlProject.domain.CommentVo;
import mysqlProject.service.BoardDao;
import mysqlProject.service.CommentDao;

@WebServlet("/BoardController")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//한글 깨짐 방지
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		HttpSession session = request.getSession();
		
		//주소의 풀경로를 추출
		String uri = request.getRequestURI();
		//프로젝트 이름을 추출
		String pj = request.getContextPath();
		//프로젝트 이름을 뺀 나머지 가상경로를 추출
		String command = uri.substring(pj.length());
		System.out.println("command:"+command);
		
		if(command.equals("/board/write.do")) {
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardWrite.jsp");
			rd.forward(request, response);
		
		}else if(command.equals("/board/writeAction.do")) {
			String category = request.getParameter("category");
			int category_ = Integer.parseInt(category);
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			String ip = InetAddress.getLocalHost().getHostAddress();
			int midx = (int)session.getAttribute("midx");
			String writer = (String)session.getAttribute("memberNicname");
			String notice = request.getParameter("mainnotice");
			String profile = (String)session.getAttribute("memberProfile");
			
			BoardVo bv = new BoardVo();
			bv.setCategory(category_);
			bv.setTitle(title);
			bv.setContent(content);
			bv.setWriter(writer);
			bv.setBoardip(ip);
			bv.setMidx(midx);
			bv.setWriter(writer);
			bv.setNotice(notice);
			bv.setProfile(profile);
			
			BoardDao bd = new BoardDao();
			int value = bd.Write(bv);
			
			if(value == 1){
				response.sendRedirect(request.getContextPath()+"/");
			}else{
				response.sendRedirect(request.getContextPath()+"/board/writeAction.do");
			}
		
		}else if(command.equals("/board/content.do")) {
			String page = request.getParameter("page");
			if(page == null) page = "1";
			int page_ = Integer.parseInt(page);
			String bidx = request.getParameter("bidx");
			int bidx_ = Integer.parseInt(bidx);
			
			//처리
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardContent(bidx_);
			
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(page_);
			 
			CommentDao cd = new CommentDao();
			int cnt = cd.commentList(bidx_);
			Criteria cri = new Criteria();
			cri.setPage(page_);
			PageMaker pm = new PageMaker();
			pm.setScri(scri);
			pm.setTotalCount(cnt);
			ArrayList<CommentVo> clist = cd.commentListAll(scri,bidx_);
			
			request.setAttribute("clist", clist);	//데이터(자원) 공유
			request.setAttribute("pm", pm);
			request.setAttribute("bv", bv);
			
			//3.이동
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardContent.jsp");
			rd.forward(request, response);
			
		}else if(command.equals("/board/list.do")) {
			String page = request.getParameter("page");
			if(page == null) page = "1";
			int page_ = Integer.parseInt(page);
			
			String keyword = request.getParameter("keyword");
			if(keyword == null) keyword = "";
			String searchType = request.getParameter("searchType");
			if(searchType == null) searchType = "all";
			String category = request.getParameter("category");
			if(category == null) category = "0";
			
			int category_ = Integer.parseInt(category);
			if(category_ == 6) {
				PrintWriter out = response.getWriter();
				out.println("<script>window.location.href='"+request.getContextPath()+"/board/gallery.do?category=6';</script>");
				out.flush();
				
			}
			//처리
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(page_);
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			scri.setCategory(category_);
			
			BoardDao bd = new BoardDao();
			int cnt = bd.boardList(scri);
			Criteria cri = new Criteria();
			cri.setPage(page_);
			
			PageMaker pm = new PageMaker();
			pm.setScri(scri);
			pm.setTotalCount(cnt);
			if(searchType.equals("all")) {
				ArrayList<BoardVo> alist = bd.boardListAll(scri);
				request.setAttribute("alist", alist);	//데이터(자원) 공유
			}
			else {
				ArrayList<BoardVo> alist = bd.boardSearchAll(scri);
				request.setAttribute("alist", alist);	//데이터(자원) 공유
			}
			request.setAttribute("pm", pm);
			session.setAttribute("category", category_);
			
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardList.jsp");
			rd.forward(request, response);
			//이동
			
		}else if(command.equals("/board/delete.do")) {
			String bidx = request.getParameter("bidx");
			int bidx_ = Integer.parseInt(bidx);
			
			BoardDao bd = new BoardDao();
			int value = bd.boardDelete(bidx_);
			
			if(value == 1) {
				PrintWriter out = response.getWriter();
				out.println("<script>alert('삭제 완료'); location.href='"+request.getContextPath()+"/'</script>");
				out.flush();
			}else {
				PrintWriter out = response.getWriter();
				out.println("<script>alert('오류발생'); location.href='"+request.getContextPath()+"/'</script>");
				out.flush();
			}
		
		}else if(command.equals("/board/modify.do")) {
			String bidx = request.getParameter("bidx");
			int bidx_ = Integer.parseInt(bidx);
			
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardContent(bidx_);
			request.setAttribute("bv", bv);	//내부적 자원공유
			//3.이동
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardModify.jsp");
			rd.forward(request, response);
		
		}else if(command.equals("/board/modifyAction.do")) {
			String bidx = request.getParameter("bidx");
			int bidx_ = Integer.parseInt(bidx);
			String category = request.getParameter("category");
			int category_ = Integer.parseInt(category);
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			String ip = InetAddress.getLocalHost().getHostAddress();
			String notice = request.getParameter("mainnotice");
			
			BoardVo bv = new BoardVo();
			bv.setBidx(bidx_);
			bv.setCategory(category_);
			bv.setTitle(title);
			bv.setContent(content);
			bv.setBoardip(ip);
			bv.setNotice(notice);
			
			BoardDao bd = new BoardDao();
			int value = bd.boardModify(bv);
			//int value2 = bd.boardModifyIp(bidx_,ip);
			
			if(value == 1){
				response.sendRedirect(request.getContextPath()+"/");
			}else{
				response.sendRedirect(request.getContextPath()+"/board/modify.do");
			}
		
		}else if(command.equals("/board/search.do")) {
			String page = request.getParameter("page");
			if(page == null) page = "1";
			int page_ = Integer.parseInt(page);
			String keyword = request.getParameter("keyword");
			if(keyword == null) keyword = "";
			String searchType = request.getParameter("searchType");
			if(searchType == null) searchType = "title-content";
			//처리
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(page_);
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			BoardDao bd = new BoardDao();
			int cnt = bd.boardSearch(scri);
			
			Criteria cri = new Criteria();
			cri.setPage(page_);
			
			PageMaker pm = new PageMaker();
			pm.setScri(scri);
			pm.setTotalCount(cnt);
			
			ArrayList<BoardVo> alist = bd.boardSearchAll(scri);
			request.setAttribute("alist", alist);	//데이터(자원) 공유
			request.setAttribute("pm", pm);
			session.setAttribute("keyword", keyword);
			//이동
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardSearch.jsp");
			rd.forward(request, response);
		
		}else if(command.equals("/board/reply.do")) {
			String bidx = (String)request.getParameter("bidx");
			String category = (String)request.getParameter("category");
			String depth = (String)request.getParameter("depth");
			
			request.setAttribute("bidx", bidx);
			request.setAttribute("category", category);
			request.setAttribute("depth", depth);
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardReply.jsp");
			rd.forward(request, response);
		
		}else if(command.equals("/board/replyAction.do")) {
			String category = request.getParameter("category");
			int category_ = Integer.parseInt(category);
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			String ip = InetAddress.getLocalHost().getHostAddress();
			int midx = (int)session.getAttribute("midx");
			String writer = (String)session.getAttribute("memberNicname");
			String bidx = request.getParameter("bidx");
			int bidx_ = Integer.parseInt(bidx);
			String depth = request.getParameter("depth");
			int depth_ = Integer.parseInt(depth);
			
			BoardVo bv = new BoardVo();
			bv.setCategory(category_);
			bv.setTitle(title);
			bv.setContent(content);
			bv.setWriter(writer);
			bv.setBoardip(ip);
			bv.setMidx(midx);
			bv.setWriter(writer);
			bv.setBidx(bidx_);
			bv.setDepth(depth_);
			
			BoardDao bd = new BoardDao();
			int value = bd.reply(bv);
			
			if(value == 2){
				response.sendRedirect(request.getContextPath()+"/");
			}else{
				response.sendRedirect(request.getContextPath()+"/board/writeAction.do");
			}
		
		}else if(command.equals("/board/gallery.do")) {
			String page = request.getParameter("page");
			if(page == null) page = "1";
			int page_ = Integer.parseInt(page);
			
			String keyword = request.getParameter("keyword");
			if(keyword == null) keyword = "";
			String searchType = request.getParameter("searchType");
			if(searchType == null) searchType = "all";
			String category = request.getParameter("category");
			if(category == null) category = "0";
			int category_ = Integer.parseInt(category);
			//처리
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(page_);
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			scri.setCategory(category_);
			
			BoardDao bd = new BoardDao();
			int cnt = bd.boardList(scri);
			Criteria cri = new Criteria();
			cri.setPage(page_);
			
			PageMaker pm = new PageMaker();
			pm.setScri(scri);
			pm.setTotalCount(cnt);
			if(searchType.equals("all")) {
				ArrayList<BoardVo> alist = bd.boardGalleryAll(scri);
				request.setAttribute("alist", alist);	//데이터(자원) 공유
			}
			else {
				ArrayList<BoardVo> alist = bd.boardGallerySearchAll(scri);
				request.setAttribute("alist", alist);	//데이터(자원) 공유
			}
			request.setAttribute("pm", pm);
			session.setAttribute("category", category_);
			
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardGallery.jsp");
			rd.forward(request, response);
			//이동
			
		}
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
