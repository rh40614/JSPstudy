
package jspstudy.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import jspstudy.domain.BoardVo;
import jspstudy.domain.PageMaker;
import jspstudy.domain.SearchCriteria;
import jspstudy.service.BoardDao;



@WebServlet("/BoardController")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			request.setCharacterEncoding("UTf-8");
			response.setContentType("text/html;charset =UTF-8");
		
			
			String uri = request.getRequestURI();
			String pj =request.getContextPath();
			//프로젝트 이름을 뺀 나머지 가상경로 추출.substring으로 구해놓은 pj만큼을 uri에서 자름
			String command = uri.substring(pj.length());
			System.out.println("command:"+command);
			
			//사진 경로 
			String uploadPath = "D:\\open API (A)\\dev\\jspstudy\\src\\main\\webapp\\";	//실제저장경로
			String saveFolder = "images";
			String saveFullPath = uploadPath+saveFolder;
			
			
			if(command.equals("/board/boardWrite.do")) {
				
				RequestDispatcher rd= request.getRequestDispatcher("/board/boardWrite.jsp");// 진짜 경로 연결하기
				rd.forward(request, response);
				
			}else if(command.equals("/board/boardWriteAction.do")) { // 페이지에서 전송버튼을 누르면 값을 넘겨 받기.그리고 디비에 올리기 
				
				// 1. 화면단에서 값(파라미터)을 넘겨받기 
				
				//사진파일 저장경로 지정(멤버변수)  -request기능으로 saveFullPath에 저장할때 사용
					
				int sizeLimit = 1024*1024*15; 		// 15메가 까지만 업로드하겠다. 
				
				MultipartRequest multi = null; 		//두가지 타입을 넘길수있음 (String이랑 바이너리 타입(사진을 받는 타입) )
				multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy()); 
				//request의 기능을 사용하겠다. (값을 주고받기)// saveFullPath로 저장되는 경로를 지정한다. //사이즈 제한 //같은이름 중복으로 생겨나지않게 정책(같은 이름이면 뒤에(1)적히게)
				
