package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;


import dbconn.Dbconn;
import domain.MemberVo;
import domain.SearchCriteria;

public class MemberDao {
	
	private Connection conn; 
	private PreparedStatement pstmt;
	
	
	
	public MemberDao(){
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	//ȸ�����̺� ȸ������ �ֱ�. ȸ������
	public int insertMember(String memberId, String memberPwd, String memberNickname, String memberName,String memberPhone, String memberAdr, String memberEmail, String memberIp, String birth_yy, String birth_mm, String birth_dd)
	{
		int value = 0;
		
		String sql= "insert into member(MEMBERID,MEMBERPWD,MEMBERNICKNAME,MEMBERNAME,MEMBERPHONE,MEMBERADR,MEMBEREMAIL,MEMBERIP,BIRTH_YY,BIRTH_MM,BIRTH_DD)"
					+"values(?,?,?,?,?,?,?,?,?,?,?)";
		
	
		try {
			pstmt = conn.prepareStatement(sql);
	
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberPwd);
			pstmt.setString(3, memberNickname);
			pstmt.setString(4, memberName);
			pstmt.setString(5, memberPhone);
			pstmt.setString(6, memberAdr);
			pstmt.setString(7, memberEmail);
			pstmt.setString(8, memberIp);
			pstmt.setString(9, birth_yy);
			pstmt.setString(10, birth_mm);
			pstmt.setString(11, birth_dd);
	
			value =pstmt.executeUpdate();
			//System.out.println(value);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	return value;
	
	}
	
	
	
	
	//�α��� �޼���(���̵�, ��й�ȣ�� ��ġ�ϴ� ȸ������)
	public MemberVo memberLogin(String memberId, String memberPwd) {
		
//		System.out.println("memberId: "+memberId);
//		System.out.println("memberPwd: "+memberPwd);
		MemberVo mv =null;
		ResultSet rs =null;
		
		String sql="select * from member where delYN='N' and memberId=? and memberPwd=?";
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberPwd);
			
			rs = pstmt.executeQuery();
		//	System.out.println(rs.next());
			if(rs.next()) {
			
				mv=new MemberVo();
				mv.setMidx(rs.getInt("midx"));
				mv.setMemberId(rs.getString("memberId"));
				mv.setMemberPwd(rs.getString("memberPwd"));
				mv.setMemberNickname(rs.getString("memberNickname"));
				mv.setAdminYN(rs.getString("adminYN"));
				mv.setMemberImage(rs.getString("memberImage"));
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		System.out.println("�α��� �޼��� ����");
		
		return mv;
	}
	
	//�α��� ���� �Է�
	public int loginInfoInsert(String memberIp, int midx) {
		System.out.println("�α��������޼��� ����");
		int value = 0;
				
		String sql = "INSERT INTO LOGIN(LOGINIP,MIDX,LOGINDATE) "
					+ " VALUES(?, ?, now() ";
				
				
		try {
			pstmt =conn.prepareStatement(sql);
					
			pstmt.setString(1, memberIp);
			pstmt.setInt(2, midx);
			
			value = pstmt.executeUpdate();
					
		} catch (SQLException e) {
					
			e.printStackTrace();
		}
		System.out.println("�α��� ���� �Է� �޼���");
		return value;
	}

	//�α��� �ð� ������Ʈ 
//	public int loginDate(String memberId) {
//		
//		int value = 0;
//		
//		String sql ="update (select* FROM member a inner join login b on a.midx=b.midx)"
//				+ "loginDate = SYSDATE"
//				+ "where memberId=?";
//		
//		try {
//			pstmt =conn.prepareStatement(sql);
//			
//			pstmt.setString(1, memberId);
//			
//		} catch (SQLException e) {
//			
//			e.printStackTrace();
//		}
//		
//		return value;
//	}
	
	
	
	
	//�α��� ���� Ƚ�� ī��Ʈ �޼���
	public int plusLoginFailCount(String memberId) {
		int value=0;
		
		
		String sql ="update (select* FROM member a inner join login b on a.midx=b.midx) A"
					+ " LOGINFAILCOUNT set LOGINFAILCOUNT = LOGINFAILCOUNT +1"
					+ " loginDate = now()" 
					+ " where memberId=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			value = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
				
		return value;
	}
	
	//�α��� ���� Ƚ��,���� �ʱ�ȭ
	 public int clearLoginFailCount(String memberId) {
		 int value=0;
		 
		  String sql ="update (select* FROM member a inner join login b on a.midx=b.midx) A"
		  			+ "LOGINFAILCOUNT set LOGINFAILCOUNT='0'"
					+ ", loginDate = now()"
					+ ", isLock = 'N'"
					+ "where memberId=?";
		 
		  try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, memberId);
				
				value = pstmt.executeUpdate();
				
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		 
		 
		 
		 return value;
	 }
	
	//�α��� ���� Ƚ�� �ʰ�. �α��� ����
	 
