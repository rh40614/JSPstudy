package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import domain.BoardVo;
import domain.Criteria;
import domain.PageMaker;
import domain.ReplyVo;
import domain.SearchCriteria;
import service.BoardDao;
import service.ReplyDao;


@WebServlet("/BoardController")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTf-8");
		response.setContentType("text/html;charset =UTF-8");
			
		String uri = request.getRequestURI();
		String pj =request.getContextPath();	
			
		String command = uri.substring(pj.length());
		System.out.println("command:"+command);
		
		// 사진경로
		String uploadPath = "D:\\open API (A)\\dev\\Movizam\\src\\main\\webapp\\image\\";
		String saveFolder = "boardImg";
		String saveFullPath = uploadPath+saveFolder;
		
	
		
		
		
		//게시글 쓰기 
		if(command.equals("/board/boardWrite.do")) {
			
			RequestDispatcher rd= request.getRequestDispatcher("/board/boardWrite.jsp");// 진짜 경로 연결하기
			rd.forward(request, response);
			
		}else if(command.equals("/board/boardWriteAction.do")) { // 페이지에서 전송버튼을 누르면 값을 넘겨 받기.그리고 디비에 올리기 
			
			// 1. 값받기
			
			int sizeLimit = 1024*1024*15; 		// 15메가 까지만 업로드하겠다. 
			
			MultipartRequest multi = null; 		
			multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy()); 
		
			String boardType = multi.getParameter("boardType");
			String boardTitle =multi.getParameter("title");
			String boardContent =multi.getParameter("textarea");
			String ip =InetAddress.getLocalHost().getHostAddress();	
			
			Enumeration files  = multi.getFileNames();
			String file = (String)files.nextElement();
			String boardFilename= multi.getFilesystemName(file);		//저장되는 파일이름
			
			//로그인 한사람의 정보
			HttpSession session = request.getSession();
			int midx= (int)session.getAttribute("midx");
			
			//게시글 작성자를 멤버 닉네임으로 설정해주기 위해 session에서 가져오기
			String boardWriter = (String)session.getAttribute("memberNickname");
			
			//System.out.println("midx: "+midx);
			
			//2. 처리
			BoardDao bd =new BoardDao();
		
			int value = bd.boardWrite(boardTitle, boardContent, boardWriter, boardFilename, ip, midx, boardType);
		
			if(value==1) {	//게시글 작성에 성공하면
				System.out.println("게시글 작성성공");

			//자유게시판이면 자유게시판 쪽으로 이동하고, 영화게시판이면 영화 게시판 쪽으로 이동하게하기
				 if(boardType.equals("free")) {
					 response.sendRedirect(request.getContextPath()+"/board/boardList.do"); 
				 }else if(boardType.equals("film")) {
					 response.sendRedirect(request.getContextPath()+"/board/boardList_film.do"); 
				 }
				 
			}else {		//실패하면
				System.out.println("게시글 작성실패");
				response.sendRedirect(request.getContextPath()+"/board/boardWrite.do");
			}
			
		//게시글 리스트
		}else if(command.equals("/board/boardList.do")) {
			
			String page = request.getParameter("page");
			if(page == null) page ="1";
			int pagex = Integer.parseInt(page);
			
			String keyword = request.getParameter("keyword");
			if(keyword == null)	keyword="";
			//boardList에서 searchType을 제목과 작성자 두가지로 설정하였는데 사용자가 설정하지 않고 검색하려고 하면 기본값을 제목으로 한다. 	
			String searchType = request.getParameter("searchType");
			if(searchType == null ) searchType="boardTitle";
			

			
			//처리
			SearchCriteria scri = new SearchCriteria(); 
			scri.setPage(pagex); 		
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			BoardDao bd =new BoardDao();
			int cnt = bd.boardTotal_free(scri); 
			// 게시물 전체 개수 가져오기(매개변수인 scri로 사용자가 검색을 안하면 페이지 목록이 10개. 검색하면 검색한 키워드의 게시물 개수만큼의 목록 개수가 나온다.)
			
			PageMaker pm =new PageMaker();
			pm.setScri(scri);		
			pm.setTotalCount(cnt);
			
			
			ArrayList<BoardVo> alist = bd.boardSelectAll(scri);			//위에서 처리해준scri값들
			request.setAttribute("alist", alist); //데이터(자원) 공유.  //게시판 자원공유
			request.setAttribute("pm", pm);// 페이지 정보도 화면단으로 가지고 이동한다. 
			
			
			//이동
			RequestDispatcher rd =request.getRequestDispatcher("/board/boardList.jsp");
			rd.forward(request, response);
			
		
		
		//게시글 한개보기(조회수 올리는 메서드도 같이 쓰기)(이전 다음 칼럼 안 만들어서 해쉬맵)
		}else if(command.equals("/board/boardContent.do")) { //주소창을 통해서 데이터 넘길것 get
			
			BoardDao bd =new BoardDao();
			
		//	int flagx = 0;
			
			String flag = request.getParameter("flag");			
			String bidx = request.getParameter("bidx");
			int bidx_=Integer.parseInt(bidx); //boardselectone이라는 메서드는 매개변수로 int를 받으므로 변경해준다. 
			
			if(flag == null) {
				//조회수올리기 
				bd.boardHitCnt(bidx_);	
			}// 추천 버튼을 누르고 들어오면 flag 값이 있어서 조회수는 안올라가게설정.
			
			
			//게시글 하나 출력
			BoardVo bv = bd.boardSelectOne(bidx_);
			//System.out.println("controller bv"+bv);
			
			//조회수올리기 
			//bd.boardHitCnt(bidx_);
			
			//이전글 다음글
			HashMap<String, Integer> map = bd.bidxPreNext(bidx_);
			//System.out.println("map pre: "+map.get("pre"));
			 
			
			//댓글 리스트 불러오기
			ReplyDao rpd = new ReplyDao();
			SearchCriteria scri = new SearchCriteria();
			
			
			ArrayList<ReplyVo> alist= rpd.ReplySelectAll(scri, bidx_);
			
			
			// 화면단 데이터
			request.setAttribute("bv", bv);
			request.setAttribute("map", map);
			request.setAttribute("alist", alist);
			
			
			
			System.out.println("게시글 불러오기 성공");
			
			//이동(제목 누르면 컨텐트로 연결)
			RequestDispatcher rd =request.getRequestDispatcher("/board/boardContent.jsp");
			rd.forward(request, response);
	
			
		//게시글 수정 페이지 이동
		}else if(command.equals("/board/boardEdit.do")) {
			
			
			String bidx = request.getParameter("bidx");
			int bidx_=Integer.parseInt(bidx); 
			
			//처리
			BoardDao bd =new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidx_);
			
			request.setAttribute("bv", bv);// 내부적으로 자원공유(jsp상단에 적어서 데이터가져옴)
			
			RequestDispatcher rd =request.getRequestDispatcher("/board/boardEdit.jsp");
			rd.forward(request, response);
		
		//게시글 수정처리	
		}else if(command.equals("/board/boardEditAction.do")){
			//화면단에서 수정하고자하는 글의 제목,내용,작성자,ip, bidx넘어옴. bidx는 int형으로 바꿔주기
			
			int sizeLimit = 1024*1024*15; 		// 15메가 까지만 업로드하겠다. 
			
			MultipartRequest multi = null; 		
			multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy()); 
			
			Enumeration files  = multi.getFileNames();
			String file = (String)files.nextElement();
			String boardFilename= multi.getFilesystemName(file);
			
			String boardType = request.getParameter("boardType");
			String boardTitle = request.getParameter("boardTitle");
			String boardContent = request.getParameter("boardContent");
			String ip =InetAddress.getLocalHost().getHostAddress();
			String bidx = request.getParameter("bidx");
			
			int bidx_=Integer.parseInt(bidx); 
			
			System.out.println("boardType: "+boardType);
			System.out.println("boardTitle: "+boardTitle);
			System.out.println("boardContent: "+boardContent);
			System.out.println("bidx: "+bidx);
			
			//메서드생성해서 수정 메서드 사용. 매개변수로 
			BoardDao bd =new BoardDao();
			int value = bd.boardEdit(boardTitle,boardContent,boardFilename, ip, bidx_, boardType);
			
			//수정성공하면
			if(value==1) {	System.out.println("수정성공");
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
			}else {	//수정 실패하면	
				System.out.println("수정실패");
				response.sendRedirect(request.getContextPath()+"/board/boardEdit.do?bidx="+bidx);
				// ?는 sql쿼리를 사용하겠다?    수정에 실패하면 그 글을 다시 수정하러 가야하기 때문에 가상경로로 이동을 하면서 bidx값도 같이 가지고 갈 수 있도록 한다. 
			}
			
		//게시글 삭제	
		}else if(command.equals("/board/boardDeleteAction.do")) {
			System.out.println("삭제 커멘드 진입");
			
			String bidx =request.getParameter("bidx");
			int bidx_ =Integer.parseInt(bidx);
			//System.out.println(bidx);
			
			BoardDao bd = new BoardDao();
			int value = bd.delete(bidx_);
							
			
			if(value==1) {	System.out.println("삭제성공");
				response.sendRedirect(request.getContextPath()+"/board/boardList.do");
			}else {	
				System.out.println("삭제실패");
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
			}
			
	
		//게시물 추천 - content에서 추천 버튼을 누르면 실행
		}else if(command.equals("/board/contentRecommend.do")) {
			
			BoardDao bd = new BoardDao();
			
			String bidx =request.getParameter("bidx");
			int bidx_ =Integer.parseInt(bidx);
			
			// null 값으로 들어올 변수의 int 담을 거 미리선언하고값 받아서 int 형으로 넣기
			int flagx = 0;
			String flag = request.getParameter("flag");	
			flagx = Integer.parseInt(flag); 
			//추천 버튼에 flag 1주고
			if(flagx == 1) {
				//추천카운트 올리기
				int value = bd.boardLikeCnt(bidx_);	
				
				if(value==1) {	
					System.out.println("추천!");	//추천해서 들어갈때는 프러그 가지고 가서 조회수 안오르게 하기
					response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx+"&flag="+flag);
				}else {	
					System.out.println("추천안됑");
					response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
				}	
			}
			
		//인기게시판
		}else if(command.equals("/board/hitList.do")) {
		
			String page = request.getParameter("page");
			if(page== null) page="1";
			int pagex=Integer.parseInt(page);
			
			//검색창에 값을 입력하는 경우. 값을 받기 안하면 null
			String keyword = request.getParameter("keyword");
			if(keyword == null)	keyword="";
			//boardList에서 searchType을 제목과 작성자 두가지로 설정하였는데 사용자가 설정하지 않고 검색하려고 하면 기본값을 제목으로 한다. 	
			String searchType = request.getParameter("searchType");
			if(searchType == null ) searchType="boardTitle";

			
						
			//처리
			SearchCriteria scri = new SearchCriteria(); 
			
			scri.setPage(pagex); 		
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			BoardDao bd =new BoardDao();
			int cnt = bd.boardTotal(scri); 
			// 게시물 전체 개수 가져오기(매개변수인 scri로 사용자가 검색을 안하면 페이지 목록이 10개. 검색하면 검색한 키워드의 게시물 개수만큼의 목록 개수가 나온다.)
			
			PageMaker pm =new PageMaker();
			pm.setScri(scri);		
			pm.setTotalCount(cnt);
			
			ArrayList<BoardVo> alist = bd.boardHitAll(scri);	//위에서 처리해준scri값들
			
			request.setAttribute("alist", alist); //데이터(자원) 공유.  //게시판 자원공유
			request.setAttribute("pm", pm);// 페이지 정보도 화면단으로 가지고 이동한다. 
			
			
			//이동
			RequestDispatcher rd =request.getRequestDispatcher("/board/hitList.jsp");
			rd.forward(request, response);
			
			
			
			
			
			
			
		//추천게시판
		}else if(command.equals("/board/likeList.do")) {
			
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
				searchType="boardTitle";
			}
			
			
			//처리
			SearchCriteria scri = new SearchCriteria(); 
			
			scri.setPage(pagex); 		
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			BoardDao bd =new BoardDao();
			int cnt = bd.boardTotal(scri); 
			// 게시물 전체 개수 가져오기(매개변수인 scri로 사용자가 검색을 안하면 페이지 목록이 10개. 검색하면 검색한 키워드의 게시물 개수만큼의 목록 개수가 나온다.)
			
			PageMaker pm =new PageMaker();
			pm.setScri(scri);		
			pm.setTotalCount(cnt);
			
		
			ArrayList<BoardVo> alist = bd.boardLikeAll(scri);	//위에서 처리해준scri값들
			
			request.setAttribute("alist", alist); //데이터(자원) 공유.  //게시판 자원공유
			request.setAttribute("pm", pm);// 페이지 정보도 화면단으로 가지고 이동한다. 
			
			System.out.println("cnt: "+cnt);
			System.out.println("alist: "+alist);
			System.out.println("pm: "+pm);
			
			//이동
			RequestDispatcher rd =request.getRequestDispatcher("/board/likeList.jsp");
			rd.forward(request, response);
			
		
		
		
		
		//게시글(영화) 리스트
		}else if(command.equals("/board/boardList_film.do")) {
			
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
				searchType="boardTitle";
			}
			
			
			//처리
			SearchCriteria scri = new SearchCriteria(); 
			
			scri.setPage(pagex); 		
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			BoardDao bd =new BoardDao();
			int cnt = bd.boardTotal_film(scri); 
			// 게시물 전체 개수 가져오기(매개변수인 scri로 사용자가 검색을 안하면 페이지 목록이 10개. 검색하면 검색한 키워드의 게시물 개수만큼의 목록 개수가 나온다.)
			
			PageMaker pm =new PageMaker();
			pm.setScri(scri);		
			pm.setTotalCount(cnt);
			
			
			ArrayList<BoardVo> alist = bd.boardSelectAll_film(scri);			//위에서 처리해준scri값들
			request.setAttribute("alist", alist); //데이터(자원) 공유.  //게시판 자원공유
			request.setAttribute("pm", pm);// 페이지 정보도 화면단으로 가지고 이동한다. 
			
			
			//이동
			RequestDispatcher rd =request.getRequestDispatcher("/board/boardList_film.jsp");
			rd.forward(request, response);
			
		
		}	
		
	}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
