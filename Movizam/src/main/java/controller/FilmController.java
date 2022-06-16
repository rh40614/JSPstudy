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
			
		//��ȭ ������ ��� 
		String uploadPath = "D:\\open API (A)\\dev\\Movizam\\src\\main\\webapp\\image\\";	
		String saveFolder = "movie";
		String saveFullPath = uploadPath+saveFolder;
			
		
		
		
		
		
		//��ȭ��� ������ ����
		if(command.equals("/film/filmInsert.do")) {
			
			RequestDispatcher rd= request.getRequestDispatcher("/film/filmInsert.jsp");
			rd.forward(request, response);
		
		//��ȭ���		
		}else if(command.equals("/film/filmInsertAction.do")) {  
				
				
			//�������� ������ ��������� ����  -request������� saveFullPath�� �����Ҷ� ���
			int sizeLimit = 1024*1024*15; 		// 15�ް� ������ ���ε��ϰڴ�. 
				
			//cos.jar  �ٿ�޾Ƽ�  lib �� ����
			MultipartRequest multi =null;
				
			multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy()); 
			//DefaultFileRenamePolicy()  :�����̸� �ߺ����� ���ܳ����ʰ� ��å(���� �̸��̸� �ڿ�(1)������)
				
			
	
			String filmCategory =multi.getParameter("filmCategory");
			String filmName =multi.getParameter("filmName");
			String filmDate =multi.getParameter("filmDate");	
			String filmStatus = multi.getParameter("filmStatus");
			String filmDetail = multi.getParameter("filmDetail");
			//String filmPoster = multi.getParameter("filmPoster");
	
			//Enumeration �޼ҵ�� ���� ������ �ִ� Ŭ���� //�����ڿ� ����� ������ ��� ��ü�� �����Ѵ�. 
			Enumeration files  = multi.getFileNames();
			//��� ���� ��ü�� ���� ��´�. 
			String file = (String)files.nextElement();
			//�����̸� �̾Ƴ���(��񿡴� �����̸����� �����Ѵ�. filmPoster �� �ޱ�) 
			String filmPoster= multi.getFilesystemName(file);		//����Ǵ� �����̸�
			
			HttpSession session = request.getSession();
			int midx= (int)session.getAttribute("midx");	//�α����ؼ� ��ȭ�� ����� �������� midx
				
			//���
			FilmDao fd = new FilmDao();
			
			//�Ű������� ȭ����� �����޾Ƽ� ��� �Է��ϱ�.//������ �̸����޴°�
			int value = fd.filmInsert(filmCategory, filmName, filmDate, filmStatus, filmDetail, filmPoster, midx);
			System.out.println("value: "+ value);
				if(value==1) {	//��ȭ��� ����
					System.out.println("��ȭ��ϼ���");
					response.sendRedirect(request.getContextPath()+"/film/filmInsert.jsp");
				}else {		//�����ϸ�
					System.out.println("��ȭ��Ͻ���");
					response.sendRedirect(request.getContextPath()+"/film/filmInsert.do");
				}


			//��ȭ���	//��ȭ��� �����ϸ� ����¡ ó���ϱ�
			}else if(command.equals("/film/filmList.do")) {
				
				FilmDao fd = new FilmDao();
				
				//�ʱ� ������ �Ķ���ʹ� null�̸� 1�� ����ش�. 			
				String page =request.getParameter("page");
				if(page == null) {page= "1";}
				int pagex =Integer.parseInt(page);
				
				//Ű���� ���� ��� ��. ��ġ Ÿ���� ȸ���̸��� �⺻������ ����
				String keyword =request.getParameter("keyword");
				String searchType =request.getParameter("searchType");
				if(keyword == null) keyword= "";
				if(searchType == null) searchType= "filmName";
				
				//�������� �˻��� �Ѵ� �ʱ�ȭ�����ؾ������� SearchCreteria ����
				SearchCriteria scri = new SearchCriteria();
				scri.setPage(pagex);
				scri.setKeyword(keyword);
				scri.setSearchType(searchType);
				
				//����¡�����Ϸ��� ��ȸ���� �� �ʿ�
				//(�Ű������� scri�� ����ڰ� �˻��� ���ϸ� ������ ����� 10������. �˻��ϸ� �˻��� Ű������ �Խù� ������ŭ�� ��� ������ ���´�.)
				int cnt = fd.filmTotal(scri);
				
				PageMaker pm = new PageMaker();
				//�˻������� ���ǿ� �´� ȸ���� ���� ����¡
				pm.setScri(scri);
				//�� ȸ���� ���� �޾Ƽ� ����¡
				pm.setTotalCount(cnt);
				
				
				//��ȭ��� ������ ����
				ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
				
				alist = fd.FilmSelectAll(scri);
				
				request.setAttribute("alist", alist);
				request.setAttribute("pm", pm);
				//System.out.println("alist: "+ alist);
				
				RequestDispatcher rd= request.getRequestDispatcher("/film/filmList.jsp");
				rd.forward(request, response);
			
				
				
			//��ȭ ���� �� view
			}else if(command.equals("/film/filmView.do")) {
				
				//��� �޾ƿ��°�? ��ũ�ɶ� �Ķ���� �ɾ ������!
				String fidx = request.getParameter("fidx");
				//System.out.println(fidx);
				int fidx_ = Integer.parseInt(fidx);
				
				FilmDao fd = new FilmDao();
				FilmVo fv = new FilmVo();
				
				fv = fd.FilmSelectOne(fidx_);
				
				request.setAttribute("fv", fv);
				
				RequestDispatcher rd= request.getRequestDispatcher("/film/filmView.jsp");
				rd.forward(request, response);
			
			//��ȭ����ȭ�� ����
			}else if(command.equals("/film/filmEdit.do")) {
				

				//��� �޾ƿ��°�? ��ũ�ɶ� �Ķ���� �ɾ ������!
				String fidx = request.getParameter("fidx");
				int fidx_ = Integer.parseInt(fidx);
				
				FilmDao fd = new FilmDao();
				FilmVo fv = new FilmVo();
				
				fv = fd.FilmSelectOne(fidx_);
				
				request.setAttribute("fv", fv);
				
				RequestDispatcher rd= request.getRequestDispatcher("/film/filmEdit.jsp");
				rd.forward(request, response);
				
				
			//��ȭ���� ó��	
			}else if(command.equals("/film/filmEditAction.do")) {
				
				
				//��ȭ������	
				int sizeLimit = 1024*1024*15; 		// 15�ް� ������ ���ε��ϰڴ�. 
					
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
				
				//�̸����� �����ͼ� ���
				Enumeration files  = multi.getFileNames();
				//��� ���� ��ü�� ���� ��´�. 
				String file = (String)files.nextElement();
				//�����̸� �̾Ƴ���(��񿡴� �����̸����� �����Ѵ�.)
				String filmPoster= multi.getFilesystemName(file);		//����Ǵ� �����̸�
				
				
				FilmDao fd = new FilmDao();
				int value = fd.filmEdit(fidx_, filmCategory, filmName, filmDate, filmStatus, filmDetail, filmPoster);
				
				if(value==1) {	
					System.out.println("��ȭ��������");
					response.sendRedirect(request.getContextPath()+"/film/filmView.do?fidx="+fidx);	//�ش�Ǵ� �Խù��� �����ߴ����� Ȯ���Ϸ����°������� fidx �ѱ��
				}else {		
					System.out.println("��ȭ��������");
					response.sendRedirect(request.getContextPath()+"/film/filmEdit.do?fidx="+fidx);
				}
			
				
			//��ȭ����	
			}else if(command.equals("/film/filmDelete.do")) {		//�����Ͻðڽ��ϱ� Ȯ��â ����
				
				String fidx = request.getParameter("fidx");	
				int fidx_ = Integer.parseInt(fidx);
				
				FilmDao fd =new FilmDao();
				int value = fd.filmDelete(fidx_);
				
				if(value==1) {	
					System.out.println("��ȭ ���� ����");
					response.sendRedirect(request.getContextPath()+"/film/filmList.do");
				}else {		
					System.out.println("��ȭ ���� ����");
					response.sendRedirect(request.getContextPath()+"/film/filmList.do");
				}
		
			//��ȭ������ ����
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