	 public int loginLock(String memberId) {
		 int value=0;
		 
		 String sql = "update (select* FROM member a inner join login b on a.midx=b.midx) A"
		 			+ "set isLock ='Y' "
		 			+ "where delYN='N' "
		 			+ "AND memberId = ? "
		 			+ "AND LOGINFAILCOUNT > 5";
		 
		 try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			value= pstmt.executeUpdate();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		 
		 return value;
	 }
	 
	 
	 
	 
	 //ȸ������ ã��
	 public MemberVo memberInfoReturn(String memberName, int birth_yy, int birth_mm ,int birth_dd ,String memberPhone ) {
		 
//		 System.out.println(memberName);
//		 System.out.println(birth_yy);
//		 System.out.println(birth_mm);
//		 System.out.println(birth_dd);
//		 System.out.println(memberPhone);
		 
		 
		 MemberVo mv =null;
		 ResultSet rs =null;
		
		 String sql = "select * from member "
		 		+ "where memberName=? "
		 		+ "and birth_yy=? "
		 		+ "and birth_mm=? "
		 		+ "and birth_dd=? "
		 		+ "and memberPhone=? ";
		 
		 try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberName);
			pstmt.setInt(2, birth_yy);
			pstmt.setInt(3, birth_mm);
			pstmt.setInt(4, birth_dd);
			pstmt.setString(5, memberPhone);
			
			rs= pstmt.executeQuery();
			
