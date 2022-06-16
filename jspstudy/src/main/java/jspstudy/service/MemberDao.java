package jspstudy.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jspstudy.dbconn.Dbconn;
import jspstudy.domain.MemberVo;

public class MemberDao { //data access object    �޼��常 ������ //������ �����Ű�¿���
	
	//���ᰴü�� ������ü ��������� �����ؼ� ���
	private Connection conn; //Connection ��ü�� �̿��ؼ� ���ڿ��� ����ȭ ��Ų��. 
	private PreparedStatement pstmt;
	
	//������//Dao�����ϸ� ���ᰴü�� �Բ� �����ǵ����Ѵ�.
	public MemberDao() {
		Dbconn db = new Dbconn(); 
		this.conn = db.getConnection();
	}
	
	public int insertMember( String memberId, String memberpwd, String memberName, String memberemail, String memberGender, String memberAdress, String memberPhone, String memberJumin, String hobby, String ip){
		int value = 0;
		
		
		//sql ������ insert������ �ۼ��Ͽ� �����͸� ������ �´�. "���⼭ �ۼ��� insert ������ �ܼ��� ���ڿ�"
		//�׸��� ���������� ������ ������ ������ Ŭ������ stmt���� //�������� ������ �ִ°��� �ƴ϶� ����Ŭ���� �⺻���� �ִ� �͵��� �����ϰ� �������� //value���� �����μ� ���� ���� �⺻���� �����ϴ°͵��� ���� �ʴ´�. 
		String sql= "insert into a_member(MEMBERID,MEMBERPWD,MEMBERNAME,MEMBEREMAIL,MEMBERGENDER,MEMBERADRESS,MEMBERPHONE,MEMBERJUMIN,MEMBERHOBBY,MEMBERIP)" 
				+ "values(?,?,?,?,?,?,?,?,?,?)"; 
			
		//����ó��
		try{
			//Connection���� ����� �������� Statement ��ü�� �����.
			//���ڿ��� sql�� ��Ƽ�  prepareStatment�� ���� ���ڿ��� sql���������� �ٲ��ش�. .executeUpdate�� �̿��Ͽ� ������� int������ ��ȯ
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberpwd);
			pstmt.setString(3, memberName);
			pstmt.setString(4, memberemail);
			pstmt.setString(5, memberGender);
			pstmt.setString(6, memberAdress);
			pstmt.setString(7, memberPhone);
			pstmt.setString(8, memberJumin);
			pstmt.setString(9, hobby);
			pstmt.setString(10, ip);
			
			value =pstmt.executeUpdate();	//pstmt��  insert������ �Ǿ������� ?�� setString�� �̿��Ͽ� ������ ���� �ް� .executeUpdate�� �̿��Ͽ� �������ش�. 
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				pstmt.close();
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			}
		
		
		return value;
	}//insertMember �޼��� ��������

	
	
	
	//ȸ������Ʈ�� ���ٿ� �Ѱ�ü�� ���� bean�� ���� ����. memberVo ��� Ŭ������ ���� ��������� ���� ���
	//select���������ϴ� �޼���
	public ArrayList<MemberVo> memberSelectAll(){
		//��ü����
		ArrayList<MemberVo> alist =new ArrayList<MemberVo>();
		
		ResultSet rs =null; //resultset�� ���� �����͸� �����ؼ� �״�� ����ִ� Ŭ���� 
		String sql="select * from a_member where delyn='N' order by midx desc";
		
	
		try{
			
		pstmt = conn.prepareStatement(sql);
		rs= pstmt.executeQuery(); 
		
		//�ݺ��� ����. rs.next�� �������� ���� ������ true ������ false�� ��ȯ�Ѵ�. ���� ������ �������� �̵��Ѵ�. 
		while(rs.next()){
			//�ݺ��Ҷ����� ��ü����
			MemberVo mv = new MemberVo();
			//�Ű� ���
			//midx �� �̾Ƽ� ��Ʈ���̴ϱ� getint�״��� mv�� ����
			mv.setMidx(rs.getInt("midx"));
			mv.setMembername(rs.getString("memberName"));
			mv.setMemberphone(rs.getString("memberphone"));
			mv.setWriteday(rs.getString("writeday"));
			//���� mv�� alist�� �߰��Ѵ�. 
			alist.add(mv);
		}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				pstmt.close();
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			}
		
		
		return alist;

	}
	
	
	
	//�α��� �޼���  ȸ�������ѻ���߿� �α����ϴ� ����� �����Ͱ� �ֳ� Ȯ��
	
	public MemberVo memberLogin(String memberId, String memberPwd) {
		
		ResultSet rs =null;
		MemberVo mv =null;
		String sql = "select * from a_member where delyn='N' and memberId=? and memberPwd=?";
		
		String sql2 = "INSERT INTO LOGIN(LOGINCD,LOGINIP,MIDX,LOGINDATE) "
				+ "VALUES(loginCD_SEQ.NEXTVAL, ?, ?, SYSDATE); ";
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberPwd);
			
			rs =pstmt.executeQuery(); //���Ŀ� ���� ������ ��ƾ��ϱ⶧���� rs�� ��Ƽ�bean�� ���
			//System.out.println("rs"+rs.next()); rs�� �����Ǿ����� Ȯ�� ��������� Ŀ���� �̵��ϱ⶧���� �ּ�
			if(rs.next()) {
				mv = new MemberVo();
			
				mv.setMidx(rs.getInt("midx"));		
				mv.setMemberId(rs.getString("memberId"));
				mv.setMembername(rs.getString("memberName"));
				}
		
			
		
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
		}
		System.out.println("����α��� �� ���:"+mv.getMidx());
		return mv;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
		

}
