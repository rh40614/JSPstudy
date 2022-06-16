package mysqlProject.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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

import mysqlProject.domain.BoardVo;
import mysqlProject.domain.Criteria;
import mysqlProject.domain.MemberVo;
import mysqlProject.domain.PageMaker;
import mysqlProject.domain.SearchCriteria;
import mysqlProject.service.MemberDao;
import mysqlProject.service.infoDao;

@WebServlet("/MemberController")
public class MemberController extends HttpServlet {
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
		
		if(command.equals("/member/join.do")) {
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberJoin.jsp");
			rd.forward(request, response);
		
		}else if(command.equals("/member/joinAction.do")) {
			String id = request.getParameter("memberId");	
			String pwd = request.getParameter("memberPwd");
			String nic = request.getParameter("memberNic");
			String name = request.getParameter("memberName");
			String email = request.getParameter("memberEmail");
			String phoneAgency = request.getParameter("memberPhoneAgency");
			String phone = request.getParameter("memberPhone");
			String gender = request.getParameter("memberGender");
			String ip = InetAddress.getLocalHost().getHostAddress();
			
			MemberVo mv = new MemberVo();
			mv.setMemberid(id);
			mv.setMemberpwd(pwd);
			mv.setMembernicname(nic);
			mv.setMembername(name);
			mv.setMemberemail(email);
			mv.setMemberphoneagency(phoneAgency);
			mv.setMemberphone(phone);
			mv.setMembergender(gender);
			mv.setJoinip(ip);
						
			MemberDao md = new MemberDao();
			int value = md.insertMember(mv);
			
			if(value == 1){
				//response.sendRedirect(request.getContextPath()+"/");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('회원가입 완료'); location.href='"+request.getContextPath()+"/'</script>");
				out.flush();
			}else{
				response.sendRedirect(request.getContextPath()+"/member/memberJoin.do");
			}
		
		}else if(command.equals("/member/login.do")) {
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberLogin.jsp");
			rd.forward(request, response);
		
		}else if(command.equals("/member/loginAction.do")) {
			//넘어온 값 받기
			String id = request.getParameter("memberId");
			String pwd = request.getParameter("memberPwd");
			//처리하기
			MemberDao md = new MemberDao();
			MemberVo mv = md.memberLogin(id, pwd);
			
			if(mv != null) {
				session.setAttribute("midx", mv.getMidx());
				session.setAttribute("memberId", mv.getMemberid());
				session.setAttribute("memberNicname", mv.getMembernicname());
				session.setAttribute("oper", mv.getOper());
				session.setAttribute("memberProfile", mv.getMemberProfile());
				
				String path = request.getSession().getServletContext().getRealPath("/") + File.separator + "profile/"+mv.getMidx();
				File folder = new File(path);
				System.out.println("folder : "+folder);
				if(!folder.exists()) {
					try {
						folder.mkdir();
						String basicImage = request.getSession().getServletContext().getRealPath("/") + "profile\\1.jpg";
						String insertImage = request.getSession().getServletContext().getRealPath("/") + "profile\\"+mv.getMidx()+"\\1.jpg";
						System.out.println("basicImage : "+basicImage);
						System.out.println("insertImage : "+insertImage);
						File basicFile = new File(basicImage);
						File insertFile = new File(insertImage);
						Files.copy(basicFile.toPath(),insertFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					} catch (Exception e) {
						e.getStackTrace();
					}
				}
				
				if(session.getAttribute("saveUrl") != null) {
					response.sendRedirect((String)session.getAttribute("saveUrl"));
				}else {
					response.sendRedirect(request.getContextPath()+"/");
				}
				
			}else {
				PrintWriter out = response.getWriter();
				out.println("<script>alert('아이디 혹은 비밀번호 오류'); location.href='"+request.getContextPath()+"/member/login.do'</script>");
				out.flush();
				//response.sendRedirect(request.getContextPath()+"/member/login.do");
			}
			
		}else if(command.equals("/member/logout.do")) {
			session.invalidate();			
			response.sendRedirect(request.getContextPath()+"/");
		
		}else if(command.equals("/member/find.do")) {
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberFind.jsp");
			rd.forward(request, response);
		
		}else if(command.equals("/member/info.do")) {
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberInfo.jsp");
			rd.forward(request, response);
		
		}else if(command.equals("/member/infoProfile.do")) {
			
			int midx = (int) session.getAttribute("midx");
			int sizeLimit = 1024*1024*15;
			String path = request.getSession().getServletContext().getRealPath("/") + File.separator + "profile/"+midx;
			
			File folder = new File(path);
			
			if(!folder.exists()) {
				try {
					folder.mkdir();
				} catch (Exception e) {
					e.getStackTrace();
				}
			}else {
				File[] files = folder.listFiles();
				for(File file : files) {
					file.delete();
				}
				folder.mkdir();
			}
			
			MultipartRequest multi = new MultipartRequest(request,path,sizeLimit,"UTF-8",new DefaultFileRenamePolicy());
			
			@SuppressWarnings("rawtypes")
			Enumeration files = multi.getFileNames();
			String file = (String) files.nextElement();
			String fileName = multi.getFilesystemName(file);
			
			MemberDao md = new MemberDao();
			int value = md.changeProfile(midx, fileName);
			
			if(value==1) {
				session.removeAttribute("memberProfile");
				session.setAttribute("memberProfile",fileName);
				System.out.println(session.getAttribute("memberProfile"));
				PrintWriter out = response.getWriter();
				out.println("<script>window.parent.location.href='"+request.getContextPath()+"/member/info.do';</script>");
				out.flush();
			}
			
		}else if(command.equals("/member/findIdAction.do")) {
			session.setAttribute("find", "id");
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberFind_input.jsp");
			rd.forward(request, response);
		
		}else if(command.equals("/member/findPwdAction.do")) {
			session.setAttribute("find", "pwd");
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberFind_input.jsp");
			rd.forward(request, response);
		
		}else if(command.equals("/member/findResult.do")) {
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String find = (String) session.getAttribute("find");
			
			MemberVo mv = new MemberVo();
			if(find.equals("id")) {
				mv.setMemberid(id);
				mv.setMembername(name);
				mv.setMemberemail(email);
				mv.setFind(find);
				MemberDao md = new MemberDao();
				md.findresult(mv);
			}else {
				mv.setMemberid(id);
			}
			request.setAttribute("mv", mv);
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberFind_result.jsp");
			rd.forward(request, response);
		
		}else if(command.equals("/member/changePwd.do")) {
			String id = request.getParameter("id");
			String pwd = request.getParameter("memberPwd");
			
			MemberDao md = new MemberDao();
			int value = md.changePwd(id, pwd);
			
			PrintWriter out = response.getWriter();
			if(value==1) {
				out.println("<script>alert('비밀번호 변경 완료'); location.href='"+request.getContextPath()+"/'</script>");
				out.flush();
			}else {
				out.println("<script>alert('변경오류'); location.href='"+request.getContextPath()+"/'</script>");
				out.flush();
			}
		
		}else if(command.equals("/member/idxModify.do")) {
			int midx = (int) session.getAttribute("midx");
			String pwd = request.getParameter("memberPwd");
			String nic = request.getParameter("memberNic");
			String name = request.getParameter("memberName");
			String email = request.getParameter("memberEmail");
			String phoneAgency = request.getParameter("memberPhoneAgency");
			String phone = request.getParameter("memberPhone");
			String gender = request.getParameter("memberGender");
			MemberVo mv = new MemberVo();
			mv.setMidx(midx);
			mv.setMemberpwd(pwd);
			mv.setMembernicname(nic);
			mv.setMembername(name);
			mv.setMemberemail(email);
			mv.setMemberphoneagency(phoneAgency);
			mv.setMemberphone(phone);
			mv.setMembergender(gender);
						
			MemberDao md = new MemberDao();
			int value = md.modifyMember(mv);
			
			if(value >=1){
				session.setAttribute("memberNicname", mv.getMembernicname());
				PrintWriter out = response.getWriter();
				out.println("<script>alert('정보수정 완료'); window.parent.location.href='"+request.getContextPath()+"/member/info.do';</script>");
				out.flush();
			}else{
				response.sendRedirect(request.getContextPath()+"/member/memberJoin.do");
			}
		
		}else if(command.equals("/member/infoBoard.do")) {
			int midx = (int) session.getAttribute("midx");
			String page = request.getParameter("page");
			if(page == null) page = "1";
			int page_ = Integer.parseInt(page);
			//처리
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(page_);
			infoDao id = new infoDao();
			int cnt = id.infoBoardList(scri, midx);
			
			Criteria cri = new Criteria();
			cri.setPage(page_);
			
			PageMaker pm = new PageMaker();
			pm.setScri(scri);
			pm.setTotalCount(cnt);
			System.out.println(cnt);
			ArrayList<BoardVo> alist = id.infoBoardListAll(scri, midx);
			request.setAttribute("alist", alist);	//데이터(자원) 공유
			request.setAttribute("pm", pm);
			//이동
			RequestDispatcher rd = request.getRequestDispatcher("/member/info_board.jsp");
			rd.forward(request, response);
		
		}else if(command.equals("/member/delAction.do")) {
			String id = (String) session.getAttribute("memberId");
			MemberDao md = new MemberDao();
			
			int value = md.delClear(id);
			if(value == 1) {
				session.invalidate();
				PrintWriter out = response.getWriter();
				out.println("<script>alert('회원탈퇴 완료'); window.parent.location.href='"+request.getContextPath()+"/';</script>");
				out.flush();
			}else {
				PrintWriter out = response.getWriter();
				out.println("<script>alert('탈퇴 중 오류 발생'); window.parent.location.href='"+request.getContextPath()+"/member/info.do';</script>");
				out.flush();
			}
		}
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
