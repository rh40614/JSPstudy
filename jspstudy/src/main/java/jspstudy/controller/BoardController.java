
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
			//������Ʈ �̸��� �� ������ ������ ����.substring���� ���س��� pj��ŭ�� uri���� �ڸ�
			String command = uri.substring(pj.length());
			System.out.println("command:"+command);
			
			//���� ��� 
			String uploadPath = "D:\\open API (A)\\dev\\jspstudy\\src\\main\\webapp\\";	//����������
			String saveFolder = "images";
			String saveFullPath = uploadPath+saveFolder;
			
			
			if(command.equals("/board/boardWrite.do")) {
				
				RequestDispatcher rd= request.getRequestDispatcher("/board/boardWrite.jsp");// ��¥ ��� �����ϱ�
				rd.forward(request, response);
				
			}else if(command.equals("/board/boardWriteAction.do")) { // ���������� ���۹�ư�� ������ ���� �Ѱ� �ޱ�.�׸��� ��� �ø��� 
				
				// 1. ȭ��ܿ��� ��(�Ķ����)�� �Ѱܹޱ� 
				
				//�������� ������ ����(�������)  -request������� saveFullPath�� �����Ҷ� ���
					
				int sizeLimit = 1024*1024*15; 		// 15�ް� ������ ���ε��ϰڴ�. 
				
				MultipartRequest multi = null; 		//�ΰ��� Ÿ���� �ѱ������ (String�̶� ���̳ʸ� Ÿ��(������ �޴� Ÿ��) )
				multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy()); 
				//request�� ����� ����ϰڴ�. (���� �ְ�ޱ�)// saveFullPath�� ����Ǵ� ��θ� �����Ѵ�. //������ ���� //�����̸� �ߺ����� ���ܳ����ʰ� ��å(���� �̸��̸� �ڿ�(1)������)
				
