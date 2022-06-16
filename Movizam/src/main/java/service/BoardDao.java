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
	
	//�޼��徲���� �����ҋ� ��� ���� �����ϱ�
	public BoardDao(){
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	

	
	//�Խñ� �ۼ�
	public int boardWrite(String boardTitle, String boardContent, String boardWriter, String boardFilename,String boardIp, int midx, String boardType) {
		
		int value = 0;
		
		String str="";
		
		//�Խ���Ÿ�Կ� ���� �ۼ�
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
			pstmt.setString(3, boardWriter);	//���ǿ� ���� ��� �г��Ӱ� ����
			pstmt.setString(4, boardFilename);
			pstmt.setString(5, boardIp);
			pstmt.setInt(6, midx);
			
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
		return value;
				
	}
	
	
	
	
	
	//�Խñ� ����
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
	
	
	
	
	//�Խñ� ����
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
	
	
	//�Խñ� ���� 
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
		
		
	
	//�Խñ� ����Ʈ(�Ű����� �޾Ƽ� �˻�)(����¡)(�⺻)
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
			pstmt.setInt(2, ((scri.getPage()-1)*15+1));  //���� 1 �������̸� �Խñ� 1��. 2 �������� 16��
			pstmt.setInt(3, (scri.getPage()*15));	
			
			rs = pstmt.executeQuery();
//			System.out.println("rs: "+rs);
//			System.out.println("rs next: "+rs.next());
			
		while(rs.next()){
			
			bv = new BoardVo();
			//�Խù� ��� ���ٿ� ���� ������
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
	
	
	//(main)�Խñ� ����Ʈ(�Ű����� �޾Ƽ� �˻�)(����¡)(�⺻)
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
				//�Խù� ��� ���ٿ� ���� ������
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
	
	
	//�Խù� ��ü ���� (�˻����� ����� �Խù� ����)
	public int boardTotal(SearchCriteria scri) {
		int cnt = 0;
		ResultSet rs = null;
		
		String str ="";
		if(scri.getSearchType().equals("boardTitle")) {
			str = " and boardTitle like ?";
		}else if(scri.getSearchType().equals("boardWriter")){
			str = " and boardWriter like ?";
		}
		
		//��Ī ����� ����
		String sql ="select count(*) as cnt from board where boardDelYN ='N' "+ str;	// ���� ���ϴ� �Ŵϱ� order by �ʿ����
		
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
		
		//��Ī ����� ����
		String sql ="select count(*) as cnt from board where boardDelYN ='N' and boardType = 'free' "+ str;	// ���� ���ϴ� �Ŵϱ� order by �ʿ����
		
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
	
	
	
	
	
	
	//��ȸ�� �ø��� 
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
	
	//�Խñ�(��ȭ)�� ����
	public int boardTotal_film(SearchCriteria scri) {
		int cnt = 0;
		ResultSet rs = null;
		
		String str ="";
		if(scri.getSearchType().equals("boardTitle")) {
			str = " and boardTitle like ?";
		}else if(scri.getSearchType().equals("boardWriter")){
			str = " and boardWriter like ?";
		}
		
		//��Ī ����� ����
		String sql ="select count(*) as cnt from board where boardDelYN ='N' and boardType = 'film' "+ str;	// ���� ���ϴ� �Ŵϱ� order by �ʿ����
		
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
	
	
	//��õ�� �ø��� 
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
	
	
	
	// ������, ������ ����
	//Vo �� ���� ���� �ű涧�� HashMap�� ����Ҽ��� �ִ�. 
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
	
	
	
	
	
	
	//���� ������ ��ȸ�� ���� �Խñ� ����Ʈ(���� 5��)
	public ArrayList<BoardVo> boardHitMain(){
			
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
		BoardVo bv = null;
		ResultSet rs = null;
			
			
		//BIDX �������� ��ȸ�� �������� �����ϰ� ROWNUM���� ��������	
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
			//�Խù� ��� ���ٿ� ���� ������
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
	
	
	
	
	//���� ������ ���ƿ� ���� �Խñ� ����Ʈ(���� 5��)
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
				//�Խù� ��� ���ٿ� ���� ������
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
	
	
	//��ȸ�� ���� �Խñ� ����Ʈ(�Ű����� �޾Ƽ� �˻�)(����¡)
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
		
				
		//BIDX �������� ��ȸ�� �������� �����ϰ� ROWNUM���� ��������	
		String sql =" select * from board where boardDelYN ='N' "+ str +"order by boardHit desc limit ?, ? ";
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+scri.getKeyword()+"%");
			pstmt.setInt(2, ((scri.getPage()-1)*15+1));  //���� 1 �������̸� �Խñ� 1��. 2 �������� 16��
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
	
	
	
	
	//���ƿ� ���� �Խñ� ����Ʈ(�Ű����� �޾Ƽ� �˻�)(����¡)
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
			
					
			//BIDX �������� ��ȸ�� �������� �����ϰ� ROWNUM���� ��������	
			String sql =" select * from board where boardDelYN ='N' "+ str +"order by boardLike desc limit ?, ? ";
					
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%"+scri.getKeyword()+"%");
				pstmt.setInt(2, ((scri.getPage()-1)*15+1));  //���� 1 �������̸� �Խñ� 1��. 2 �������� 16��
				pstmt.setInt(3, (scri.getPage()*15));	
					
				rs = pstmt.executeQuery();
					
				//System.out.println("rs: "+ rs);
				//System.out.println("rs.next: "+ rs.next());
				
				
			while(rs.next()){
						
				bv = new BoardVo();
				//�Խù� ��� ���ٿ� ���� ������
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
	
	
	
		//��ȭ ��õ �Խñ� ����Ʈ(�Ű����� �޾Ƽ� �˻�)(����¡)(�⺻)
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
				pstmt.setInt(2, ((scri.getPage()-1)*15+1));  //���� 1 �������̸� �Խñ� 1��. 2 �������� 16��
				pstmt.setInt(3, (scri.getPage()*15));	
				
				rs = pstmt.executeQuery();
//				System.out.println("rs: "+rs);
//				System.out.println("rs next: "+rs.next());
				
			while(rs.next()){
				
				bv = new BoardVo();
				//�Խù� ��� ���ٿ� ���� ������
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
