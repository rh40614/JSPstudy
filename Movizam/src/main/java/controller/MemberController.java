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
		
		//ȸ���̹��� ��� 
		String uploadPath = "D:\\open API (A)\\dev\\Movizam\\src\\main\\webapp\\image\\";	
		String saveFolder = "profile";
		String saveFullPath = uploadPath+saveFolder;
		
		
		
		//ȸ������
		if(command.equals("/member/memberJoinAction.do")) {
			
			request.setCharacterEncoding("UTf-8");
			
			//ȸ������ ������ �Ѱܹ޴� ������
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
			
				System.out.println("ȸ�����Լ���");
						
			}else{
				response.sendRedirect(request.getContextPath()+"/member/memberJoin.do");
				System.out.println("ȸ�����Խ���");
			}
			
		
		}else if(command.equals("/member/memberJoin.do")) {
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberJoin.jsp");
			rd.forward(request, response);
			
		}else if(command.equals("/member/memberLogin.do")) {
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberLogin.jsp");
			rd.forward(request, response);
		
		}
		
		//�α���
		else if(command.equals("/member/memberLoginAction.do")){
			
			String memberId = request.getParameter("memberId");
			String memberPwd = request.getParameter("memberPwd");
			String memberIp = InetAddress.getLocalHost().getHostAddress();
			
			MemberDao md = new MemberDao();
			MemberVo mv =md.memberLogin(memberId, memberPwd);
			
			//---�α��� ����----
			///ȯ���޼���
			String message=md.randomGreetingMessage();
			
			//��������
			HttpSession session = request.getSession(); 
			if(mv!= null) {
				session.setAttribute("midx", mv.getMidx());
				session.setAttribute("memberId",mv.getMemberId());
				session.setAttribute("memberPwd",mv.getMemberPwd());
				session.setAttribute("memberNickname", mv.getMemberNickname());
				session.setAttribute("adminYN", mv.getAdminYN());
				//ȸ���̹��� 
				session.setAttribute("memberImage", mv.getMemberImage());
				//ȯ���λ絵 ���� ��ư���
				session.setAttribute("message", message);
			
				//�α������� �Է�
				int midx= (int)session.getAttribute("midx");
				System.out.println("��");
				
				int loginInfo = md.loginInfoInsert(memberIp, midx);
				
				if(loginInfo == 1) {
					System.out.println("�α������� �Է¿Ϸ�");
				
				}else {
					System.out.println("�α������� �Է½���");
				}
				
				//�α��ν��а� Ŭ����
//				md.clearLoginFailCount(memberId);
				
			
			
//			System.out.println("saveURI: "+session.getAttribute("saveURI"));
//			System.out.println("nickname session :"+session.getAttribute("memberNickname"));
//			System.out.println("message :"+message);
//			System.out.println("message session :"+session.getAttribute("message"));
				
			//System.out.println(session.getAttribute("saveURI"));
				
				//session ���� ������ �°����� ���ư���
				if(session.getAttribute("saveURI")!= null){
					
					response.sendRedirect((String)session.getAttribute("saveURI"));
					
					
				}else {//Ȩ���������� �α����ѰŸ� Ȩ�������� ����(�ε��� �������� jsp�̱⶧���� �����͸� ������ sendredirect�� �̵��� ������.  �����ּ� ����ؼ� �̵��ϱ�)
					response.sendRedirect(request.getContextPath()+ "/main/main.do");
				}
			
				
				
			}else {System.out.println("�α��� ����");
			//�α��� �����ϸ� ī��Ʈ �ø��� �ٽ� �α����������� �̵�
			
			//int loginfail = md.plusLoginFailCount(memberId);
//			if(loginfail == 1) {
//				System.out.println("�α��ν��� ī��Ʈ ��");
//			
//			}else {
//				System.out.println("�α��� ����ī��Ʈ ����");
//			}
//			md.loginLock(memberId);
			response.sendRedirect(request.getContextPath()+ "/member/memberLogin.do");
			
			}
			
	
		//�α׾ƿ�
		}else if(command.equals("/member/memberLogout.do")) {
			
			HttpSession session = request.getSession();
			session.invalidate(); //������ ��� ��������

			response.sendRedirect(request.getContextPath()+ "/main/main.do");
		
		//ȸ������ã�� ȭ��
		}else if(command.equals("/member/memberInfo_search.do")){
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberInfo_search.jsp");
			rd.forward(request, response);
		//ȸ������ ã��	
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
		
			
		//ȸ��Ż�� 
		}else if(command.equals("/member/memberDelete.do")) {

			RequestDispatcher rd = request.getRequestDispatcher("/member/memberDelete.jsp");
			rd.forward(request, response);
			
		//ȸ��Ż�𼺰� Ȯ�� ������
		}else if(command.equals("/member/memberDeleteConfirm.do")) {
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberDeleteConfirm.jsp");
			rd.forward(request, response);
							
		//ȸ��Ż�� ó��	
		}else if(command.equals("/member/memberDeleteAction.do")) {
			
			String memberId = request.getParameter("memberId");
			String memberPwd = request.getParameter("memberPwd");
			
			MemberDao md = new MemberDao();
			int value = md.memberDelete(memberId, memberPwd);
			
			if(value == 1 ) {
				HttpSession session = request.getSession();
				session.invalidate(); //������ ��� ��������
				
				response.sendRedirect(request.getContextPath()+"/member/memberDeleteConfirm.do"); 	
				
				System.out.println("ȸ��Ż�� ����");
			}else {
				response.sendRedirect(request.getContextPath()+"/member/memberDelete.do");
				System.out.println("ȸ��Ż�����");
				
			}
		
		
		//ȸ����������
		}else if(command.equals("/member/memberInfo.do")) {

			MemberDao md =new MemberDao();
			MemberVo mv = new MemberVo();

			//�̹� �α��ε� ���������� ���ǿ��� midx ��������
			HttpSession session = request.getSession();
			int midx= (int)session.getAttribute("midx");
			
			mv = md.memberSelectOne(midx);
			
			request.setAttribute("mv", mv);
			
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberInfo.jsp");
			rd.forward(request, response);	
			
		//ȸ������ ������������ �̵� 	
		}else if(command.equals("/member/memberEdit.do")) {
			
			MemberDao md =new MemberDao();
			MemberVo mv = new MemberVo();

			
			//�̹� �α��ε� ���������� ���ǿ��� midx ��������
			HttpSession session = request.getSession();
			int midx= (int)session.getAttribute("midx");
			
			mv = md.memberSelectOne(midx);
			
			request.setAttribute("mv", mv);
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberEdit.jsp");
			rd.forward(request, response);
			
			
		//ȸ������ �����ϱ� 	
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
			
			String memberImage= multi.getFilesystemName(file); //ȸ���̹���
			
			//String midx = request.getParameter("midx"); //midx�� ������ session���� �޾ƿ� 
			
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
				System.out.println("ȸ������ ��������");
				response.sendRedirect(request.getContextPath()+"/member/memberInfo.do");
			}else{
				System.out.println("ȸ������ ��������");
				response.sendRedirect(request.getContextPath()+"/member/memberEdit.do");
			
			}	
			
			
			
			
		//ȸ�����
		}else if(command.equals("/member/memberList.do")) {
			
			MemberDao md =new MemberDao();
			
			//�ʱ� ������ �Ķ���ʹ� null�̸� 1�� ����ش�. 			
			String page =request.getParameter("page");
			if(page == null) {page= "1";}
			int pagex =Integer.parseInt(page);
			
			//Ű���� ���� ��� ��. ��ġ Ÿ���� ȸ���̸��� �⺻������ ����
			String keyword =request.getParameter("keyword");
			String searchType =request.getParameter("searchType");
			if(keyword == null) keyword= "";
			if(searchType == null) searchType= "memberName";
			
			//�������� �˻��� �Ѵ� �ʱ�ȭ�����ؾ������� SearchCreteria ����
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(pagex);
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			//����¡�����Ϸ��� ��ȸ���� �� �ʿ�
			//(�Ű������� scri�� ����ڰ� �˻��� ���ϸ� ������ ����� 10������. �˻��ϸ� �˻��� Ű������ �Խù� ������ŭ�� ��� ������ ���´�.)
			int cnt = md.MemberTotal(scri);
			
			PageMaker pm = new PageMaker();
			//�˻������� ���ǿ� �´� ȸ���� ���� ����¡
			pm.setScri(scri);
			//�� ȸ���� ���� �޾Ƽ� ����¡
			pm.setTotalCount(cnt);
			
			
			//ȸ������ ������ ����
			ArrayList<MemberVo> alist =md.memberSelectAll(scri);
			
			request.setAttribute("alist", alist);
			request.setAttribute("pm", pm);
			//System.out.println("pm share: "+pm);
			//System.out.println("alist: " + alist);
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberList.jsp");
			rd.forward(request, response);
			
			
			
	//ȸ����������(������)
	}else if(command.equals("/member/memberInfoAdmin.do")) {

		MemberDao md =new MemberDao();
		MemberVo mv = new MemberVo();

		//�������� midx �� �ƴ϶� ȸ���� ������ ȸ���� �ش��ϴ� ���� �����ϱ� 
		//HttpSession session = request.getSession();
		//int midx= (int)session.getAttribute("midx");
		
		String midx = request.getParameter("midx");
		int midxx = Integer.parseInt(midx);
		
		mv = md.memberSelectOne(midxx);
		
		request.setAttribute("mv", mv);
		
		
		RequestDispatcher rd = request.getRequestDispatcher("/member/memberInfoAdmin.jsp");
		rd.forward(request, response);	
		
	//ȸ������ ������������ �̵� (������)
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
		
		
	//ȸ������ �����ϱ� (������)	
	}else if(command.equals("/member/memberEditAdminAction.do")) {	
		
		MemberDao md =new MemberDao();
		
		MultipartRequest multi =null;
		  
		int sizeLimit = 1024*1024*15;
		 
		multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy());
		 
		Enumeration files = multi.getFileNames(); 
		String file =(String)files.nextElement();
		
		// ȸ���� �����ϴ� �Ͱ� �����ڰ� �����ϴ� ���� midx ���� �ٸ��Ƿ� ���� ����
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
		
		String memberImage= multi.getFilesystemName(file); //ȸ���̹���
		
	
		
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
			System.out.println("ȸ������ ��������");
			response.sendRedirect(request.getContextPath()+"/member/memberInfoAdmin.do?midx="+midxx);
		}else{
			System.out.println("ȸ������ ��������");
			response.sendRedirect(request.getContextPath()+"/member/memberEditAdmin.do?midx="+midxx);
		
		}	
		
			
			
		
	}else if(command.equals("/member/memberAdminOn.do")) {
		
		String midx = request.getParameter("midx");
		
		MemberDao md = new MemberDao();
		
		int value = md.AdminYNon(Integer.parseInt(midx));
		
		
		if(value==1) {
			System.out.println("�����ڷ� ���� ����Ǿ����ϴ�.");
			response.sendRedirect(request.getContextPath()+"/member/memberList.do");
		}else{
			System.out.println("���� ���� ����");
			response.sendRedirect(request.getContextPath()+"/member/memberList.do");
		
		}
		
	//������ ���� ����
	}else if(command.equals("/member/memberAdminOff.do")) {
		
		String midx = request.getParameter("midx");
		
		MemberDao md = new MemberDao();
		
		int value = md.AdminYNoff(Integer.parseInt(midx));
		
		
		if(value==1) {
			System.out.println("�Ϲ�ȸ������ ������ ����Ǿ����ϴ�.");
			response.sendRedirect(request.getContextPath()+"/member/memberList.do");
		}else{
			System.out.println("���� ���� ����");
			response.sendRedirect(request.getContextPath()+"/member/memberList.do");
		
		}	
	}
		
			
			
			
				
		
	}
			
	
			
			  
			
			
			
			
		
		
		
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
