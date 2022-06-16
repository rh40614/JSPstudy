package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import dbconn.Dbconn;
import domain.BoardVo;
import domain.SearchCriteria;

public class BoardDao {
	
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	
	//메서드쓰려고 생성할떄 디비도 같이 생성하기
	public BoardDao(){
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	

	
	//게시글 작성
	public int boardWrite(String boardTitle, String boardContent, String boardWriter, String boardFilename,String boardIp, int midx, String boardType) {
		
		int value = 0;
		
		String str="";
		
		//게시판타입에 따라 작성
		if(boardType.equals("free")) {
			
			str ="insert into board(BOARDTITLE,BOARDCONTENT,BOARDWRITER,BOARDLIKE,BOARDHIT,BOARDFILENAME,BOARDIP, midx, boardType) "
					+ "values(?,?,?,0,0,?,?,?,'free')";
			
		}else if(boardType.equals("film")) {
			
			str="insert into board(BOARDTITLE,BOARDCONTENT,BOARDWRITER,BOARDLIKE,BOARDHIT,BOARDFILENAME,BOARDIP, midx, boardType) "
				+ "values(?,?,?,0,0,?,?,?,'film')";
		}
		
		String sql = str;
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, boardTitle);
			pstmt.setString(2, boardContent);
			pstmt.setString(3, boardWriter);	//세션에 담은 멤버 닉네임값 넣음
			pstmt.setString(4, boardFilename);
			pstmt.setString(5, boardIp);
			pstmt.setInt(6, midx);
			
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
		return value;
				
	}
	
	
	
	
	
	//게시글 수정
	public int boardEdit (String boardTitle, String boardContent, String boardFilename,String boardIp, int bidx, String boardType) {
		
		int value = 0;
		
		String sql = "update board "
				+ "set boardTitle =?, boardContent =?, "
				+ "boardFilename =?, boardIp =?, boardType =? where bidx =? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, boardTitle);
			pstmt.setString(2, boardContent);
			pstmt.setString(3, boardFilename);
			pstmt.setString(4, boardIp);
			pstmt.setInt(5, bidx);
			pstmt.setString(6, boardType);
			
			value = pstmt.executeUpdate();
		
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return value;
	}
	
	
	
	
	//게시글 삭제
	public int delete(int bidx) {
		
		int value = 0;
		
		String sql = "update board set boardDelyn='Y' where bidx=?";
	
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
		return value;
	}
	
	
	//게시글 보기 
		public BoardVo boardSelectOne(int bidx) {
			
			BoardVo bv = null;
			ResultSet rs =null; 
			
			String sql= "select * from board where bidx=?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bidx);	
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					
					bv = new BoardVo();
			
					bv.setBidx(rs.getInt("bidx"));
					bv.setBoardTitle(rs.getString("boardTitle"));
					bv.setBoardWriter(rs.getString("boardWriter"));
					bv.setBoardWdate(rs.getString("boardWdate"));
					bv.setBoardContent(rs.getString("boardContent"));
					bv.setBoardFilename(rs.getString("boardFilename"));
					bv.setBoardHit(rs.getInt("boardHit"));		
					bv.setBoardLike(rs.getInt("boardLike"));
					bv.setMidx(rs.getInt("midx"));
					
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
			return bv;
		}
		
		
	
	//게시글 리스트(매개변수 받아서 검색)(페이징)(기본)
	public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri){
		
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
		BoardVo bv = null;
		ResultSet rs = null;
		
		String str ="";
		
		if(scri.getSearchType().equals("boardTitle")) {
			str = "and boardTitle like ?";
		}else {
			str = "and boardWriter like ?";
		}
		
//		String sql ="select * from ("
//				+ " select rownum as rnum, A.* from ("
//				+ "select * from board where boardDelYN ='N' and boardType='free' "+ str +"order by bidx desc) A"
//				+ ") B where rnum between ? and ? ";
		
		String sql = "select * from board where boardDelYN ='N' and boardType='free' "+ str +"order by bidx desc limit ?,? ";
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+scri.getKeyword()+"%");
			pstmt.setInt(2, ((scri.getPage()-1)*15+1));  //현재 1 페이지이면 게시글 1번. 2 페이지면 16번
			pstmt.setInt(3, (scri.getPage()*15));	
			
			rs = pstmt.executeQuery();
