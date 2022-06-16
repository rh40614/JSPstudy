package jspstudy.controller;

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

import jspstudy.domain.MemberVo;
import jspstudy.service.MemberDao;


@WebServlet("/MemberController") // �������� .do��� �ϴ°��� ��� // ��Ĺ������ .jsp�� ó�������� ������ .do Ȯ���ڷε����� ���� �� Ŭ���� memberCONTROLLER�� �ؼ��ϼ����ϰ���. 
//�����δ� �������ƴ� ������ ó���ϴ� ��
public class MemberController extends HttpServlet { 	//httpServlet�� ��ӹ޾Ƽ� �ڹ� ���� ��ȭ���� ����� �� �ֵ�����
	private static final long serialVersionUID = 1L;
    
   

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//�ּ��� full��θ� �����ϴ� �޼���
		String uri = request.getRequestURI();
		//������Ʈ �̸� ���� 
		String pj =request.getContextPath();
		//������Ʈ �̸��� �� ������ ������ ����.substring���� ���س��� pj��ŭ�� uri���� �ڸ�
		String command = uri.substring(pj.length());
		System.out.println("command:"+command);
		
		
		
		//�����ΰ� command�Ͱ����� ����
		if(command.equals("/member/memberJoinAction.do")) {	
				
			//PrintWriter out =response.getWriter();
			
			//memberJoinOk.jsp�� ����� ������
			request.setCharacterEncoding("UTf-8");
			//�� �޾ƿ���
			String memberId =request.getParameter("memberId");//html ���� input�� ��Ƶ� ���� ��� �ִ� ��ü�� �̸��� ���ش�. 
			String memberpwd =request.getParameter("memberpwd");
			String memberName =request.getParameter("memberName");
			String memberPhone =request.getParameter("memberPhone");
			String memberJumin =request.getParameter("memberJumin");
			String memberAdress =request.getParameter("memberAdress");
			String memberemail =request.getParameter("memberemail");
			String memberGender =request.getParameter("memberGender");
			//�������� ���� ���� ��ü�� �̸��� ȣ���ϸ� �迭������ ���������Ѵ�. getParameterValues ���
			String[] memberHobby =request.getParameterValues("memberHobby"); //�ߺ������� �����Ѱ͵��� �迭�� �ޱ�
			//hobby�� ���ÿ� �������� �޾Ƽ� ��������� �ݺ����� ����ϰ�, hobby�ϴ� ������ ����� ������ �迭�� �ݺ��� ���� ��Ƽ� �ݺ��� �� �ֵ����Ѵ�.
			String hobby="";
			for(int i = 0; i<memberHobby.length; i++){
				hobby +=","+memberHobby[i];
				//out.println(memberHobby[i]+"<br>");
			}hobby =hobby.substring(1);
		//	out.println(hobby);
			//InetAdress Ŭ������ ȣ��Ʈ ������ �ҷ����� ��ɿ� �ּҸ� �̴� ���
			String ip = InetAddress.getLocalHost().getHostAddress();
			
			
			
			
			//���ᰴü�� ���ڿ� ����ȭ �Ҽ��ֵ��� dao ����
			MemberDao md = new MemberDao();	
			
			int value = md.insertMember( memberId, memberpwd,  memberName, memberemail, memberGender, memberAdress, memberPhone, memberJumin, hobby, ip);
			
		//	out.println(value);  //1�� �߸� 1���� ����. ������ ���̽��ȿ� ���� ���� �ȴ�. 
			//1���� �߸鼭 ��� �����Ͱ� �ö󰡸� ȸ�������� �Ϸ�Ȱ��̹Ƿ� ȸ�������� �Ϸ�Ǹ� ������������ �ű� �� �ֵ����Ѵ�. //�����ϸ� �ٽ� �ϵ��� �̵�.
			
			
			if (value==1){
				response.sendRedirect(request.getContextPath()+"/member/memberList.do"); 	
				//sendredirect�� ��û�� ������ Ư�� url�ΰ��� ������������� �ٽ� ������ �ٽ� ��û�̰��� �����ϴ� ���
				//out.println("<script>alert('ȸ�������� �Ϸ�Ǿ����ϴ�.'); //location.href='"+request.getContextPath()+"/index.jsp'</script>");
			}else{
				response.sendRedirect(request.getContextPath()+"/member/memberJoin.do");
				//out.println("<script>alert('ȸ�����Խ���'); location.href='./memberJoin.jsp'</script>");
			}
		
		}else if (command.equals("/member/memberJoin.do")) {
				//ȸ������ �������� ������ ó����
			System.out.println("test");
			//�Ѿ�� �������� �̵���Ű�� ����� �޼������ϳ�. �����η� �Ѿ�ͼ� ���������δ� ������������ ��Ÿ���Բ� �������ִ°�
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberJoin.jsp");
			rd.forward(request, response); //forward ����� �ܺ�(Ŭ���̾�Ʈ)�� �𸣰� ���ο��� �̵��ϴ� ���. Ŭ��� �����ּҸ� ����Ű�� forward�� �̿��ؼ� �����ּҸ� ��¥�ּҷ� ����Ű�� �Ѵ�. 
				
		}else if(command.equals("/member/memberList.do")) {
				
			//memberList.jsp�ִ� ��ü ������ ����� �����´�. ���ϸ� ���̱� ���� 
			MemberDao md = new MemberDao(); 
			ArrayList<MemberVo> alist =md.memberSelectAll();
			System.out.println(alist);
			//alist�� ���� ���� ȭ�鿡 ����ϱ�
			request.setAttribute("alist",alist);
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberList.jsp");
			rd.forward(request, response); 
		
			//�α���
		}else if(command.equals("/member/memberLogin.do")) {
			//�α��� �������� ����
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberLogin.jsp");
			rd.forward(request, response); 
			
		}else if(command.equals("/member/memberLoginAction.do")) {
			//1. �Ѿ�� ���� �޴´�. 
			String memberId = request.getParameter("memberId"); //input�� name������ �ޱ�
			String memberPwd = request.getParameter("memberPwd");
			
			//2. ó���ϱ� 
			MemberDao md = new MemberDao();
			MemberVo mv = md.memberLogin(memberId, memberPwd);
			
			//�������� �ѱ�� �������̳ʿ� �����ϴ� ��
			HttpSession session  = request.getSession();
			if(mv != null){
				session.setAttribute("midx", mv.getMidx());
				session.setAttribute("memberId", mv.getMemberId());
				session.setAttribute("memberName", mv.getMembername()); 
			
			
			//3. �̵��Ѵ�. 
			System.out.println("���ǻ���. �α��μ���");
			//response.sendRedirect(request.getContextPath()+ "/member/memberList.do");
			
			//session ���� �ִٸ� (�Խñ� �ۼ� �� ��� ����� �̿��Ϸ��� ���߿� �α����� �����ʾƼ� �Ѿ���� ���)
			if(session.getAttribute("saveURI")!= null)
			{
				response.sendRedirect((String)session.getAttribute("saveURI"));
			}
			
			}else {System.out.println("�α��� ����");
			response.sendRedirect(request.getContextPath()+ "/member/memberLogin.do");
			}
		
		}else if(command.equals("/member/memberLogout.do")) {
			
			HttpSession session = request.getSession();
			session.invalidate(); //������ ��� ��������

			response.sendRedirect(request.getContextPath()+"/");
		}
		
	}
		
		
		
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