//파라미터받기 	//파라미터를  request말고 multi 타입으로 받기
				String subject =multi.getParameter("subject");
				String content =multi.getParameter("content");
				String writer =multi.getParameter("writer");
				
				//Enumeration 메소드는 없고 변수만 있는 클래스 //열거자에 저장될 파일을 담는 객체를 생성한다. 
				Enumeration files  = multi.getFileNames();
				//담긴 파일 객체의 값을 얻는다. 
				String file = (String)files.nextElement();
				//파일이름 뽑아내기(디비에는 파일이름으로 저장한다.)
				String fileName= multi.getFilesystemName(file);		//저장되는 파일이름
				//String originFileName = multi.getOriginalFileName(file);	//진짜 파일 이름 추출
	
				String ip =InetAddress.getLocalHost().getHostAddress();	
					
				
				HttpSession session = request.getSession();
				int midx= (int)session.getAttribute("midx");	//로그인 한사람의 정보
				
			//2. 1번에서 정리한 값들 디비에 담기
			BoardDao bd =new BoardDao();
			//매개변수로 화면단의 정보받아서 디비에 입력하기.//파일은 이름만받는것
			int value = bd.insertBoard(subject, writer, content, ip, midx, fileName);
			
				if(value==1) {	//게시글 작성에 성공하면
					response.sendRedirect(request.getContextPath()+"/index.jsp");
				}else {		//실패하면
					System.out.println("게시글 작성실패");
					response.sendRedirect(request.getContextPath()+"/board/boardWrite.do");
				}


			
			}else if(command.equals("/board/boardList.do")) {
				
				
	//			//boardselectall에 page넘겨주기
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
					searchType="subject";
				}
				
				
				//처리
				SearchCriteria scri = new SearchCriteria(); 
				// 페이징 처리만했을때는 크라이테리아만 쓰면되는데 이것을 상속받은 SearchCriteria가 더 큰 기능을 수행하고 있음으로 SearchCriteria로 바꾸어준다. 
				scri.setPage(pagex); 		
				scri.setKeyword(keyword);
				scri.setSearchType(searchType);
				
				BoardDao bd =new BoardDao();
				int cnt = bd.boardTotal(scri); 
				// 게시물 전체 개수 가져오기(매개변수인 scri로 사용자가 검색을 안하면 페이지 목록이 10개. 검색하면 검색한 키워드의 게시물 개수만큼의 목록 개수가 나온다.)
				
				PageMaker pm =new PageMaker();
				pm.setScri(scri);		
				pm.setTotalCount(cnt);
				
				
				ArrayList<BoardVo> alist = bd.boardSelectAll(scri);			//위에서 처리해준scri값들
				request.setAttribute("alist", alist); //데이터(자원) 공유.  //게시판 자원공유
				request.setAttribute("pm", pm);// 페이지 정보도 화면단으로 가지고 이동한다. 
				
				System.out.println("리스트 불러오기 성공");
				
				//이동 (값을 처리하고 이동해야함)
				RequestDispatcher rd =request.getRequestDispatcher("/board/boardList.jsp");
				rd.forward(request, response);
				
				
			}else if(command.equals("/board/boardContent.do")) { //주소창을 통해서 데이터 넘길것 get
				System.out.println("컨텐트 불러오기 성공");
				//1. 파라미터 넘어옴
				String bidx = request.getParameter("bidx");
				int bidx_=Integer.parseInt(bidx); //boardselectone이라는 메서드는 매개변수로 int를 받으므로 변경해준다. 
				
				//2. 처리함(데이터가 실제로 화면단에 나오도록)
				BoardDao bd =new BoardDao();
				BoardVo bv = bd.boardSelectOne(bidx_);
				
				request.setAttribute("bv", bv);// 내부적으로 자원공유(jsp상단에 적어서 데이터가져옴)
				
				
				//3. 이동함 (제목 누르면 컨텐트로 연결)
				RequestDispatcher rd =request.getRequestDispatcher("/board/boardContent.jsp");
				rd.forward(request, response);
				
			}else if(command.equals("/board/boardModify.do")) {
				System.out.println("게시글 수정하기");
				//1. 파라미터 넘어옴
				String bidx = request.getParameter("bidx");
				int bidx_=Integer.parseInt(bidx); //boardselectone이라는 메서드는 매개변수로 int를 받으므로 변경해준다. 
				
				//2. 처리함(데이터가 실제로 화면단에 나오도록)
				BoardDao bd =new BoardDao();
				BoardVo bv = bd.boardSelectOne(bidx_);
				
				request.setAttribute("bv", bv);// 내부적으로 자원공유(jsp상단에 적어서 데이터가져옴)
				//실제경로로 연결
				RequestDispatcher rd =request.getRequestDispatcher("/board/boardModify.jsp");
				rd.forward(request, response);
				
			}else if(command.equals("/board/boardModifyAction.do")){
				//화면단에서 수정하고자하는 글의 제목,내용,작성자,ip, bidx넘어옴. bidx는 int형으로 바꿔주기
				String subject = request.getParameter("subject");
				String content = request.getParameter("content");
				String writer = request.getParameter("writer");
				String ip =InetAddress.getLocalHost().getHostAddress();
				String bidx = request.getParameter("bidx");
				
				int bidx_=Integer.parseInt(bidx); 
				
				//메서드생성해서 수정 메서드 사용. 매개변수로 
				BoardDao bd =new BoardDao();
				int value = bd.modify(subject,content,writer,ip, bidx_);
				
				//수정성공하면
				if(value==1) {	System.out.println("수정성공");
					response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
				}else {	//수정 실패하면	
					System.out.println("수정 실패");
					response.sendRedirect(request.getContextPath()+"/board/boardModify.do?bidx="+bidx);
					// ?는 sql쿼리를 사용하겠다?    수정에 실패하면 그 글을 다시 수정하러 가야하기 때문에 가상경로로 이동을 하면서 bidx값도 같이 가지고 갈 수 있도록 한다. 
				}
				
				
			}else if(command.equals("/board/boardDelete.do")) {
				
				//content에서 삭제버튼을 누르면 해당 bidx와 함께 넘어온다. 
				//bidx값을 받기 위해 getParameter로 받고 setAttribute로 내부공유해서 delete페이지에서 다시 사용할 수있도록한다. 
				String bidx= request.getParameter("bidx"); //parameter로 넘길때는 다 String
				request.setAttribute("bidx", bidx);
								
				RequestDispatcher rd = request.getRequestDispatcher("/board/boardDelete.jsp");
				rd.forward(request, response);
				
				
			}else if(command.equals("/board/boardDeleteAction.do")) {
				
				String bidx =request.getParameter("bidx");
				 int bidx_ =Integer.parseInt(bidx);
				 
				BoardDao bd = new BoardDao();
				int value = bd.delete(bidx_);
								
				
			if(value==1) {	System.out.println("삭제성공");
				response.sendRedirect(request.getContextPath()+"/board/boardList.do");
			}else {	
				System.out.println("삭제실패");
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
			}
				
				
			}else if(command.equals("/board/boardReply.do")) {
				//답변 버튼을 누르면 원글에 대해 답변을 달기위해
				//원글에대한 정보받기.어떤 글인지
				String bidx =request.getParameter("bidx");
				String originbidx = request.getParameter("originbidx");
				String depth = request.getParameter("depth");
				String level_ = request.getParameter("level_");
				//원글에 대한 정보를 출력 boardSelectOne을 쓰기 위해 생성
				BoardDao bd =new BoardDao();
				//답변을 쓰고 싶은 원글 보여주기 
				BoardVo bv = bd.boardSelectOne(Integer.parseInt(bidx));
				//내부공유를 위해서 두번째 bean을 만들어서 
				BoardVo bv2 = new BoardVo();
				//출력해야하는 원글의 정보는 메서드를 통해가져온 bv에서 꺼내서 bv2에 담아주기
				bv2.setSubject(bv.getSubject());	
				bv2.setContent(bv.getContent());
				bv2.setWriter(bv.getWriter());
				//원글이 무엇인지에 대한 정보 표시하기위해 받아온값 bv2에 담기(클라한테는 안보이게 표시해야함)
				bv2.setBidx(Integer.parseInt(bidx));		//값설정하는데 int형으로 바꿔주기 
				bv2.setOriginbidx(Integer.parseInt(originbidx));
				bv2.setDepth(Integer.parseInt(depth));
				bv2.setLevel_(Integer.parseInt(level_));
				
				//내부적으로 공유하기
				request.setAttribute("bv2", bv2);
				
				RequestDispatcher rd = request.getRequestDispatcher("/board/boardReply.jsp");
				rd.forward(request, response);
						
			}else if(command.equals("/board/boardReplyAction.do")) {
				//화면단에서 답변으로 입력한 값을 받아오기
				String bidx = request.getParameter("bidx");
				String originbidx = request.getParameter("originbidx");
				String depth = request.getParameter("depth");
				String level_ = request.getParameter("level_");
				String subject = request.getParameter("subject");
				String content = request.getParameter("content");
				String writer = request.getParameter("writer");
				String ip = InetAddress.getLocalHost().getHostAddress();
				HttpSession session = request.getSession();			//로그인 한 사람만 답변을 달 수있다.
				int midx= (int)session.getAttribute("midx");
			
				//boardReply()의 매개변수가 Vo 타입으므로 메서드의 인자값으로 쓰기위해 생성해서 화면에서 받은 값 모아 넣기
				BoardVo bv = new BoardVo();		
				bv.setBidx(Integer.parseInt(bidx));		
				bv.setOriginbidx(Integer.parseInt(originbidx));
				bv.setDepth(Integer.parseInt(depth));
				bv.setLevel_(Integer.parseInt(level_));
				bv.setSubject(subject);
				bv.setContent(content);
				bv.setWriter(writer);
				bv.setIp(ip);
				bv.setMidx(midx);
				
				
				BoardDao bd = new BoardDao();
				int value = bd.boardReply(bv);
				
				if(value == 1) {	System.out.println("답변작성완료");
					response.sendRedirect(request.getContextPath()+"/board/boardList.do");
				}else{	System.out.println("답변작성 실패");
					response.sendRedirect(request.getContextPath()+"/board/boardContext.do?bidx="+bidx);
				}
				
				
				
				
			}else if(command.equals("/board/fileDownload.do")) {
				
				//파일이름 넘겨받기
				String fileName = request.getParameter("fileName");
				//파일의 전체 경로 
				String filePath = saveFullPath + File.separator+fileName; //separator는 구분자 
				//해당 위치에 있는 파일을 읽어온다. 
				FileInputStream fileInputStream = new FileInputStream(filePath);
				//실제경로 뽑아서 
				Path source = Paths.get(filePath);
				//mimeType은 파일의 형식을 변환하기 위한 타입. 이메일 등에서 사진을 함께 전송할때 사진을 텍스트로 전환해서 전송하기위해 개발. 현재는 다양한 타입의 파일을 변환하는데 사용
				String mimeType = Files.probeContentType(source);
				//헤더 정보에 추출한 파일형식을 담는다.
				response.setContentType(mimeType);
				//인코딩
				String sEncoding = new String(fileName.getBytes("utf-8"));
				//헤더정보에 파일이름을 담는다. 
				response.setHeader("Content-Disposition","attatchment;filename"+ sEncoding); //attatchment;filename 첨부파일의 이름은~
				//파일 쓰기(출력)
				ServletOutputStream servletOutStream = response.getOutputStream();
				//파일을 읽었을때 
				byte[] b = new byte[4096];
				int read=0;
				while((read = fileInputStream.read(b, 0, b.length))!= -1 ) {
					servletOutStream.write(b,0,read);
				}
				servletOutStream.flush();
				servletOutStream.close();
				fileInputStream.close();
			
			}
			
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
