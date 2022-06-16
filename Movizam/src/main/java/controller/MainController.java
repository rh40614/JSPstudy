package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.BoardVo;
import domain.FilmVo;
import domain.PageMaker;
import domain.SearchCriteria;
import service.BoardDao;
import service.FilmDao;

@WebServlet("/MainController")
public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	
	
	
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTf-8");
		response.setContentType("text/html;charset =UTF-8");
			
		String uri = request.getRequestURI();
		String pj =request.getContextPath();	
			
		String command = uri.substring(pj.length());
		System.out.println("command:"+command);
		
				
				
		//영화 상영상태에 따라 불러오기
		if(command.equals("/main/main.do")){
			
//			HttpSession session = request.getSession();
//			
//			if(session.getAttribute("midx") == null){
//			    	session.setAttribute("saveURI",request.getRequestURI());
//			    }
			
			//영화	
			FilmDao fd = new FilmDao();			
			
			ArrayList<FilmVo> alist1 = new ArrayList<FilmVo>();
			ArrayList<FilmVo> alist2 = new ArrayList<FilmVo>();
			ArrayList<FilmVo> alist3 = new ArrayList<FilmVo>();
				
			alist1 = fd.upcommingFilmLimit();
			alist2 = fd.onFilmLimit();
			alist3 = fd.offFilmLimit();
			
			
			request.setAttribute("alist1", alist1);
			request.setAttribute("alist2", alist2);
			request.setAttribute("alist3", alist3);
				
			//게시글
			BoardDao bd = new BoardDao();
			
			ArrayList<BoardVo> alist4 = new ArrayList<BoardVo>();
			ArrayList<BoardVo> alist5 = new ArrayList<BoardVo>();
			
			alist4 = bd.boardHitMain();
			alist5 = bd.boardLikeMain();
			
			
			request.setAttribute("alist4", alist4);
			request.setAttribute("alist5", alist5);
			
			RequestDispatcher rd= request.getRequestDispatcher("/main/main.jsp");
			rd.forward(request, response);
		
		
		}else if(command.equals("/main/mainSearchAction.do")){
			 
			 
			String keyword = request.getParameter("keyword"); 
			if(keyword == null) keyword="";
			System.out.println("keyword: "+ keyword);
			 
			//처리 
			SearchCriteria scri = new SearchCriteria();
			 
			scri.setKeyword(keyword);
			 
			
			BoardDao bd =new BoardDao(); 
			FilmDao fd = new FilmDao();
			
			ArrayList<BoardVo> alist1 = bd.boardSelectAll_main(scri); //위에서 처리해준scri값들
			request.setAttribute("alist1", alist1); //데이터(자원) 공유. //게시판 자원공유
		
			//System.out.println("alist1: "+alist1);
			
		 
		//	ArrayList<FilmVo> alist2 =fd.FilmSelectAll_main(scri);
		//	request.setAttribute("alist2", alist2);
			
		
			
			RequestDispatcher rd= request.getRequestDispatcher("/main/SearchResult.jsp");
			rd.forward(request, response);
			}
			 

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