			//System.out.println("rs.next: "+rs.next());
			if(rs.next()) {
				mv=new MemberVo();
				mv.setMemberId(rs.getString("memberId"));
				mv.setMemberPwd(rs.getString("memberPwd"));
			}
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		 
		 return mv;
	 }
	 
	 
	 
	//�α��� �� �λ�޼��� ���(db �����)
		public String randomGreetingMessage() {
			
			ArrayList<String> alist = new ArrayList<String>();
				
			alist.add("������ � ��ȭ�� ���̳���?");
			alist.add("���� ������ ��ȭ�� �˻��غ�����!");
			alist.add("������ ������ ���ƿ�! ��ȭ��������!");
			alist.add("���� �ݶ󰡼� ��ȭ���� �ϽǷ���?");
			alist.add("������ ������ ���ƿ�! �����̶� �������?");
			alist.add("� ��ȭ �����ϼ���?");
			alist.add("���ͽ�Ʈ������ ���̾��?");
			alist.add("���� ���� �����̴��� ���� ������ �Ф�");
			alist.add("���õ� ���� �Ϸ� �Ǽ���!");
			
			Random r = new Random();

			int randomitem = r.nextInt(alist.size());		//�ε��������� ���������� int ������ ���� 
			String randomMessage = alist.get(randomitem);
			
			System.out.println("ȯ������ ����");
			return randomMessage;
		}
	 
	 
	
		//ȸ�� Ż��
		public int memberDelete(String memberId, String memberPwd) {
			
			System.out.println(memberId);
			System.out.println(memberPwd);
			int value = 0;
			String sql = "update member set delyn='Y' where memberId=? and memberPwd=?";
		
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, memberId);
				pstmt.setString(2, memberPwd);
				
				value = pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return value;
		}
		
		
		
		//ȸ����� �ҷ�����(�Ű����� �־ �˻��� �����ϰ� )
		public ArrayList<MemberVo> memberSelectAll(SearchCriteria scri){
		
			ArrayList<MemberVo> alist = new ArrayList<MemberVo>();
			MemberVo mv= null;
			ResultSet rs =null;
			
			String str ="";
			if(scri.getSearchType().equals("memberName")) {
				str = " and memberName like ? ";		
			}else if(scri.getSearchType().equals("memberNickname")){
				str = " and memberNickname like ? ";
			}else if(scri.getSearchType().equals("memberPhone")){
				str = " and memberPhone like ? ";
			}else if(scri.getSearchType().equals("memberAdr")){
				str = " and memberAdr like ? ";
			}
			
			
			String sql="select * from ("
					+ "select  rownum as rnum, A.* from ("
					+ "select * from member where delYN ='N'" +str+" order by midx desc) A "
					+ ") B where rnum between ? and ?";
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%"+scri.getKeyword()+"%");
				pstmt.setInt(2, ((scri.getPage()-1)*15+1));  //�ż��� ��ü�� �Ű������� int page�� ����
				pstmt.setInt(3, (scri.getPage()*15));
				
				rs= pstmt.executeQuery(); 
				
				while(rs.next()){
					//�ݺ��Ҷ����� �Ѹ� ���� ��ü����
					mv = new MemberVo();
				
					mv.setMidx(rs.getInt("midx"));
					mv.setScreenMidx(rs.getInt("rnum"));
					mv.setMemberNickname(rs.getString("memberNickname"));
					mv.setMemberName(rs.getString("memberName"));
					mv.setMemberPhone(rs.getString("memberphone"));
					mv.setAdminYN(rs.getString("adminYN"));
					
					alist.add(mv);
				}
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
			
			return alist;
		}
		
		
		
		
		
		
		
		
		//ȸ������ ���� �� ���
		public MemberVo memberSelectOne(int midx) {
			
			MemberVo mv = new MemberVo();
			ResultSet rs =null;
			
			String sql ="select * from member where midx=? ";
			
			try {
				pstmt= conn.prepareStatement(sql);
				pstmt.setInt(1, midx);
			
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					mv.setMidx(rs.getInt("midx"));
					mv.setMemberName(rs.getString("memberName"));
					mv.setMemberPwd(rs.getString("memberPwd"));
					mv.setMemberNickname(rs.getString("memberNickname"));
					mv.setBirth_yy(rs.getInt("birth_yy"));
					mv.setBirth_mm(rs.getInt("birth_mm"));
					mv.setBirth_dd(rs.getInt("birth_dd"));
					mv.setMemberAdr(rs.getString("memberAdr"));
					mv.setMemberPhone(rs.getString("memberPhone"));
					mv.setAdminYN(rs.getString("adminYN"));
					mv.setMemberEmail(rs.getString("memberEmail"));
					mv.setMemberImage(rs.getString("memberImage")); 
				}
				
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			System.out.println("mv: "+mv);
			
			return mv;
		}
		
		
		
		
		//ȸ������ ����
		
		public int memberEdit( String memberPwd, String memberNickname, String memberName, int birth_yy, int birth_mm, int birth_dd, String memberAdr, String memberPhone, String memberEmail, String memberImage, int midx) {
			int value = 0;
			
			String sql ="update member "
					+ "set memberPwd=?, memberNickname=?, memberName=?, "
					+ "birth_yy=?, birth_mm=?, birth_dd=?, "
					+ "memberAdr=?, memberPhone=?, memberEmail=?, memberImage =? "
					+ "where midx=?";		
			
			try {
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, memberPwd);
				pstmt.setString(2, memberNickname);
				pstmt.setString(3, memberName);
				pstmt.setInt(4, birth_yy);
				pstmt.setInt(5, birth_mm);
				pstmt.setInt(6, birth_dd);
				pstmt.setString(7, memberAdr);
				pstmt.setString(8, memberPhone);
				pstmt.setString(9, memberEmail);
				pstmt.setString(10, memberImage);
				pstmt.setInt(11, midx);
				
				value = pstmt.executeUpdate();				
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
			return value;
			
		}
		
		
		//�� ȸ����	//�Ű������־ �˻���� �߰�
		public int MemberTotal(SearchCriteria scri) {
			
			//��ȸ���� ���� ������ ��ȯ�ϹǷ� int	
			int cnt =0;	
			ResultSet rs =null;
			
			//����ڰ� �Է��� Ű���带 �޾Ƽ� �˻� ī�װ��� ���� ����
			String str ="";
			if(scri.getSearchType().equals("memberName")) {
				str = " and memberName like ? ";		
			}else if(scri.getSearchType().equals("memberNickname")){
				str = " and memberNickname like ? ";
			}else if(scri.getSearchType().equals("memberPhone")){
				str = " and memberPhone like ? ";
			}else if(scri.getSearchType().equals("memberAdr")){
				str = " and memberAdr like ? ";
			}
			
			
			//ȸ���� ��(�ӽ�Į�� cnt ������ֱ� )
			String sql ="select count(*) as cnt from member where delYN='N'"+ str;
			
			try {
				pstmt = conn.prepareStatement(sql);
				// Ű���� ������ �ͼ� ����Ǵ� ������ �°� �־ ����
				pstmt.setString(1, "%"+scri.getKeyword()+"%");
				//����Ǵ� sql ������ ���ǹ��� �ɷ��� ? �� �Ѱ�
//				pstmt.setString(2, "%"+scri.getKeyword()+"%");
//				pstmt.setString(3, "%"+scri.getKeyword()+"%");
//				pstmt.setString(4, "%"+scri.getKeyword()+"%");
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					
					cnt = rs.getInt("cnt");
				}
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
			
			return cnt;
			
		}
		
		
		
		
		
		//���� �ο� 
		public int AdminYNon(int midx) {
			int value =0;
			
			String sql ="update member set adminYN = 'Y' where midx = ? ";
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, midx);
				value = pstmt.executeUpdate();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		
			
			return value;
		}
		
		
		
		
		//���� ����
		public int AdminYNoff(int midx) {
			int value =0;
					
			String sql ="update member set adminYN = 'N' where midx = ? ";
					
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, midx);
				value = pstmt.executeUpdate();
						
			} catch (SQLException e) {
						
				e.printStackTrace();
			}
				
					
			return value;
		}
		
		
		
		
		
		
		
		
		
		
		
		
	 
}
