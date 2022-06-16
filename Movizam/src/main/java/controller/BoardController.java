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
		
		// �������
		String uploadPath = "D:\\open API (A)\\dev\\Movizam\\src\\main\\webapp\\image\\";
		String saveFolder = "boardImg";
		String saveFullPath = uploadPath+saveFolder;
		
	
		
		
		
		//�Խñ� ���� 
		if(command.equals("/board/boardWrite.do")) {
			
			RequestDispatcher rd= request.getRequestDispatcher("/board/boardWrite.jsp");// ��¥ ��� �����ϱ�
			rd.forward(request, response);
			
		}else if(command.equals("/board/boardWriteAction.do")) { // ���������� ���۹�ư�� ������ ���� �Ѱ� �ޱ�.�׸��� ��� �ø��� 
			
			// 1. ���ޱ�
			
			int sizeLimit = 1024*1024*15; 		// 15�ް� ������ ���ε��ϰڴ�. 
			
			MultipartRequest multi = null; 		
			multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy()); 
		
			String boardType = multi.getParameter("boardType");
			String boardTitle =multi.getParameter("title");
			String boardContent =multi.getParameter("textarea");
			String ip =InetAddress.getLocalHost().getHostAddress();	
			
			Enumeration files  = multi.getFileNames();
			String file = (String)files.nextElement();
			String boardFilename= multi.getFilesystemName(file);		//����Ǵ� �����̸�
			
			//�α��� �ѻ���� ����
			HttpSession session = request.getSession();
			int midx= (int)session.getAttribute("midx");
			
			//�Խñ� �ۼ��ڸ� ��� �г������� �������ֱ� ���� session���� ��������
			String boardWriter = (String)session.getAttribute("memberNickname");
			
			//System.out.println("midx: "+midx);
			
			//2. ó��
			BoardDao bd =new BoardDao();
		
			int value = bd.boardWrite(boardTitle, boardContent, boardWriter, boardFilename, ip, midx, boardType);
		
			if(value==1) {	//�Խñ� �ۼ��� �����ϸ�
				System.out.println("�Խñ� �ۼ�����");

			//�����Խ����̸� �����Խ��� ������ �̵��ϰ�, ��ȭ�Խ����̸� ��ȭ �Խ��� ������ �̵��ϰ��ϱ�
				 if(boardType.equals("free")) {
					 response.sendRedirect(request.getContextPath()+"/board/boardList.do"); 
				 }else if(boardType.equals("film")) {
					 response.sendRedirect(request.getContextPath()+"/board/boardList_film.do"); 
				 }
				 
			}else {		//�����ϸ�
				System.out.println("�Խñ� �ۼ�����");
				response.sendRedirect(request.getContextPath()+"/board/boardWrite.do");
			}
			
		//�Խñ� ����Ʈ
		}else if(command.equals("/board/boardList.do")) {
			
			String page = request.getParameter("page");
			if(page == null) page ="1";
			int pagex = Integer.parseInt(page);
			
			String keyword = request.getParameter("keyword");
			if(keyword == null)	keyword="";
			//boardList���� searchType�� ����� �ۼ��� �ΰ����� �����Ͽ��µ� ����ڰ� �������� �ʰ� �˻��Ϸ��� �ϸ� �⺻���� �������� �Ѵ�. 	
			String searchType = request.getParameter("searchType");
			if(searchType == null ) searchType="boardTitle";
			

			
			//ó��
			SearchCriteria scri = new SearchCriteria(); 
			scri.setPage(pagex); 		
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			BoardDao bd =new BoardDao();
			int cnt = bd.boardTotal_free(scri); 
			// �Խù� ��ü ���� ��������(�Ű������� scri�� ����ڰ� �˻��� ���ϸ� ������ ����� 10��. �˻��ϸ� �˻��� Ű������ �Խù� ������ŭ�� ��� ������ ���´�.)
			
			PageMaker pm =new PageMaker();
			pm.setScri(scri);		
			pm.setTotalCount(cnt);
			
			
			ArrayList<BoardVo> alist = bd.boardSelectAll(scri);			//������ ó������scri����
			request.setAttribute("alist", alist); //������(�ڿ�) ����.  //�Խ��� �ڿ�����
			request.setAttribute("pm", pm);// ������ ������ ȭ������� ������ �̵��Ѵ�. 
			
			
			//�̵�
			RequestDispatcher rd =request.getRequestDispatcher("/board/boardList.jsp");
			rd.forward(request, response);
			
		
		
		//�Խñ� �Ѱ�����(��ȸ�� �ø��� �޼��嵵 ���� ����)(���� ���� Į�� �� ���� �ؽ���)
		}else if(command.equals("/board/boardContent.do")) { //�ּ�â�� ���ؼ� ������ �ѱ�� get
			
			BoardDao bd =new BoardDao();
			
		//	int flagx = 0;
			
			String flag = request.getParameter("flag");			
			String bidx = request.getParameter("bidx");
			int bidx_=Integer.parseInt(bidx); //boardselectone�̶�� �޼���� �Ű������� int�� �����Ƿ� �������ش�. 
			
			if(flag == null) {
				//��ȸ���ø��� 
				bd.boardHitCnt(bidx_);	
			}// ��õ ��ư�� ������ ������ flag ���� �־ ��ȸ���� �ȿö󰡰Լ���.
			
			
			//�Խñ� �ϳ� ���
			BoardVo bv = bd.boardSelectOne(bidx_);
			//System.out.println("controller bv"+bv);
			
			//��ȸ���ø��� 
			//bd.boardHitCnt(bidx_);
			
			//������ ������
			HashMap<String, Integer> map = bd.bidxPreNext(bidx_);
			//System.out.println("map pre: "+map.get("pre"));
			 
			
			//��� ����Ʈ �ҷ�����
			ReplyDao rpd = new ReplyDao();
			SearchCriteria scri = new SearchCriteria();
			
			
			ArrayList<ReplyVo> alist= rpd.ReplySelectAll(scri, bidx_);
			
			
			// ȭ��� ������
			request.setAttribute("bv", bv);
			request.setAttribute("map", map);
			request.setAttribute("alist", alist);
			
			
			
			System.out.println("�Խñ� �ҷ����� ����");
			
			//�̵�(���� ������ ����Ʈ�� ����)
			RequestDispatcher rd =request.getRequestDispatcher("/board/boardContent.jsp");
			rd.forward(request, response);
	
			
		//�Խñ� ���� ������ �̵�
		}else if(command.equals("/board/boardEdit.do")) {
			
			
			String bidx = request.getParameter("bidx");
			int bidx_=Integer.parseInt(bidx); 
			
			//ó��
			BoardDao bd =new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidx_);
			
			request.setAttribute("bv", bv);// ���������� �ڿ�����(jsp��ܿ� ��� �����Ͱ�����)
			
			RequestDispatcher rd =request.getRequestDispatcher("/board/boardEdit.jsp");
			rd.forward(request, response);
		
		//�Խñ� ����ó��	
		}else if(command.equals("/board/boardEditAction.do")){
			//ȭ��ܿ��� �����ϰ����ϴ� ���� ����,����,�ۼ���,ip, bidx�Ѿ��. bidx�� int������ �ٲ��ֱ�
			
			int sizeLimit = 1024*1024*15; 		// 15�ް� ������ ���ε��ϰڴ�. 
			
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
			
			//�޼�������ؼ� ���� �޼��� ���. �Ű������� 
			BoardDao bd =new BoardDao();
			int value = bd.boardEdit(boardTitle,boardContent,boardFilename, ip, bidx_, boardType);
			
			//���������ϸ�
			if(value==1) {	System.out.println("��������");
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
			}else {	//���� �����ϸ�	
				System.out.println("��������");
				response.sendRedirect(request.getContextPath()+"/board/boardEdit.do?bidx="+bidx);
				// ?�� sql������ ����ϰڴ�?    ������ �����ϸ� �� ���� �ٽ� �����Ϸ� �����ϱ� ������ �����η� �̵��� �ϸ鼭 bidx���� ���� ������ �� �� �ֵ��� �Ѵ�. 
			}
			
		//�Խñ� ����	
		}else if(command.equals("/board/boardDeleteAction.do")) {
			System.out.println("���� Ŀ��� ����");
			
			String bidx =request.getParameter("bidx");
			int bidx_ =Integer.parseInt(bidx);
			//System.out.println(bidx);
			
			BoardDao bd = new BoardDao();
			int value = bd.delete(bidx_);
							
			
			if(value==1) {	System.out.println("��������");
				response.sendRedirect(request.getContextPath()+"/board/boardList.do");
			}else {	
				System.out.println("��������");
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
			}
			
	
		//�Խù� ��õ - content���� ��õ ��ư�� ������ ����
		}else if(command.equals("/board/contentRecommend.do")) {
			
			BoardDao bd = new BoardDao();
			
			String bidx =request.getParameter("bidx");
			int bidx_ =Integer.parseInt(bidx);
			
			// null ������ ���� ������ int ���� �� �̸������ϰ� �޾Ƽ� int ������ �ֱ�
			int flagx = 0;
			String flag = request.getParameter("flag");	
			flagx = Integer.parseInt(flag); 
			//��õ ��ư�� flag 1�ְ�
			if(flagx == 1) {
				//��õī��Ʈ �ø���
				int value = bd.boardLikeCnt(bidx_);	
				
				if(value==1) {	
					System.out.println("��õ!");	//��õ�ؼ� ������ ������ ������ ���� ��ȸ�� �ȿ����� �ϱ�
					response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx+"&flag="+flag);
				}else {	
					System.out.println("��õ�ȉ�");
					response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
				}	
			}
			
		//�α�Խ���
		}else if(command.equals("/board/hitList.do")) {
		
			String page = request.getParameter("page");
			if(page== null) page="1";
			int pagex=Integer.parseInt(page);
			
			//�˻�â�� ���� �Է��ϴ� ���. ���� �ޱ� ���ϸ� null
			String keyword = request.getParameter("keyword");
			if(keyword == null)	keyword="";
			//boardList���� searchType�� ����� �ۼ��� �ΰ����� �����Ͽ��µ� ����ڰ� �������� �ʰ� �˻��Ϸ��� �ϸ� �⺻���� �������� �Ѵ�. 	
			String searchType = request.getParameter("searchType");
			if(searchType == null ) searchType="boardTitle";

			
						
			//ó��
			SearchCriteria scri = new SearchCriteria(); 
			
			scri.setPage(pagex); 		
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			BoardDao bd =new BoardDao();
			int cnt = bd.boardTotal(scri); 
			// �Խù� ��ü ���� ��������(�Ű������� scri�� ����ڰ� �˻��� ���ϸ� ������ ����� 10��. �˻��ϸ� �˻��� Ű������ �Խù� ������ŭ�� ��� ������ ���´�.)
			
			PageMaker pm =new PageMaker();
			pm.setScri(scri);		
			pm.setTotalCount(cnt);
			
			ArrayList<BoardVo> alist = bd.boardHitAll(scri);	//������ ó������scri����
			
			request.setAttribute("alist", alist); //������(�ڿ�) ����.  //�Խ��� �ڿ�����
			request.setAttribute("pm", pm);// ������ ������ ȭ������� ������ �̵��Ѵ�. 
			
			
			//�̵�
			RequestDispatcher rd =request.getRequestDispatcher("/board/hitList.jsp");
			rd.forward(request, response);
			
			
			
			
			
			
			
		//��õ�Խ���
		}else if(command.equals("/board/likeList.do")) {
			
			String page = request.getParameter("page");
			if(page== null) page="1";
			int pagex=Integer.parseInt(page);
			
			//�˻�â�� ���� �Է��ϴ� ���. ���� �ޱ� ���ϸ� null
			String keyword = request.getParameter("keyword");
			if(keyword == null) {	//�˻����ϸ�
				keyword="";
			}
			String searchType = request.getParameter("searchType");
			if(searchType == null ) {	//boardList���� searchType�� ����� �ۼ��� �ΰ����� �����Ͽ��µ� ����ڰ� �������� �ʰ� �˻��Ϸ��� �ϸ� �⺻���� �������� �Ѵ�. 
				searchType="boardTitle";
			}
			
			
			//ó��
			SearchCriteria scri = new SearchCriteria(); 
			
			scri.setPage(pagex); 		
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			BoardDao bd =new BoardDao();
			int cnt = bd.boardTotal(scri); 
			// �Խù� ��ü ���� ��������(�Ű������� scri�� ����ڰ� �˻��� ���ϸ� ������ ����� 10��. �˻��ϸ� �˻��� Ű������ �Խù� ������ŭ�� ��� ������ ���´�.)
			
			PageMaker pm =new PageMaker();
			pm.setScri(scri);		
			pm.setTotalCount(cnt);
			
		
			ArrayList<BoardVo> alist = bd.boardLikeAll(scri);	//������ ó������scri����
			
			request.setAttribute("alist", alist); //������(�ڿ�) ����.  //�Խ��� �ڿ�����
			request.setAttribute("pm", pm);// ������ ������ ȭ������� ������ �̵��Ѵ�. 
			
			System.out.println("cnt: "+cnt);
			System.out.println("alist: "+alist);
			System.out.println("pm: "+pm);
			
			//�̵�
			RequestDispatcher rd =request.getRequestDispatcher("/board/likeList.jsp");
			rd.forward(request, response);
			
		
		
		
		
		//�Խñ�(��ȭ) ����Ʈ
		}else if(command.equals("/board/boardList_film.do")) {
			
			String page = request.getParameter("page");
			if(page== null) page="1";
			int pagex=Integer.parseInt(page);
			
			//�˻�â�� ���� �Է��ϴ� ���. ���� �ޱ� ���ϸ� null
			String keyword = request.getParameter("keyword");
			if(keyword == null) {	//�˻����ϸ�
				keyword="";
			}
			String searchType = request.getParameter("searchType");
			if(searchType == null ) {	//boardList���� searchType�� ����� �ۼ��� �ΰ����� �����Ͽ��µ� ����ڰ� �������� �ʰ� �˻��Ϸ��� �ϸ� �⺻���� �������� �Ѵ�. 
				searchType="boardTitle";
			}
			
			
			//ó��
			SearchCriteria scri = new SearchCriteria(); 
			
			scri.setPage(pagex); 		
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			BoardDao bd =new BoardDao();
			int cnt = bd.boardTotal_film(scri); 
			// �Խù� ��ü ���� ��������(�Ű������� scri�� ����ڰ� �˻��� ���ϸ� ������ ����� 10��. �˻��ϸ� �˻��� Ű������ �Խù� ������ŭ�� ��� ������ ���´�.)
			
			PageMaker pm =new PageMaker();
			pm.setScri(scri);		
			pm.setTotalCount(cnt);
			
			
			ArrayList<BoardVo> alist = bd.boardSelectAll_film(scri);			//������ ó������scri����
			request.setAttribute("alist", alist); //������(�ڿ�) ����.  //�Խ��� �ڿ�����
			request.setAttribute("pm", pm);// ������ ������ ȭ������� ������ �̵��Ѵ�. 
			
			
			//�̵�
			RequestDispatcher rd =request.getRequestDispatcher("/board/boardList_film.jsp");
			rd.forward(request, response);
			
		
		}	
		
	}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