//			System.out.println("rs: "+rs);
//			System.out.println("rs next: "+rs.next());
			
		while(rs.next()){
			
			bv = new BoardVo();
			//게시물 목록 한줄에 나올 데이터
			bv.setBidx(rs.getInt("bidx"));
			//bv.setScreenBidx(rs.getInt("rnum"));
			bv.setBoardTitle(rs.getString("boardTitle"));
			bv.setBoardWriter(rs.getString("boardWriter"));
			bv.setBoardWdate(rs.getString("boardWdate"));
			bv.setBoardHit(rs.getInt("boardHit"));	
		
			alist.add(bv); 
			
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
		
		return alist;
		
	}
	
	
	//(main)게시글 리스트(매개변수 받아서 검색)(페이징)(기본)
		public ArrayList<BoardVo> boardSelectAll_main(SearchCriteria scri){
			
			ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
			BoardVo bv = null;
			ResultSet rs = null;
			
			String sql = "select * from board where boardDelYN ='N' and boardTitle like ? order by bidx desc";
					
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%"+scri.getKeyword()+"%");

				
				rs = pstmt.executeQuery();
				
//				System.out.println("rs: "+rs);
//				System.out.println("rs next: "+rs.next());
				
			while(rs.next()){
				
				bv = new BoardVo();
				//게시물 목록 한줄에 나올 데이터
				bv.setBidx(rs.getInt("bidx"));
				bv.setBoardTitle(rs.getString("boardTitle"));
				bv.setBoardWriter(rs.getString("boardWriter"));
				bv.setBoardWdate(rs.getString("boardWdate"));
				bv.setBoardHit(rs.getInt("boardHit"));	
				
				alist.add(bv); 
				
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
			
			return alist;
			
		}
	
	
	//게시물 전체 개수 (검색했을 경우의 게시물 갯수)
	public int boardTotal(SearchCriteria scri) {
		int cnt = 0;
		ResultSet rs = null;
		
		String str ="";
		if(scri.getSearchType().equals("boardTitle")) {
			str = " and boardTitle like ?";
		}else if(scri.getSearchType().equals("boardWriter")){
			str = " and boardWriter like ?";
		}
		
		//별칭 까먹지 말기
		String sql ="select count(*) as cnt from board where boardDelYN ='N' "+ str;	// 갯수 구하는 거니까 order by 필요없음
		
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
	
	
	public int boardTotal_free(SearchCriteria scri) {
		int cnt = 0;
		ResultSet rs = null;
		
		String str ="";
		if(scri.getSearchType().equals("boardTitle")) {
			str = " and boardTitle like ?";
		}else if(scri.getSearchType().equals("boardWriter")){
			str = " and boardWriter like ?";
		}
		
		//별칭 까먹지 말기
		String sql ="select count(*) as cnt from board where boardDelYN ='N' and boardType = 'free' "+ str;	// 갯수 구하는 거니까 order by 필요없음
		
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
	
	
	
	
	
	
	//조회수 올리기 
	public int boardHitCnt(int bidx) {
		int cnt =0;
		
		String sql = "update board set boardHit = boardHit + 1 WHERE bidx = ? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			
			cnt = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		//System.out.println("cnt: "+cnt);
		
		return cnt;
		
	}
	
	//게시글(영화)총 갯수
	public int boardTotal_film(SearchCriteria scri) {
		int cnt = 0;
		ResultSet rs = null;
		
		String str ="";
		if(scri.getSearchType().equals("boardTitle")) {
			str = " and boardTitle like ?";
		}else if(scri.getSearchType().equals("boardWriter")){
			str = " and boardWriter like ?";
		}
		
		//별칭 까먹지 말기
		String sql ="select count(*) as cnt from board where boardDelYN ='N' and boardType = 'film' "+ str;	// 갯수 구하는 거니까 order by 필요없음
		
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
	
	
	//추천수 올리기 
	public int boardLikeCnt(int bidx) {
		int cnt =0;
		
		String sql = "update board set boardLike = boardLike + 1 WHERE bidx = ? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			
			cnt = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
		return cnt;
		
	}	
	
	
	
	// 이전글, 다음글 연결
	//Vo 에 없는 값을 옮길때는 HashMap을 사용할수도 있다. 
	public HashMap<String, Integer> bidxPreNext(int bidx) {
		
		ResultSet rs = null;
		HashMap<String, Integer> map = null;
				
		String sql ="SELECT * from ("
				+ "SELECT bidx,"
				+ "LAG(bidx, 1, -1) OVER(ORDER BY bidx) AS pre, "
				+ "LEAD(bidx) OVER(ORDER BY bidx) AS next "
				+ "FROM board WHERE boardDelYN='N' "
				+ "GROUP BY bidx) A WHERE bidx = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery();
			
			
		if(rs.next()) {
			//System.out.println("rs pre: "+ rs.getInt("pre"));
			map = new HashMap<String,Integer>();
			map.put("pre", rs.getInt("pre"));
			map.put("next", rs.getInt("next"));
			
		}
			
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}
	
		
		return map;
	}
	
	
	
	
	
	
	//메인 페이지 조회수 높은 게시글 리스트(상위 5개)
	public ArrayList<BoardVo> boardHitMain(){
			
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
		BoardVo bv = null;
		ResultSet rs = null;
			
			
		//BIDX 순서말고 조회수 기준으로 나열하고 ROWNUM으로 가져오기	
//		String sql ="select * from ("
//				+ " select rownum as rnum, A.* from ("
//				+ " select * from board where boardDelYN ='N' order by boardHit desc) A"
//				+ " ) B where rnum limit 1,5";
			
		
		String sql ="select * from board where boardDelYN ='N' order by boardHit desc limit 1,5";
		try {
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			//System.out.println("rs: "+ rs);
			//System.out.println("rsnext: "+ rs.next());
			
				
		while(rs.next()){
				
			bv = new BoardVo();
			//게시물 목록 한줄에 나올 데이터
			bv.setBidx(rs.getInt("bidx"));
			//bv.setScreenBidx(rs.getInt("rnum"));
			bv.setBoardTitle(rs.getString("boardTitle"));
			bv.setBoardWriter(rs.getString("boardWriter"));
			bv.setBoardWdate(rs.getString("boardWdate"));
			bv.setBoardHit(rs.getInt("boardHit"));	
			
			alist.add(bv); 
				
		}	
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}finally {
			try {
			//	rs.close();
				pstmt.close();
				
			} catch (SQLException e) {
				
			}
			
		}
			
			
		return alist;
			
	}
	
	
	
	
	//메인 페이지 좋아요 높은 게시글 리스트(상위 5개)
		public ArrayList<BoardVo> boardLikeMain(){
				
			ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
			BoardVo bv = null;
			ResultSet rs = null;
						
//			String sql ="select * from ("
//					+ " select rownum as rnum, A.* from ("
//					+ " select * from board where boardDelYN ='N' order by boardLike desc) A"
//					+ " )B where rnum limit 1,5";
			
			String sql = "select * from board where boardDelYN ='N' order by boardLike desc limit 1,5";
				
			try {
				pstmt = conn.prepareStatement(sql);
				
				rs = pstmt.executeQuery();
					
					
			while(rs.next()){
					
				bv = new BoardVo();
				//게시물 목록 한줄에 나올 데이터
				bv.setBidx(rs.getInt("bidx"));
				//bv.setScreenBidx(rs.getInt("rnum"));
				bv.setBoardTitle(rs.getString("boardTitle"));
				bv.setBoardWriter(rs.getString("boardWriter"));
				bv.setBoardWdate(rs.getString("boardWdate"));	
				bv.setBoardLike(rs.getInt("boardLike"));
				alist.add(bv); 
					
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
				
				
			return alist;
				
		}
	
	
	//조회수 높은 게시글 리스트(매개변수 받아서 검색)(페이징)
	public ArrayList<BoardVo> boardHitAll(SearchCriteria scri){
				
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
		BoardVo bv = null;
		ResultSet rs = null;
				
		String str ="";
		if(scri.getSearchType().equals("boardTitle")) {
			str = " and boardTitle like ?";
		}else {
			str = " and boardWriter like ?";
		}
		
				
		//BIDX 순서말고 조회수 기준으로 나열하고 ROWNUM으로 가져오기	
		String sql =" select * from board where boardDelYN ='N' "+ str +"order by boardHit desc limit ?, ? ";
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+scri.getKeyword()+"%");
			pstmt.setInt(2, ((scri.getPage()-1)*15+1));  //현재 1 페이지이면 게시글 1번. 2 페이지면 16번
			pstmt.setInt(3, (scri.getPage()*15));	
				
			rs = pstmt.executeQuery();
				
			//System.out.println("rs: "+ rs.next());
			
		while(rs.next()){
					
			bv = new BoardVo();
			
			bv.setBidx(rs.getInt("bidx"));
		//	bv.setScreenBidx(rs.getInt("rnum"));
			bv.setBoardTitle(rs.getString("boardTitle"));
			bv.setBoardWriter(rs.getString("boardWriter"));
			bv.setBoardWdate(rs.getString("boardWdate"));
			bv.setBoardHit(rs.getInt("boardHit"));
			
			
			alist.add(bv); 
							}	
				
		} catch (SQLException e) {
				
			e.printStackTrace();
		}
				
			//System.out.println("alist"+alist);

		return alist;
			
	}
	
	
	
	
	//좋아요 높은 게시글 리스트(매개변수 받아서 검색)(페이징)
		public ArrayList<BoardVo> boardLikeAll(SearchCriteria scri){
					
			ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
			BoardVo bv = null;
			ResultSet rs = null;
					
					
			String str ="";
			if(scri.getSearchType().equals("boardTitle")) {
				str = " and boardTitle like ?";
			}else {
				str = " and boardWriter like ?";
			}
			
					
			//BIDX 순서말고 조회수 기준으로 나열하고 ROWNUM으로 가져오기	
			String sql =" select * from board where boardDelYN ='N' "+ str +"order by boardLike desc limit ?, ? ";
					
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%"+scri.getKeyword()+"%");
				pstmt.setInt(2, ((scri.getPage()-1)*15+1));  //현재 1 페이지이면 게시글 1번. 2 페이지면 16번
				pstmt.setInt(3, (scri.getPage()*15));	
					
				rs = pstmt.executeQuery();
					
				//System.out.println("rs: "+ rs);
				//System.out.println("rs.next: "+ rs.next());
				
				
			while(rs.next()){
						
				bv = new BoardVo();
				//게시물 목록 한줄에 나올 데이터
				bv.setBidx(rs.getInt("bidx"));
				//bv.setScreenBidx(rs.getInt("rnum"));
				bv.setBoardTitle(rs.getString("boardTitle"));
				bv.setBoardWriter(rs.getString("boardWriter"));
				bv.setBoardWdate(rs.getString("boardWdate"));
				bv.setBoardHit(rs.getInt("boardHit"));
				bv.setBoardLike(rs.getInt("boardLike"));
				
				alist.add(bv); 
						
			}	
					
			} catch (SQLException e) {
				
				e.printStackTrace();
				
			}finally {
				try {
					//rs.close();
					pstmt.close();
					
				} catch (SQLException e) {
					
				}
				
			}
					
				
			return alist;
				
		}	
	
	
	
		//영화 추천 게시글 리스트(매개변수 받아서 검색)(페이징)(기본)
		public ArrayList<BoardVo> boardSelectAll_film(SearchCriteria scri){
			
			ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
			BoardVo bv = null;
			ResultSet rs = null;
			
			String str ="";
			
			if(scri.getSearchType().equals("boardTitle")) {
				str = "and boardTitle like ?";
			}else {
				str = "and boardWriter like ?";
			}
			
//			String sql ="select * from ("
//					+ " select rownum as rnum, A.* from ("
//					+ "select * from board where boardDelYN ='N' and boardType='film' "+ str +"order by bidx desc) A"
//					+ ") B where rnum between ? and ? ";

			String sql ="select * from board where boardDelYN ='N' and boardType='film' "+ str +"order by bidx desc  limit ?, ? ";
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%"+scri.getKeyword()+"%");
				pstmt.setInt(2, ((scri.getPage()-1)*15+1));  //현재 1 페이지이면 게시글 1번. 2 페이지면 16번
				pstmt.setInt(3, (scri.getPage()*15));	
				
				rs = pstmt.executeQuery();
//				System.out.println("rs: "+rs);
//				System.out.println("rs next: "+rs.next());
				
			while(rs.next()){
				
				bv = new BoardVo();
				//게시물 목록 한줄에 나올 데이터
				bv.setBidx(rs.getInt("bidx"));
			//	bv.setScreenBidx(rs.getInt("rnum"));
				bv.setBoardTitle(rs.getString("boardTitle"));
				bv.setBoardWriter(rs.getString("boardWriter"));
				bv.setBoardWdate(rs.getString("boardWdate"));
				bv.setBoardHit(rs.getInt("boardHit"));	
				
				alist.add(bv); 
				
			}	
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
			
			return alist;
			
		}

	
		
		
		
		
		
	
}