//�Ķ���͹ޱ� 	//�Ķ���͸�  request���� multi Ÿ������ �ޱ�
				String subject =multi.getParameter("subject");
				String content =multi.getParameter("content");
				String writer =multi.getParameter("writer");
				
				//Enumeration �޼ҵ�� ���� ������ �ִ� Ŭ���� //�����ڿ� ����� ������ ��� ��ü�� �����Ѵ�. 
				Enumeration files  = multi.getFileNames();
				//��� ���� ��ü�� ���� ��´�. 
				String file = (String)files.nextElement();
				//�����̸� �̾Ƴ���(��񿡴� �����̸����� �����Ѵ�.)
				String fileName= multi.getFilesystemName(file);		//����Ǵ� �����̸�
				//String originFileName = multi.getOriginalFileName(file);	//��¥ ���� �̸� ����
	
				String ip =InetAddress.getLocalHost().getHostAddress();	
					
				
				HttpSession session = request.getSession();
				int midx= (int)session.getAttribute("midx");	//�α��� �ѻ���� ����
				
			//2. 1������ ������ ���� ��� ���
			BoardDao bd =new BoardDao();
			//�Ű������� ȭ����� �����޾Ƽ� ��� �Է��ϱ�.//������ �̸����޴°�
			int value = bd.insertBoard(subject, writer, content, ip, midx, fileName);
			
				if(value==1) {	//�Խñ� �ۼ��� �����ϸ�
					response.sendRedirect(request.getContextPath()+"/index.jsp");
				}else {		//�����ϸ�
					System.out.println("�Խñ� �ۼ�����");
					response.sendRedirect(request.getContextPath()+"/board/boardWrite.do");
				}


			
			}else if(command.equals("/board/boardList.do")) {
				
				
	//			//boardselectall�� page�Ѱ��ֱ�
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
					searchType="subject";
				}
				
				
				//ó��
				SearchCriteria scri = new SearchCriteria(); 
				// ����¡ ó������������ ũ�����׸��Ƹ� ����Ǵµ� �̰��� ��ӹ��� SearchCriteria�� �� ū ����� �����ϰ� �������� SearchCriteria�� �ٲپ��ش�. 
				scri.setPage(pagex); 		
				scri.setKeyword(keyword);
				scri.setSearchType(searchType);
				
				BoardDao bd =new BoardDao();
				int cnt = bd.boardTotal(scri); 
				// �Խù� ��ü ���� ��������(�Ű������� scri�� ����ڰ� �˻��� ���ϸ� ������ ����� 10��. �˻��ϸ� �˻��� Ű������ �Խù� ������ŭ�� ��� ������ ���´�.)
				
				PageMaker pm =new PageMaker();
				pm.setScri(scri);		
				pm.setTotalCount(cnt);
				
				
				ArrayList<BoardVo> alist = bd.boardSelectAll(scri);			//������ ó������scri����
				request.setAttribute("alist", alist); //������(�ڿ�) ����.  //�Խ��� �ڿ�����
				request.setAttribute("pm", pm);// ������ ������ ȭ������� ������ �̵��Ѵ�. 
				
				System.out.println("����Ʈ �ҷ����� ����");
				
				//�̵� (���� ó���ϰ� �̵��ؾ���)
				RequestDispatcher rd =request.getRequestDispatcher("/board/boardList.jsp");
				rd.forward(request, response);
				
				
			}else if(command.equals("/board/boardContent.do")) { //�ּ�â�� ���ؼ� ������ �ѱ�� get
				System.out.println("����Ʈ �ҷ����� ����");
				//1. �Ķ���� �Ѿ��
				String bidx = request.getParameter("bidx");
				int bidx_=Integer.parseInt(bidx); //boardselectone�̶�� �޼���� �Ű������� int�� �����Ƿ� �������ش�. 
				
				//2. ó����(�����Ͱ� ������ ȭ��ܿ� ��������)
				BoardDao bd =new BoardDao();
				BoardVo bv = bd.boardSelectOne(bidx_);
				
				request.setAttribute("bv", bv);// ���������� �ڿ�����(jsp��ܿ� ��� �����Ͱ�����)
				
				
				//3. �̵��� (���� ������ ����Ʈ�� ����)
				RequestDispatcher rd =request.getRequestDispatcher("/board/boardContent.jsp");
				rd.forward(request, response);
				
			}else if(command.equals("/board/boardModify.do")) {
				System.out.println("�Խñ� �����ϱ�");
				//1. �Ķ���� �Ѿ��
				String bidx = request.getParameter("bidx");
				int bidx_=Integer.parseInt(bidx); //boardselectone�̶�� �޼���� �Ű������� int�� �����Ƿ� �������ش�. 
				
				//2. ó����(�����Ͱ� ������ ȭ��ܿ� ��������)
				BoardDao bd =new BoardDao();
				BoardVo bv = bd.boardSelectOne(bidx_);
				
				request.setAttribute("bv", bv);// ���������� �ڿ�����(jsp��ܿ� ��� �����Ͱ�����)
				//������η� ����
				RequestDispatcher rd =request.getRequestDispatcher("/board/boardModify.jsp");
				rd.forward(request, response);
				
			}else if(command.equals("/board/boardModifyAction.do")){
				//ȭ��ܿ��� �����ϰ����ϴ� ���� ����,����,�ۼ���,ip, bidx�Ѿ��. bidx�� int������ �ٲ��ֱ�
				String subject = request.getParameter("subject");
				String content = request.getParameter("content");
				String writer = request.getParameter("writer");
				String ip =InetAddress.getLocalHost().getHostAddress();
				String bidx = request.getParameter("bidx");
				
				int bidx_=Integer.parseInt(bidx); 
				
				//�޼�������ؼ� ���� �޼��� ���. �Ű������� 
				BoardDao bd =new BoardDao();
				int value = bd.modify(subject,content,writer,ip, bidx_);
				
				//���������ϸ�
				if(value==1) {	System.out.println("��������");
					response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
				}else {	//���� �����ϸ�	
					System.out.println("���� ����");
					response.sendRedirect(request.getContextPath()+"/board/boardModify.do?bidx="+bidx);
					// ?�� sql������ ����ϰڴ�?    ������ �����ϸ� �� ���� �ٽ� �����Ϸ� �����ϱ� ������ �����η� �̵��� �ϸ鼭 bidx���� ���� ������ �� �� �ֵ��� �Ѵ�. 
				}
				
				
			}else if(command.equals("/board/boardDelete.do")) {
				
				//content���� ������ư�� ������ �ش� bidx�� �Բ� �Ѿ�´�. 
				//bidx���� �ޱ� ���� getParameter�� �ް� setAttribute�� ���ΰ����ؼ� delete���������� �ٽ� ����� ���ֵ����Ѵ�. 
				String bidx= request.getParameter("bidx"); //parameter�� �ѱ涧�� �� String
				request.setAttribute("bidx", bidx);
								
				RequestDispatcher rd = request.getRequestDispatcher("/board/boardDelete.jsp");
				rd.forward(request, response);
				
				
			}else if(command.equals("/board/boardDeleteAction.do")) {
				
				String bidx =request.getParameter("bidx");
				 int bidx_ =Integer.parseInt(bidx);
				 
				BoardDao bd = new BoardDao();
				int value = bd.delete(bidx_);
								
				
			if(value==1) {	System.out.println("��������");
				response.sendRedirect(request.getContextPath()+"/board/boardList.do");
			}else {	
				System.out.println("��������");
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
			}
				
				
			}else if(command.equals("/board/boardReply.do")) {
				//�亯 ��ư�� ������ ���ۿ� ���� �亯�� �ޱ�����
				//���ۿ����� �����ޱ�.� ������
				String bidx =request.getParameter("bidx");
				String originbidx = request.getParameter("originbidx");
				String depth = request.getParameter("depth");
				String level_ = request.getParameter("level_");
				//���ۿ� ���� ������ ��� boardSelectOne�� ���� ���� ����
				BoardDao bd =new BoardDao();
				//�亯�� ���� ���� ���� �����ֱ� 
				BoardVo bv = bd.boardSelectOne(Integer.parseInt(bidx));
				//���ΰ����� ���ؼ� �ι�° bean�� ���� 
				BoardVo bv2 = new BoardVo();
				//����ؾ��ϴ� ������ ������ �޼��带 ���ذ����� bv���� ������ bv2�� ����ֱ�
				bv2.setSubject(bv.getSubject());	
				bv2.setContent(bv.getContent());
				bv2.setWriter(bv.getWriter());
				//������ ���������� ���� ���� ǥ���ϱ����� �޾ƿ°� bv2�� ���(Ŭ�����״� �Ⱥ��̰� ǥ���ؾ���)
				bv2.setBidx(Integer.parseInt(bidx));		//�������ϴµ� int������ �ٲ��ֱ� 
				bv2.setOriginbidx(Integer.parseInt(originbidx));
				bv2.setDepth(Integer.parseInt(depth));
				bv2.setLevel_(Integer.parseInt(level_));
				
				//���������� �����ϱ�
				request.setAttribute("bv2", bv2);
				
				RequestDispatcher rd = request.getRequestDispatcher("/board/boardReply.jsp");
				rd.forward(request, response);
						
			}else if(command.equals("/board/boardReplyAction.do")) {
				//ȭ��ܿ��� �亯���� �Է��� ���� �޾ƿ���
				String bidx = request.getParameter("bidx");
				String originbidx = request.getParameter("originbidx");
				String depth = request.getParameter("depth");
				String level_ = request.getParameter("level_");
				String subject = request.getParameter("subject");
				String content = request.getParameter("content");
				String writer = request.getParameter("writer");
				String ip = InetAddress.getLocalHost().getHostAddress();
				HttpSession session = request.getSession();			//�α��� �� ����� �亯�� �� ���ִ�.
				int midx= (int)session.getAttribute("midx");
			
				//boardReply()�� �Ű������� Vo Ÿ�����Ƿ� �޼����� ���ڰ����� �������� �����ؼ� ȭ�鿡�� ���� �� ��� �ֱ�
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
				
				if(value == 1) {	System.out.println("�亯�ۼ��Ϸ�");
					response.sendRedirect(request.getContextPath()+"/board/boardList.do");
				}else{	System.out.println("�亯�ۼ� ����");
					response.sendRedirect(request.getContextPath()+"/board/boardContext.do?bidx="+bidx);
				}
				
				
				
				
			}else if(command.equals("/board/fileDownload.do")) {
				
				//�����̸� �Ѱܹޱ�
				String fileName = request.getParameter("fileName");
				//������ ��ü ��� 
				String filePath = saveFullPath + File.separator+fileName; //separator�� ������ 
				//�ش� ��ġ�� �ִ� ������ �о�´�. 
				FileInputStream fileInputStream = new FileInputStream(filePath);
				//������� �̾Ƽ� 
				Path source = Paths.get(filePath);
				//mimeType�� ������ ������ ��ȯ�ϱ� ���� Ÿ��. �̸��� ��� ������ �Բ� �����Ҷ� ������ �ؽ�Ʈ�� ��ȯ�ؼ� �����ϱ����� ����. ����� �پ��� Ÿ���� ������ ��ȯ�ϴµ� ���
				String mimeType = Files.probeContentType(source);
				//��� ������ ������ ���������� ��´�.
				response.setContentType(mimeType);
				//���ڵ�
				String sEncoding = new String(fileName.getBytes("utf-8"));
				//��������� �����̸��� ��´�. 
				response.setHeader("Content-Disposition","attatchment;filename"+ sEncoding); //attatchment;filename ÷�������� �̸���~
				//���� ����(���)
				ServletOutputStream servletOutStream = response.getOutputStream();
				//������ �о����� 
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
