package jspstudy.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jspstudy.dbconn.Dbconn;
import jspstudy.domain.MemberVo;

public class MemberDao { //data access object    메서드만 모여있음 //쿼리를 실행시키는역할
	
	//연결객체랑 구문객체 멤버변수로 선언해서 사용
	private Connection conn; //Connection 객체를 이용해서 문자열을 쿼리화 시킨다. 
	private PreparedStatement pstmt;
	
	//생성자//Dao생성하면 연결객체도 함께 생성되도록한다.
	public MemberDao() {
		Dbconn db = new Dbconn(); 
		this.conn = db.getConnection();
	}
	
	public int insertMember( String memberId, String memberpwd, String memberName, String memberemail, String memberGender, String memberAdress, String memberPhone, String memberJumin, String hobby, String ip){
		int value = 0;
		
		
		//sql 변수에 insert구문을 작성하여 데이터를 가지고 온다. "여기서 작성한 insert 구문은 단순히 문자열"
		//그리고 접속정보를 가지고 쿼리를 실행한 클래스인 stmt에서 //변수값을 일일히 넣는것이 아니라 오라클에서 기본값이 있는 것들을 제외하고 가져오기 //value에는 변수로서 값을 적고 기본값을 제공하는것들은 넣지 않는다. 
		String sql= "insert into a_member(MEMBERID,MEMBERPWD,MEMBERNAME,MEMBEREMAIL,MEMBERGENDER,MEMBERADRESS,MEMBERPHONE,MEMBERJUMIN,MEMBERHOBBY,MEMBERIP)" 
				+ "values(?,?,?,?,?,?,?,?,?,?)"; 
			
		//예외처리
		try{
			//Connection에서 명령을 실행해줄 Statement 객체를 만든다.
			//문자열을 sql에 담아서  prepareStatment로 받은 문자열을 sql쿼리문으로 바꿔준다. .executeUpdate를 이용하여 결과값을 int형으로 반환
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
			
			value =pstmt.executeUpdate();	//pstmt가  insert구문이 되었음으로 ?에 setString을 이용하여 각각의 값을 받고 .executeUpdate를 이용하여 실행해준다. 
		
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
	}//insertMember 메서드 종료지점

	
	
	
	//회원리스트를 한줄에 한객체로 만들어서 bean을 만들어서 담음. memberVo 라는 클래스를 만들어서 멤버변수로 만들어서 담기
	//select구문실행하는 메서드
	public ArrayList<MemberVo> memberSelectAll(){
		//객체생성
		ArrayList<MemberVo> alist =new ArrayList<MemberVo>();
		
		ResultSet rs =null; //resultset은 여러 데이터를 복사해서 그대로 담고있는 클래스 
		String sql="select * from a_member where delyn='N' order by midx desc";
		
	
		try{
			
		pstmt = conn.prepareStatement(sql);
		rs= pstmt.executeQuery(); 
		
		//반복문 실행. rs.next는 다음행의 값이 있으면 true 없으면 false를 반환한다. 값이 있으면 그행으로 이동한다. 
		while(rs.next()){
			//반복할때마다 객체생성
			MemberVo mv = new MemberVo();
			//옮겨 담기
			//midx 를 뽑아서 인트형이니까 getint그다음 mv에 담음
			mv.setMidx(rs.getInt("midx"));
			mv.setMembername(rs.getString("memberName"));
			mv.setMemberphone(rs.getString("memberphone"));
			mv.setWriteday(rs.getString("writeday"));
			//담은 mv를 alist에 추가한다. 
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
	
	
	
	//로그인 메서드  회원가입한사람중에 로그인하는 사람의 데이터가 있나 확인
	
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
			
			rs =pstmt.executeQuery(); //이후에 세션 변수에 담아야하기때문에 rs로 담아서bean에 담기
			//System.out.println("rs"+rs.next()); rs가 생성되엇는지 확인 살아있으면 커서가 이동하기때문에 주석
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
		System.out.println("현재로그인 한 멤버:"+mv.getMidx());
		return mv;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
		

}
