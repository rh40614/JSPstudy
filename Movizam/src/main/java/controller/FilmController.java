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

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import domain.FilmVo;
import domain.PageMaker;
import domain.SearchCriteria;
import service.FilmDao;


@WebServlet("/FilmController")
public class FilmController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTf-8");
		response.setContentType("text/html;charset =UTF-8");
			
		String uri = request.getRequestURI();
		String pj =request.getContextPath();	
			
		String command = uri.substring(pj.length());
		System.out.println("command:"+command);
			
		//영화 포스터 경로 
		String uploadPath = "D:\\open API (A)\\dev\\Movizam\\src\\main\\webapp\\image\\";	
		String saveFolder = "movie";
		String saveFullPath = uploadPath+saveFolder;
			
		
		
		
		
		
		//영화등록 페이지 연결
		if(command.equals("/film/filmInsert.do")) {
			
			RequestDispatcher rd= request.getRequestDispatcher("/film/filmInsert.jsp");
			rd.forward(request, response);
		
		//영화등록		
		}else if(command.equals("/film/filmInsertAction.do")) {  
				
				
			//사진파일 저장경로 멤버변수로 지정  -request기능으로 saveFullPath에 저장할때 사용
			int sizeLimit = 1024*1024*15; 		// 15메가 까지만 업로드하겠다. 
				
			//cos.jar  다운받아서  lib 에 넣음
			MultipartRequest multi =null;
				
			multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy()); 
			//DefaultFileRenamePolicy()  :같은이름 중복으로 생겨나지않게 정책(같은 이름이면 뒤에(1)적히게)
				
			
	
			String filmCategory =multi.getParameter("filmCategory");
			String filmName =multi.getParameter("filmName");
			String filmDate =multi.getParameter("filmDate");	
			String filmStatus = multi.getParameter("filmStatus");
			String filmDetail = multi.getParameter("filmDetail");
			//String filmPoster = multi.getParameter("filmPoster");
	
			//Enumeration 메소드는 없고 변수만 있는 클래스 //열거자에 저장될 파일을 담는 객체를 생성한다. 
			Enumeration files  = multi.getFileNames();
			//담긴 파일 객체의 값을 얻는다. 
			String file = (String)files.nextElement();
			//파일이름 뽑아내기(디비에는 파일이름으로 저장한다. filmPoster 로 받기) 
			String filmPoster= multi.getFilesystemName(file);		//저장되는 파일이름
			
			HttpSession session = request.getSession();
			int midx= (int)session.getAttribute("midx");	//로그인해서 영화를 등록할 관리자의 midx
				
			//담기
			FilmDao fd = new FilmDao();
			
			//매개변수로 화면단의 정보받아서 디비에 입력하기.//파일은 이름만받는것
			int value = fd.filmInsert(filmCategory, filmName, filmDate, filmStatus, filmDetail, filmPoster, midx);
			System.out.println("value: "+ value);
				if(value==1) {	//영화등록 성공
					System.out.println("영화등록성공");
					response.sendRedirect(request.getContextPath()+"/film/filmInsert.jsp");
				}else {		//실패하면
					System.out.println("영화등록실패");
					response.sendRedirect(request.getContextPath()+"/film/filmInsert.do");
				}


			//영화목록	//영화등록 정리하면 페이징 처리하기
			}else if(command.equals("/film/filmList.do")) {
				
				FilmDao fd = new FilmDao();
				
				//초기 페이지 파라미터는 null이면 1로 잡아준다. 			
				String page =request.getParameter("page");
				if(page == null) {page= "1";}
				int pagex =Integer.parseInt(page);
				
				//키워드 없을 경우 빈값. 서치 타입은 회원이름을 기본값으로 설정
				String keyword =request.getParameter("keyword");
				String searchType =request.getParameter("searchType");
				if(keyword == null) keyword= "";
				if(searchType == null) searchType= "filmName";
				
				//페이지랑 검색어 둘다 초기화설정해야함으로 SearchCreteria 쓰기
				SearchCriteria scri = new SearchCriteria();
				scri.setPage(pagex);
				scri.setKeyword(keyword);
				scri.setSearchType(searchType);
				
				//페이징설정하려면 총회원의 수 필요
				//(매개변수인 scri로 사용자가 검색을 안하면 페이지 목록이 10개단위. 검색하면 검색한 키워드의 게시물 개수만큼의 목록 개수가 나온다.)
				int cnt = fd.filmTotal(scri);
				
				PageMaker pm = new PageMaker();
				//검색했을때 조건에 맞는 회원의 수로 페이징
				pm.setScri(scri);
				//총 회원의 수를 받아서 페이징
				pm.setTotalCount(cnt);
				
				
				//영화목록 가지고 오기
				ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
				
				alist = fd.FilmSelectAll(scri);
				
				request.setAttribute("alist", alist);
				request.setAttribute("pm", pm);
				//System.out.println("alist: "+ alist);
				
				RequestDispatcher rd= request.getRequestDispatcher("/film/filmList.jsp");
				rd.forward(request, response);
			
				
				
			//영화 수정 전 view
			}else if(command.equals("/film/filmView.do")) {
				
				//어디서 받아오는거? 링크걸때 파라미터 걸어서 보내기!
				String fidx = request.getParameter("fidx");
				//System.out.println(fidx);
				int fidx_ = Integer.parseInt(fidx);
				
				FilmDao fd = new FilmDao();
				FilmVo fv = new FilmVo();
				
				fv = fd.FilmSelectOne(fidx_);
				
				request.setAttribute("fv", fv);
				
				RequestDispatcher rd= request.getRequestDispatcher("/film/filmView.jsp");
				rd.forward(request, response);
			
			//영화수정화면 연결
			}else if(command.equals("/film/filmEdit.do")) {
				

				//어디서 받아오는거? 링크걸때 파라미터 걸어서 보내기!
				String fidx = request.getParameter("fidx");
				int fidx_ = Integer.parseInt(fidx);
				
				FilmDao fd = new FilmDao();
				FilmVo fv = new FilmVo();
				
				fv = fd.FilmSelectOne(fidx_);
				
				request.setAttribute("fv", fv);
				
				RequestDispatcher rd= request.getRequestDispatcher("/film/filmEdit.jsp");
				rd.forward(request, response);
				
				
			//영화수정 처리	
			}else if(command.equals("/film/filmEditAction.do")) {
				
				
				//영화포스터	
				int sizeLimit = 1024*1024*15; 		// 15메가 까지만 업로드하겠다. 
					
				MultipartRequest multi =null;
				multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy()); 
								
				
				String filmCategory =multi.getParameter("filmCategory");
				String filmName =multi.getParameter("filmName");
				String filmDate =multi.getParameter("filmDate");	
				String filmStatus = multi.getParameter("filmStatus");
				String filmDetail = multi.getParameter("filmDetail");
				//String filmPoster = multi.getParameter("filmPoster");
				String fidx = multi.getParameter("fidx");
				int fidx_ = Integer.parseInt(fidx);
				
				//이름으로 가져와서 담기
				Enumeration files  = multi.getFileNames();
				//담긴 파일 객체의 값을 얻는다. 
				String file = (String)files.nextElement();
				//파일이름 뽑아내기(디비에는 파일이름으로 저장한다.)
				String filmPoster= multi.getFilesystemName(file);		//저장되는 파일이름
				
				
				FilmDao fd = new FilmDao();
				int value = fd.filmEdit(fidx_, filmCategory, filmName, filmDate, filmStatus, filmDetail, filmPoster);
				
				if(value==1) {	
					System.out.println("영화수정성공");
					response.sendRedirect(request.getContextPath()+"/film/filmView.do?fidx="+fidx);	//해당되는 게시물이 성공했는지를 확인하러가는것임으로 fidx 넘기기
				}else {		
					System.out.println("영화수정실패");
					response.sendRedirect(request.getContextPath()+"/film/filmEdit.do?fidx="+fidx);
				}
			
				
			//영화삭제	
			}else if(command.equals("/film/filmDelete.do")) {		//삭제하시겠습니까 확인창 띄우기
				
				String fidx = request.getParameter("fidx");	
				int fidx_ = Integer.parseInt(fidx);
				
				FilmDao fd =new FilmDao();
				int value = fd.filmDelete(fidx_);
				
				if(value==1) {	
					System.out.println("영화 삭제 성공");
					response.sendRedirect(request.getContextPath()+"/film/filmList.do");
				}else {		
					System.out.println("영화 삭제 실패");
					response.sendRedirect(request.getContextPath()+"/film/filmList.do");
				}
		
			//영화페이지 연결
			}else if(command.equals("/film/filmAll.do")) {
			
				
				FilmDao fd = new FilmDao();			
				
				ArrayList<FilmVo> alist1 = new ArrayList<FilmVo>();
				ArrayList<FilmVo> alist2 = new ArrayList<FilmVo>();
				ArrayList<FilmVo> alist3 = new ArrayList<FilmVo>();
					
				alist1 = fd.upcommingFilm();
				alist2 = fd.onFilm();
				alist3 = fd.offFilm();
				
				
				
				request.setAttribute("alist1", alist1);
				request.setAttribute("alist2", alist2);
				request.setAttribute("alist3", alist3);
					
				RequestDispatcher rd= request.getRequestDispatcher("/film/filmAll.jsp");
				rd.forward(request, response);

			
			
		}
		
		
		
		
		
		
		
		
		
		
			
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
