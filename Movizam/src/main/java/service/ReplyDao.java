package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbconn.Dbconn;
import domain.Criteria;
import domain.ReplyVo;
import domain.SearchCriteria;


public class ReplyDao {
	
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	
	//메서드쓰려고 생성할떄 디비도 같이 생성하기
	public ReplyDao(){
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	
	
	
	//댓글작성
	public int ReplyWrite(String replyWriter, String replyContent, String replyIp, int midx, int bidx) {
		
		int value= 0;   
		ResultSet rs =null;
		
		String sql1= " insert into reply(REPLYWRITER, REPLYCONTENT,REPLYWDATE,REPLYIP,ORIGINRIDX,DEPTH,LEVEL_,MIDX,BIDX) "
				+ "values(?, ?, now(), ?, 0, 0, 0, ?, ?)";
		
		String sql2 ="select ridx from reply where originridx=0;";
		String sql3 ="update reply set originridx = ? ridx=?";
		
		
		try {
			pstmt= conn.prepareStatement(sql1);
			pstmt.setString(1, replyWriter);
			pstmt.setString(2, replyContent);
			pstmt.setString(3, replyIp);
			pstmt.setInt(4, midx);
			pstmt.setInt(5, bidx);
			
			value = pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(sql2);
			rs = pstmt.executeQuery();
				
				if(rs.next()) {
					pstmt = conn.prepareStatement(sql3);
					pstmt.setInt(1, rs.getInt("ridx"));
					pstmt.setInt(2, rs.getInt("ridx"));
					
					value = pstmt.executeUpdate();	
				}
			
			
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		return value;
		
	}
	
	
	
	
	
	//한개시글에 해당하는 댓글리스트(검색은 필요없고 페이징만)
	public ArrayList<ReplyVo> ReplySelectAll(Criteria cri, int bidx){		
		
		ArrayList<ReplyVo> alist = new ArrayList<ReplyVo>();
		ReplyVo rv = null;
		ResultSet rs =null; 
		
		
//		String sql="SELECT * FROM ("
//				+ "SELECT ROWNUM AS rnum, A.* FROM ("
//				+ "SELECT * FROM REPLY WHERE DELYN='N' and bidx=? ORDER BY ORIGINRIDX desc, depth ASC) A" //str은 위의 try로 처리
//				+ ") B  WHERE rnum  BETWEEN ? AND ?";  //1~15 씩넘어오게 하기
		
		
		String sql ="SELECT * FROM REPLY WHERE DELYN='N' and bidx=? ORDER BY ORIGINRIDX desc, depth ASC  BETWEEN ? AND ?";
		try{	
			
			pstmt =conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			pstmt.setInt(2, ((cri.getPage()-1)*15+1));  //매서드 자체의 매개변수를 int page로 설정
			pstmt.setInt(3, (cri.getPage()*15));
			
			rs= pstmt.executeQuery();
			
		while(rs.next()){
			//반복할때마다 객체생성해서 객체마다 게시글을 하나씩담기
			rv = new ReplyVo();
			//옮겨 담기
			rv.setRidx(rs.getInt("ridx"));
			rv.setReplyWriter(rs.getString("replyWriter"));
			rv.setReplyContent(rs.getString("replyContent"));
			rv.setReplyWdate(rs.getString("replyWdate"));
			rv.setOriginRidx(rs.getInt("originRidx"));
			rv.setDepth(rs.getInt("depth"));
			rv.setLevel_(rs.getInt("level_"));
			rv.setMidx(rs.getInt("midx"));
			rv.setBidx(rs.getInt("bidx"));
			rv.setBlindYN(rs.getString("blindYN"));
			
			
			
			rv.setLevel_(rs.getInt("level_"));
			
			alist.add(rv); //각각의 bv 객체를 alist에 추가한다. 
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
	
	
	
	
	
	
	
	
	
	
	
	
	//대댓글 작성
	//기존댓글의 depth 하나 올리고 새로 달리는 댓글의 depth, level 올리기
	public int replyComment(ReplyVo rv) //매개변수를 일일히 쓰는것이라 아니라 bean을 생성해서 담아서 넘기기 
	{	
		int value=0;
		String sql1 ="update reply set depth = depth+1 where originRidx=? and depth > ?";	//원글의 depth올려줌
		String sql2= " insert into reply(REPLYWRITER, REPLYCONTENT,REPLYIP,ORIGINRIDX,DEPTH,LEVEL_,MIDX,BIDX) "
				+ "values( ?, ?, ?, ?, ?, ?, ?, ?)";
		
		
		//tranjection  두가지의 실행사항이 있을때 두가지를 소작업단위로 보고 소작업 두개가 모두 실행될 수있을때만 디비에 반영이된다. 
		//한꺼번에 모두수행되어야하는 연산. 마지막 줄에 오류가 하나나면 앞의 같은 작업단위로 묶인것들 모두 취소된다. 
	try {
		conn.setAutoCommit(false); //원래 자바에서는 실행하고나면 자동커밋되도록되어있는데 이것을 false로 바꿔줌
	
		pstmt = conn.prepareStatement(sql1); 		
		pstmt.setInt(1, rv.getOriginRidx());		//값을 매개변수에서 rv로 담아왔음으로 rv에서 꺼내서 쓴다.		
		pstmt.setInt(2, rv.getDepth());
		value = pstmt.executeUpdate();  
		
		pstmt = conn.prepareStatement(sql2); 		
		
		pstmt.setString(1, rv.getReplyWriter());
		pstmt.setString(2, rv.getReplyContent());
		pstmt.setString(3, rv.getReplyIp());
		pstmt.setInt(4,rv.getOriginRidx());
		pstmt.setInt(5,rv.getDepth()+1);	//매개변수로 받아온 Vo기준. Vo에 담겨있는 값에서 +1 해준 값을 커밋한다. 
		pstmt.setInt(6,rv.getLevel_()+1);
		pstmt.setInt(7, rv.getMidx());
		pstmt.setInt(8, rv.getBidx());
		value = pstmt.executeUpdate();
		
		conn.commit(); //쿼리정상적으로 다되면 커밋
		
		} catch (Exception e) {		
			try {
				conn.rollback();
			} catch (SQLException e1) {
				//쿼리 실행하다가 오류나면 rollback하라
				e1.printStackTrace();
			}     
			e.printStackTrace();
		} finally{
			try{	
			pstmt.close();
			conn.close();
			}catch(SQLException e){
			e.printStackTrace();
			}
		}
	
	return value;
	
	}
	
	
	
	// 댓글 수정하기	
	public int ReplyEdit(String replyContent, String replIp, int ridx) {

		int value=0;
		
		String sql = "update Reply set REPLYCONTENT = ?, REPLYIP =?  where ridx=?";
			 
		try {
			pstmt =conn.prepareStatement(sql);
			
			pstmt.setString(1, replyContent);
			pstmt.setString(2, replIp);
			pstmt.setInt(3, ridx);
			
			value = pstmt.executeUpdate();	
			
				
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		return value;
	}
	
	
	
	// 댓글 삭제
	public int replyDelete(int ridx) {
		
		int value = 0;
		String sql = "update reply set delyn='Y' where ridx=?";
	
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ridx);
			
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	
	
	
	
	
	
	// 댓글 총 개수 (페이징)	// 갯수만 반환하는데 rs 써야하나
	public int ReplyTotal(SearchCriteria scri) {	
		int cnt = 0;
		ResultSet rs= null;
	
		String str ="";
		
		if(scri.getSearchType().equals("replyContent")) {
			str = "and replyContent like ?";
		}else if(scri.getSearchType().equals("replyContent")){
			str = "and replyWriter like ?";
		}
		
		String sql ="select count(*) as cnt from reply where delyn='N' "+str;	// 게시물 전체 개수	. 
		try {
			pstmt= conn.prepareStatement(sql);
			pstmt.setString(1, "%"+scri.getKeyword()+"%");	
			
			rs= pstmt.executeQuery();
			
			if(rs.next()) {
				cnt= rs.getInt("cnt");
			}
			
		} catch (SQLException e) {
		
			e.printStackTrace();
			
		}finally {
			try {
				rs.close();
				
			pstmt.close();
			} catch (SQLException e) {
				
			}
			
		}
		
		
		return cnt;
	}
	
	
	
	//한게시글에 해당하는 댓글리스트(관리자 모드)
		public ArrayList<ReplyVo> ReplySelectAll_manage(SearchCriteria scri){		
			
			ArrayList<ReplyVo> alist = new ArrayList<ReplyVo>();
			ReplyVo rv = null;
			ResultSet rs =null; 
			
			String str ="";
			
			if(scri.getSearchType().equals("replyContent")) {
				str = "and replyContent like ?";
			}else if(scri.getSearchType().equals("replyContent")){
				str = "and replyWriter like ?";
			}
			
			
			String sql="SELECT * FROM ("
					+ "SELECT ROWNUM AS rnum, A.* FROM ("
					+ "SELECT * FROM REPLY WHERE DELYN='N' "+str+" ORDER BY ORIGINRIDX desc, depth ASC) A" 
					+ ") B WHERE rnum  BETWEEN ? AND ?";  //1~15 씩넘어오게 하기
			
			try{	
				
				pstmt =conn.prepareStatement(sql);
				pstmt.setString(1, "%"+scri.getKeyword()+"%");
				pstmt.setInt(2, ((scri.getPage()-1)*15+1));  //매서드 자체의 매개변수를 int page로 설정
				pstmt.setInt(3, (scri.getPage()*15));
				
				rs= pstmt.executeQuery();
				
			while(rs.next()){
				//반복할때마다 객체생성해서 객체마다 게시글을 하나씩담기
				rv = new ReplyVo();
				//옮겨 담기
				rv.setRidx(rs.getInt("ridx"));
				rv.setReplyWriter(rs.getString("replyWriter"));
				rv.setReplyContent(rs.getString("replyContent"));
				rv.setReplyWdate(rs.getString("replyWdate"));
				rv.setOriginRidx(rs.getInt("originRidx"));
				rv.setDepth(rs.getInt("depth"));
				rv.setLevel_(rs.getInt("level_"));
				rv.setMidx(rs.getInt("midx"));
				rv.setBidx(rs.getInt("bidx"));
				rv.setBlindYN(rs.getString("blindYN"));
				
				
				alist.add(rv); //각각의 bv 객체를 alist에 추가한다. 
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
	
		//한게시글에 해당하는 댓글리스트(일반회원 모드)
		public ArrayList<ReplyVo> ReplySelectAll_normal(SearchCriteria scri, int midx){		
					
			ArrayList<ReplyVo> alist = new ArrayList<ReplyVo>();
			ReplyVo rv = null;
			ResultSet rs =null; 
					
			String str ="";
					
			if(scri.getSearchType().equals("replyContent")) {
				str = "and replyContent like ?";
			}else if(scri.getSearchType().equals("replyContent")){
				str = "and replyWriter like ?";
			}
					
					
			String sql="SELECT * FROM ("
					+ "SELECT ROWNUM AS rnum, A.* FROM ("
					+ "SELECT * FROM REPLY WHERE DELYN='N' and midx=? "+str+" ORDER BY ORIGINRIDX desc, depth ASC) A" 
					+ ") B WHERE rnum  BETWEEN ? AND ?";  //1~15 씩넘어오게 하기
			
			try{	
						
				pstmt =conn.prepareStatement(sql);
				pstmt.setInt(1, midx);
				pstmt.setString(2, "%"+scri.getKeyword()+"%");
				pstmt.setInt(3, ((scri.getPage()-1)*15+1));  //매서드 자체의 매개변수를 int page로 설정
				pstmt.setInt(4, (scri.getPage()*15));
						
				rs= pstmt.executeQuery();
						
			while(rs.next()){
				//반복할때마다 객체생성해서 객체마다 게시글을 하나씩담기
					rv= new ReplyVo();
				//옮겨 담기
				rv.setRidx(rs.getInt("ridx"));
				rv.setReplyWriter(rs.getString("replyWriter"));
				rv.setReplyContent(rs.getString("replyContent"));
				rv.setReplyWdate(rs.getString("replyWdate"));
				rv.setOriginRidx(rs.getInt("originRidx"));
				rv.setDepth(rs.getInt("depth"));
				rv.setLevel_(rs.getInt("level_"));
				rv.setMidx(rs.getInt("midx"));
				rv.setBidx(rs.getInt("bidx"));
				rv.setBlindYN(rs.getString("blindYN"));
						
						
				alist.add(rv); //각각의 bv 객체를 alist에 추가한다. 
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
	
	//블라인드 처리
	public int blindYNon(int ridx) {
		int value =0;
		
		String sql ="update reply set blindYN = 'Y' where ridx = ? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ridx);
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	
		
		return value;
	}
	
	
	
	//블라인드 처리해제
		public int blindYNoff(int ridx) {
			int value =0;
			
			String sql ="update reply set blindYN = 'N' where ridx = ? ";
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, ridx);
				value = pstmt.executeUpdate();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		
			
			return value;
		}
	
	
	
	
	
	
}
