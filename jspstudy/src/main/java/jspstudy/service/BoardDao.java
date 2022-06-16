package jspstudy.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jspstudy.dbconn.Dbconn;
import jspstudy.domain.BoardVo;
import jspstudy.domain.Criteria;
import jspstudy.domain.SearchCriteria;


public class BoardDao {
	
	private Connection conn; //Connection 객체를 이용해서 문자열을 쿼리화 시킨다. 
	private PreparedStatement pstmt;
	
	
	public BoardDao(){
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	
	
	//db에 데이터를 입력하는 메서드.//String fileName 추가
	public int insertBoard(String subject, String writer, String content, String ip, int midx, String fileName) 
	{	
	
		int value=0;
		ResultSet rs =null;
		
		String sql ="insert into a_board(originBidx, DEPTH, LEVEL_, SUBJECT, WRITER, CONTENT, IP, MIDX, FileName)"
		+"values(0,0,0,?,?,?,?,?,?)";   
		
		String sql2 ="select bidx from a_board where originBidx = 0";
		String sql3 ="update a_board set originBidx =? where bidx =?";
		
	try {
		pstmt = conn.prepareStatement(sql); 		//위의 문자열을 sql 객체로 바꿔줌
		pstmt.setString(1,subject);					//bidx는 입력하기 않아도 자동으로 생성됨으로 값으로 설정하지는 않는다. 
		pstmt.setString(2,writer);
		pstmt.setString(3,content);
		pstmt.setString(4,ip);
		pstmt.setInt(5,midx);
		pstmt.setString(6, fileName);
		value = pstmt.executeUpdate();  
		
		//방금 넣은 게시물을 가져와서 originBidx가 0이면 그  bidx를 가져오고  그값을 update로 넣어준다. 
		pstmt = conn.prepareStatement(sql2); 	
		rs = pstmt.executeQuery();
		
			while(rs.next()) {
				pstmt = conn.prepareStatement(sql3);
				pstmt.setInt(1, rs.getInt("bidx"));
				pstmt.setInt(2, rs.getInt("bidx"));
				value = pstmt.executeUpdate();  
			}
	
		
		} catch (Exception e) {		
			e.printStackTrace();
		} finally{
			try{	
			pstmt.close();
			conn.close();
			}catch(SQLException e){
			e.printStackTrace();
			}
		}
	System.out.println("게시글 작성완료");
	
	return value;
	
	}
	
	
	
	
	
	
	//값을 담는 객체 Value Object 에 값을 담기 위한 메서드 
	//여러개의 데이터를 가져올것이기 때문에 배열로 받아온다. 
	public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri){		
		
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
		BoardVo bv = null;
		ResultSet rs =null; //resultSet은 여러 데이터를 복사해서 그대로 담고있는 클래스 
		// String sql="select * from a_board where delyn='N' order by ORIGINBIDX desc, depth asc"; //컬럼마다 오더 방향 설정해주기 
	
		//like구문을 이용하여 특정 검색어를 구분하여 쿼리를 가지고 str에 담는다. 그리고 본쿼리에서 str변수로서 가지고간다. 
		String str ="";
		if(scri.getSearchType().equals("subject")) {
			str = "and subject like ?";
		}else {
			str = "and writer like ?";
		}
		
		//limit으로 제한
		String sql = "SELECT * FROM a_board WHERE DELYN='N' "+str+"  ORDER BY ORIGINBIDX desc, depth ASC limit ?,?  ";
		
		try{			
			
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, "%"+scri.getKeyword()+"%");	//keyword(검색창)에 입력되는 값을 첫번쨰?에 넣는다. 제목이면 제목의 ?,작성자면 작성자의?에 들어간다.
		pstmt.setInt(2, 1);  
		pstmt.setInt(3, 15);
		rs= pstmt.executeQuery(); 
		
		//반복문 실행. rs.next는 다음행의 값이 있으면 true 없으면 false를 반환한다. 값이 있으면 그 행으로 커서가 이동한다. 
		while(rs.next()){
			//반복할때마다 객체생성해서 객체마다 게시글을 하나씩담기
			bv = new BoardVo();
			//옮겨 담기
			bv.setBidx(rs.getInt("bidx"));
			bv.setSubject(rs.getString("subject"));
			bv.setWriter(rs.getString("writer"));
			bv.setWriteday(rs.getString("writeday"));
			bv.setLevel_(rs.getInt("level_"));
			
			alist.add(bv); //각각의 bv 객체를 alist에 추가한다. 
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
	
	
	
	
	
	//화면단에 데이터 출력(하나씩만 가져옴) // bean에 담아서 넘기기때문에 boardVo 객체를 생성하는 것은 데이터를 담을 bean을 만드는것
	public BoardVo boardSelectOne(int bidx) {
		BoardVo bv = null;
		ResultSet rs =null; // 순서까지도 그대로 복사
		
		String sql= "select * from a_board where bidx=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);	//쿼리를  bidx에 해당되는 모든 정보로 작성했음으로 하나만 받아오면 된다. //bidx로 한줄을 다 가져와서 rs에 담아주면된다.
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				//값이 존재하면 bean 만들기
				bv = new BoardVo();
				//bean에 담기 (타입때문에 한줄씩만 담김)
				bv.setBidx(rs.getInt("bidx"));
				bv.setOriginbidx(rs.getInt("originbidx")); //수정하기 답변하기 등의 기능을 사용하기 위해
				bv.setDepth(rs.getInt("depth"));
				bv.setLevel_(rs.getInt("level_"));
				
				bv.setSubject(rs.getString("subject"));
				bv.setContent(rs.getString("content"));
				bv.setWriter(rs.getString("writer"));
				bv.setWriteday(rs.getString("writeday"));
				bv.setFileName(rs.getString("fileName"));
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return bv;
	}
	
	
	
	//수정하는 메서드. 매개변수로 화면단에서 넘어온 데이터들 받을 수있도록하기
	public int modify(String subject, String content, String writer,String ip, int bidx) {

		int value=0;
		
		String sql = "update a_board set subject=?,content=?,writer=?,writeday=now(),ip=? where bidx=?";
			 
		try {
			pstmt =conn.prepareStatement(sql);
			
			pstmt.setString(1, subject);
			pstmt.setString(2, content);
			pstmt.setString(3, writer);
			pstmt.setString(4, ip);
			pstmt.setInt(5, bidx);
			
			value = pstmt.executeUpdate(); //실행문	
			//sql쿼리가 select이면 디비에서 데이터를 가져와서 처리하는 것이기 때문에 rs를 이용해서 담아야한다. 하지만 insert, update 등 데이터변경구문을 사용한다면
			//rs를 사용하는 것이 아니라 int value를 사용해서 1이면 성공 0이면 실패 값을 만들어 줄 수 있도록한다. 
		
			
				
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		return value;
	}
	
	
	public int delete(int bidx) {
		
		int value = 0;
		String sql = "update a_board set delyn='Y' where bidx=?";
	
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return value;
	}



	public int boardReply(BoardVo bv) //매개변수를 일일히 쓰는것이라 아니라 bean을 생성해서 담아서 넘기기 
	{	
		int value=0;
		String sql1 ="update a_board set depth = depth+1 where originbidx=? and depth > ?";	//원글의 depth올려줌
		String sql2 ="insert into a_board( ORIGINBIDX, DEPTH, LEVEL_, SUBJECT, WRITER, CONTENT, IP, MIDX)" //답변은
		+"values(?,?,?,?,?,?,?,?)";   
		
		//tranjection  두가지의 실행사항이 있을때 두가지를 소작업단위로 보고 소작업 두개가 모두 실행될 수있을때만 디비에 반영이된다. 
		//한꺼번에 모두수행되어야하는 연산. 마지막 줄에 오류가 하나나면 앞의 같은 작업단위로 묶인것들 모두 취소된다. 
	try {
		conn.setAutoCommit(false); //원래 자바에서는 실행하고나면 자동커밋되도록되어있는데 이것을 false로 바꿔줌
		pstmt = conn.prepareStatement(sql1); 		
		pstmt.setInt(1,bv.getOriginbidx());		//값을 매개변수에서 bv로 담아왔음으로 bv에서 꺼내서 쓴다.		
		pstmt.setInt(2,bv.getDepth());
		value = pstmt.executeUpdate();  
		
		pstmt = conn.prepareStatement(sql2); 		
		pstmt.setInt(1,bv.getOriginbidx());		//값을 매개변수에서 bv로 담아왔음으로 bv에서 꺼내서 쓴다.		
		pstmt.setInt(2,bv.getDepth()+1);
		pstmt.setInt(3,bv.getLevel_()+1);
		pstmt.setString(4, bv.getSubject());
		pstmt.setString(5, bv.getWriter());
		pstmt.setString(6, bv.getContent());
		pstmt.setString(7, bv.getIp());
		pstmt.setInt(8, bv.getMidx());
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
	
	
	
	
	
	public int boardTotal(SearchCriteria scri) {	//매개변수를 가지고 오면서 키워드검색이 가능해짐
		int cnt = 0;
		ResultSet rs= null;
	
		//사용자가 입력한 키워드 입력받을 수있도록 str변수만들기
		String str ="";
		if(scri.getSearchType().equals("subject")) {
			str = " and subject like ?";
		}else {
			str = " and writer like ?";
		}
		
		
		String sql ="select count(*) as cnt from a_board where delyn='N'"+str;	// 게시물 전체 개수	. // 사용자가 입력한 검색어가 들어간 게시물이 총 몇개인지 설정 	
		
		try {
			pstmt= conn.prepareStatement(sql);
			pstmt.setString(1, "%"+scri.getKeyword()+"%");		//쿼리를 실행하고 str부분은 위의 str을 참고하고 ?에는 화면에서 입력받은값을 가져와서 넣을 수있도록한다.
			rs= pstmt.executeQuery();
			
			if(rs.next()) {
				cnt= rs.getInt("cnt");
			}
		} catch (SQLException e) {
		
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				//conn.close();
			pstmt.close();
			} catch (SQLException e) {
				
			}
			
		}
		
		
		return cnt;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
