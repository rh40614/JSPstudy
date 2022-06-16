package mysqlProject.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mysqlProject.domain.SearchCriteria;
import mysqlProject.dbconn.Dbconn;
import mysqlProject.domain.BoardVo;

public class BoardDao {
	//연결객체
	private Connection conn;
	//구문객체
	private PreparedStatement pstmt;
	
	public BoardDao() {
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	public int Write(BoardVo bv){
		int value = 0, value2 = 0;	
		int bidx = 0;
		String sql="INSERT INTO pj_board(midx, boardwriter, boardcategory, boardtitle, boardcontent, boardnotice,writerprofile)"
				+ " VALUES (?,?,?,?,?,?,?)";
				
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bv.getMidx());
			pstmt.setString(2, bv.getWriter());
			pstmt.setInt(3, bv.getCategory());
			pstmt.setString(4, bv.getTitle());
			pstmt.setString(5, bv.getContent());
			pstmt.setString(6, bv.getNotice());
			pstmt.setString(7, bv.getProfile());
			value = pstmt.executeUpdate();
			if(value == 1) {
				sql = "select max(bidx) as bidx from pj_board";
				pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					bidx = rs.getInt("bidx");
					sql = "INSERT INTO pj_board_detail(originbidx,writeip)"
							+ "VALUES(?,?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, bidx);
					pstmt.setString(2, bv.getBoardip());
					value2 = pstmt.executeUpdate();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}			
		return value2;		
	}
	
	public ArrayList<BoardVo> boardSelectAll() {
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
		ResultSet rs = null;
		String sql = "SELECT * FROM pj_board a JOIN pj_board_detail b "
				+ "ON a.bidx = b.bdidx left JOIN (SELECT bidx,count(*) AS cnt FROM pj_comment WHERE delyn='N' GROUP BY BIDX) c "
				+ "ON a.bidx = c.bidx WHERE a.delyn ='N' ORDER BY originbidx DESC, depth asc";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			//다음값이 존재할동안 반복
			while(rs.next()) {
				BoardVo bv = new BoardVo();
				bv.setCategory(rs.getInt("boardcategory"));
				bv.setTitle(rs.getString("boardtitle"));
				bv.setWriter(rs.getString("boardwriter"));
				bv.setNotice(rs.getString("boardnotice"));
				bv.setBidx(rs.getInt("bidx"));
				bv.setBoardComment(rs.getInt("cnt"));
				bv.setLevel_(rs.getInt("level_"));
				Date now = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
				SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
				SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("MM-dd");
				String strnow = simpleDateFormat.format(now);
				String strWriteDay = simpleDateFormat.format(rs.getDate("writeday"));
				String strViewTime = simpleDateFormat2.format(rs.getDate("writeday"));
				String strViewDay = simpleDateFormat3.format(rs.getDate("writeday"));
				
				if(strnow.equals(strWriteDay)) {
					bv.setWriteday(strViewTime);
				}else {
					bv.setWriteday(strViewDay);
				}
				alist.add(bv);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {			
			close();			
		}		
		return alist;
	}
	
	
	
	public BoardVo boardContent(int bidx) {
		BoardVo bv = null;
		ResultSet rs = null;
		String sql="SELECT * FROM pj_board a JOIN pj_board_detail b ON a.bIDX = b.bDIDX join pj_member_detail c on a.midx = c.mdidx "
				+ "LEFT JOIN (SELECT bidx,COUNT(*) AS cnt FROM pj_comment WHERE delyn='N' AND bidx = ? GROUP BY bidx)d ON a.bidx=d.bidx WHERE DELYN ='N' AND a.bidx=?";
		
		
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bidx);
				pstmt.setInt(2, bidx);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					bv = new BoardVo();
					bv.setBidx(rs.getInt("bidx"));
					bv.setMidx(rs.getInt("midx"));
					bv.setWriter(rs.getString("boardwriter"));
					bv.setCategory(rs.getInt("boardcategory"));
					bv.setTitle(rs.getString("boardtitle"));
					bv.setContent(rs.getString("boardcontent"));
					bv.setOriginbidx(rs.getInt("originbidx"));
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String strWriteDay = simpleDateFormat.format(rs.getDate("writeday"));
					bv.setWriteday(strWriteDay);
					bv.setDepth(rs.getInt("depth"));
					bv.setLevel_(rs.getInt("level_"));
					bv.setBoardViews(rs.getInt("boardviews"));
					bv.setProfile(rs.getString("memberprofile"));
					bv.setNotice(rs.getString("boardnotice"));
					bv.setBoardComment(rs.getInt("cnt"));
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					pstmt.close();
					conn.close();
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		return bv;
	}
	
	public int boardDelete(int bidx) {
		int value = 0;
		String sql = "update pj_board set delyn='Y' where bidx = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			value = pstmt.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		
		return value;
	}
	
	public int boardModify(BoardVo bv){
		int value = 0;	
		
		String sql = "update pj_board set boardtag=?, title=?, content=?, mainnotice=? where bidx=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bv.getCategory());
			pstmt.setString(2, bv.getTitle());
			pstmt.setString(3, bv.getContent());
			pstmt.setString(4, bv.getNotice());
			pstmt.setInt(5, bv.getBidx());
			value = pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		return value;		
	}
	public int boardModifyIp(int bidx , String ip) {
		System.out.println("ip확인창");
		int value = 0;
		ResultSet rs = null;
		String ip_ = "";
		String sql = "select writeip from pj_board_detail where bdidx=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery();
			ip_ = rs.getString("writeip");
			System.out.println("ip = " + ip_);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if(ip_.equals(ip)) {
			return value = 1;
		}
		
		sql = "update pj_board_detail set writeip=? where bdidx=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ip);
			pstmt.setInt(2, bidx);
			value = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return value;
	}
	
	public int boardSearch(SearchCriteria scri) {
		int cnt = 0;
		int dbs = 0;
		ResultSet rs = null;
		
		String str = "";
		
		if(scri.getSearchType().equals("title")) {
			str = "and title like ?";
		}else if(scri.getSearchType().equals("content")){
			str = "and content like ?";
		}else if(scri.getSearchType().equals("writer")) {
			str = "and writer like ?";
		}else {
			str = "and title like ? or content like ?";
			dbs = 1;
		}
		String sql = "select count(*) as cnt from pj_board where delyn='N' "+str+"" ;
		
		try {
			pstmt = conn.prepareStatement(sql);
			if(dbs == 0) {
				pstmt.setString(1, "%"+scri.getKeyword()+"%");	
			}else {
				pstmt.setString(1, "%"+scri.getKeyword()+"%");
				pstmt.setString(2, "%"+scri.getKeyword()+"%");
			}
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				cnt = rs.getInt("cnt");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return cnt;
	}
	
	public ArrayList<BoardVo> boardSearchAll(SearchCriteria scri) {
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
		ResultSet rs = null;
		int dbs = 0;
		
		String str = "";
		String sql = "";
		if(scri.getSearchType().equals("title")) {
			str = "and title like ?";
		}else if(scri.getSearchType().equals("content")){
			str = "and content like ?";
		}else if(scri.getSearchType().equals("writer")) {
			str = "and writer like ?";
		}else {
			str = "and title like ? or content like ?";
			dbs = 1;
		}		
		if(scri.getCategory()==0) {
			sql = "SELECT * FROM("
					+"SELECT ROWNUM AS rnum, A.* FROM("
					+"SELECT * FROM pj_BOARD a JOIN pj_board_detail b ON a.bidx = b.bdidx "
					+"left JOIN (SELECT bidx,count(*) AS cnt FROM pj_comment WHERE delyn='N' GROUP BY BIDX) c ON a.bidx = c.bidx "
					+"WHERE DELYN='N' "+str+" order by a.bidx desc) A"
					+")B WHERE rnum BETWEEN ? AND ? and delyn = 'N'";
		}
		else {
			sql = "SELECT * FROM("
					+"SELECT ROWNUM AS rnum, A.* FROM("
					+"SELECT * FROM pj_BOARD a JOIN pj_board_detail b ON a.bidx = b.bdidx "
					+"left JOIN (SELECT bidx,count(*) AS cnt FROM pj_comment WHERE delyn='N' GROUP BY BIDX) c ON a.bidx = c.bidx "
					+"WHERE DELYN='N' and boardtag=? "+str+" order by a.bidx desc) A"
					+")B WHERE rnum BETWEEN ? AND ? and delyn = 'N'";
		}
		try {
			pstmt = conn.prepareStatement(sql);
			if(scri.getCategory()==0) {
				if(dbs == 0) {
					pstmt.setString(1, "%"+scri.getKeyword()+"%");
					pstmt.setInt(2, (scri.getPage()-1)*scri.getPerPageNum()+1);
					pstmt.setInt(3, scri.getPage()*scri.getPerPageNum());
				}
				else {
					pstmt.setString(1, "%"+scri.getKeyword()+"%");
					pstmt.setString(2, "%"+scri.getKeyword()+"%");
					pstmt.setInt(3, (scri.getPage()-1)*scri.getPerPageNum()+1);
					pstmt.setInt(4, scri.getPage()*scri.getPerPageNum());
				}
			}else {
				if(dbs == 0) {
					pstmt.setInt(1, scri.getCategory());
					pstmt.setString(2, "%"+scri.getKeyword()+"%");
					pstmt.setInt(3, (scri.getPage()-1)*scri.getPerPageNum()+1);
					pstmt.setInt(4, scri.getPage()*scri.getPerPageNum());
				}
				else {
					pstmt.setInt(1, scri.getCategory());
					pstmt.setString(2, "%"+scri.getKeyword()+"%");
					pstmt.setString(3, "%"+scri.getKeyword()+"%");
					pstmt.setInt(4, (scri.getPage()-1)*scri.getPerPageNum()+1);
					pstmt.setInt(5, scri.getPage()*scri.getPerPageNum());
				}
			}
			
			rs = pstmt.executeQuery();
			
			//다음값이 존재할동안 반복
			while(rs.next()) {
				BoardVo bv = new BoardVo();
				bv.setCategory(rs.getInt("boardtag"));
				bv.setTitle(rs.getString("title"));
				bv.setWriter(rs.getString("writer"));
				bv.setNotice(rs.getString("mainnotice"));
				bv.setBidx(rs.getInt("bidx"));
				bv.setContent(rs.getString("content"));
				bv.setLevel_(rs.getInt("level_"));
				bv.setBoardComment(rs.getInt("cnt"));
				Date now = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
				SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
				SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("MM-dd");
				String strnow = simpleDateFormat.format(now);
				String strWriteDay = simpleDateFormat.format(rs.getDate("writeday"));
				String strViewTime = simpleDateFormat2.format(rs.getDate("writeday"));
				String strViewDay = simpleDateFormat3.format(rs.getDate("writeday"));
				
				if(strnow.equals(strWriteDay)) {
					bv.setWriteday(strViewTime);
				}else {
					bv.setWriteday(strViewDay);
				}
				alist.add(bv);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {			
			close();		
		}		
		return alist;
	}
	
	public int boardList(SearchCriteria scri) {
		int cnt = 0;
		ResultSet rs = null;
		int dbs = 0;
		int sw = 0;
		String sql ="";
		String str = "";
		
		if(scri.getSearchType().equals("title")) {
			str = "and title like ?";
		}else if(scri.getSearchType().equals("content")){
			str = "and content like ?";
		}else if(scri.getSearchType().equals("writer")) {
			str = "and writer like ?";
		}else if(scri.getSearchType().equals("title-content")){
			str = "and title like ? or content like ?";
			dbs = 1;
		}else {
			sw = 1;
		}
		if(sw==0) {
			if(scri.getCategory() ==0) {
				sql = "select count(*) as cnt from pj_board a JOIN pj_board_detail b ON a.bidx = b.bdidx where delyn='N' "+str+"" ;
			}else {
				sql = "select count(*) as cnt from pj_board a JOIN pj_board_detail b ON a.bidx = b.bdidx where delyn='N' and boardtag=? "+str+"" ;
			}
		}else {
			if(scri.getCategory() ==0) {
				sql = "select count(*) as cnt from pj_board a JOIN pj_board_detail b ON a.bidx = b.bdidx where delyn='N'" ;
			}else {
				sql = "select count(*) as cnt from pj_board a JOIN pj_board_detail b ON a.bidx = b.bdidx where delyn='N' and boardtag=?" ;
			}
		}
		
		try {
			pstmt = conn.prepareStatement(sql);
			if(sw==0) {
				if(dbs == 0) {
					if(scri.getCategory()!=0) {
						pstmt.setInt(1, scri.getCategory());
						pstmt.setString(2, "%"+scri.getKeyword()+"%");
					}else {
						pstmt.setString(1, "%"+scri.getKeyword()+"%");
					}
				}else {
					if(scri.getCategory()!=0) {
						pstmt.setInt(1, scri.getCategory());
						pstmt.setString(2, "%"+scri.getKeyword()+"%");
						pstmt.setString(3, "%"+scri.getKeyword()+"%");
					}else {
						pstmt.setString(1, "%"+scri.getKeyword()+"%");
						pstmt.setString(2, "%"+scri.getKeyword()+"%");
					}
				}
			}else {
				if(scri.getCategory()!=0) {
					pstmt.setInt(1, scri.getCategory());
				}
			}
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				cnt = rs.getInt("cnt");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cnt;
	}
	
	public ArrayList<BoardVo> boardListAll(SearchCriteria scri) {
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
		ResultSet rs = null;
		String sql = "";
		if(scri.getCategory() == 0) {
			sql = "SELECT * FROM pj_BOARD a JOIN pj_board_detail b ON a.bidx = b.bdidx "
					+"left JOIN (SELECT bidx,count(*) AS cnt FROM pj_comment WHERE delyn='N' GROUP BY BIDX) c ON a.bidx = c.bidx "
					+"WHERE a.DELYN='N' order by originbidx desc, depth asc limit ?,?";
		}else {
			/*
			 * sql = "SELECT * FROM(" +"SELECT ROWNUM AS rnum, A.* FROM("
			 * +"SELECT * FROM pj_BOARD a JOIN pj_board_detail b ON a.bidx = b.bdidx "
			 * +"left JOIN (SELECT bidx,count(*) AS cnt FROM pj_comment WHERE delyn='N' GROUP BY BIDX) c ON a.bidx = c.bidx "
			 * +"WHERE a.DELYN='N' and boardtag = ? order by a.bidx desc) A"
			 * +")B WHERE rnum BETWEEN ? AND ? and delyn = 'N' order by originbidx desc, depth asc"
			 * ;
			 */
			sql = "SELECT * FROM pj_BOARD a JOIN pj_board_detail b ON a.bidx = b.bdidx "
			+"left JOIN (SELECT bidx,count(*) AS cnt FROM pj_comment WHERE delyn='N' GROUP BY BIDX) c ON a.bidx = c.bidx "
			+"WHERE a.DELYN='N' and boardtag = ? order by originbidx desc, depth asc limit ?,?";
		}		
		try {
			pstmt = conn.prepareStatement(sql);
			if(scri.getCategory()==0) {
				pstmt.setInt(1, (scri.getPage()-1)*scri.getPerPageNum());
				pstmt.setInt(2, scri.getPage()*scri.getPerPageNum());
			}else {
				pstmt.setInt(1, scri.getCategory());
				pstmt.setInt(2, (scri.getPage()-1)*scri.getPerPageNum());
				pstmt.setInt(3, scri.getPage()*scri.getPerPageNum());
			}
			rs = pstmt.executeQuery();
			
			//다음값이 존재할동안 반복
			while(rs.next()) {
				BoardVo bv = new BoardVo();
				bv.setCategory(rs.getInt("boardcategory"));
				bv.setTitle(rs.getString("boardtitle"));
				bv.setWriter(rs.getString("boardwriter"));
				bv.setNotice(rs.getString("boardnotice"));
				bv.setBidx(rs.getInt("bidx"));
				bv.setContent(rs.getString("boardcontent"));
				bv.setBoardComment(rs.getInt("cnt"));
				bv.setLevel_(rs.getInt("level_"));
				Date now = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
				SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
				SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("MM-dd");
				String strnow = simpleDateFormat.format(now);
				String strWriteDay = simpleDateFormat.format(rs.getDate("writeday"));
				String strViewTime = simpleDateFormat2.format(rs.getDate("writeday"));
				String strViewDay = simpleDateFormat3.format(rs.getDate("writeday"));
				
				if(strnow.equals(strWriteDay)) {
					bv.setWriteday(strViewTime);
				}else {
					bv.setWriteday(strViewDay);
				}
				alist.add(bv);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {			
			close();		
		}		
		return alist;
	}
	
	public int reply(BoardVo bv){
		int value = 0, value2 = 0;	
		String sql = "update a_board set depth=depth+1 where originbidx=? and depth > ?";
		
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bv.getOriginbidx());
			pstmt.setInt(2, bv.getDepth());
			value = pstmt.executeUpdate();
			if(value == 1) {
				sql = "INSERT INTO pj_board(midx, writer, boardtag, title, content) VALUES (?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bv.getMidx());
				pstmt.setString(2, bv.getWriter());
				pstmt.setInt(3, bv.getCategory());
				pstmt.setString(4, bv.getTitle());
				pstmt.setString(5, bv.getContent());
				value = pstmt.executeUpdate();
				if(value == 1) {
					sql="INSERT INTO pj_board_detail(originbidx,writeip,level_,depth)"
							+ "VALUES(?,?,1,?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, bv.getBidx());
					pstmt.setString(2, bv.getBoardip());
					pstmt.setInt(3, bv.getDepth()+1);
					value2 = pstmt.executeUpdate();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}			
		return value2;		
	}
	
	public ArrayList<BoardVo> boardGalleryAll(SearchCriteria scri) {
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
		ResultSet rs = null;
		String sql = "";
		if(scri.getCategory() == 0) {
			sql = "SELECT * FROM("
					+"SELECT ROWNUM AS rnum, A.* FROM("
					+"SELECT * FROM pj_BOARD a JOIN pj_board_detail b ON a.bidx = b.bdidx "
					+"left JOIN (SELECT bidx,count(*) AS cnt FROM pj_comment WHERE delyn='N' GROUP BY BIDX) c ON a.bidx = c.bidx "
					+"WHERE a.DELYN='N' order by a.bidx desc) A"
					+")B WHERE rnum BETWEEN ? AND ? and delyn = 'N' order by originbidx desc, depth asc";
		}else {
			sql = "SELECT * FROM("
					+"SELECT ROWNUM AS rnum, A.* FROM("
					+"SELECT * FROM pj_BOARD a JOIN pj_board_detail b ON a.bidx = b.bdidx "
					+"left JOIN (SELECT bidx,count(*) AS cnt FROM pj_comment WHERE delyn='N' GROUP BY BIDX) c ON a.bidx = c.bidx "
					+"WHERE a.DELYN='N' and boardtag = ? order by a.bidx desc) A"
					+")B WHERE rnum BETWEEN ? AND ? and delyn = 'N' order by originbidx desc, depth asc";
		}		
		try {
			pstmt = conn.prepareStatement(sql);
			if(scri.getCategory()==0) {
				pstmt.setInt(1, (scri.getPage()-1)*scri.getPerPageNum()+1);
				pstmt.setInt(2, scri.getPage()*scri.getPerPageNum());
			}else {
				pstmt.setInt(1, scri.getCategory());
				pstmt.setInt(2, (scri.getPage()-1)*scri.getPerPageNum()+1);
				pstmt.setInt(3, scri.getPage()*scri.getPerPageNum());
			}
			rs = pstmt.executeQuery();
			
			//다음값이 존재할동안 반복
			while(rs.next()) {
				BoardVo bv = new BoardVo();
				bv.setCategory(rs.getInt("boardtag"));
				bv.setTitle(rs.getString("title"));
				bv.setWriter(rs.getString("writer"));
				bv.setNotice(rs.getString("mainnotice"));
				bv.setBidx(rs.getInt("bidx"));
				String str = rs.getString("content");
				String piece = str.substring(str.indexOf("<img"));
				
				bv.setContent(piece);
				bv.setBoardComment(rs.getInt("cnt"));
				bv.setLevel_(rs.getInt("level_"));
				Date now = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
				SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
				SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("MM-dd");
				String strnow = simpleDateFormat.format(now);
				String strWriteDay = simpleDateFormat.format(rs.getDate("writeday"));
				String strViewTime = simpleDateFormat2.format(rs.getDate("writeday"));
				String strViewDay = simpleDateFormat3.format(rs.getDate("writeday"));
				if(strnow.equals(strWriteDay)) {
					bv.setWriteday(strViewTime);
				}else {
					bv.setWriteday(strViewDay);
				}
				alist.add(bv);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {			
			close();		
		}		
		return alist;
	}
	
	public ArrayList<BoardVo> boardGallerySearchAll(SearchCriteria scri) {
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
		ResultSet rs = null;
		int dbs = 0;
		
		String str = "";
		String sql = "";
		if(scri.getSearchType().equals("title")) {
			str = "and title like ?";
		}else if(scri.getSearchType().equals("content")){
			str = "and content like ?";
		}else if(scri.getSearchType().equals("writer")) {
			str = "and writer like ?";
		}else {
			str = "and title like ? or content like ?";
			dbs = 1;
		}		
		if(scri.getCategory()==0) {
			sql = "SELECT * FROM("
					+"SELECT ROWNUM AS rnum, A.* FROM("
					+"SELECT * FROM pj_BOARD a JOIN pj_board_detail b ON a.bidx = b.bdidx "
					+"left JOIN (SELECT bidx,count(*) AS cnt FROM pj_comment WHERE delyn='N' GROUP BY BIDX) c ON a.bidx = c.bidx "
					+"WHERE DELYN='N' "+str+" order by a.bidx desc) A"
					+")B WHERE rnum BETWEEN ? AND ? and delyn = 'N'";
		}
		else {
			sql = "SELECT * FROM("
					+"SELECT ROWNUM AS rnum, A.* FROM("
					+"SELECT * FROM pj_BOARD a JOIN pj_board_detail b ON a.bidx = b.bdidx "
					+"left JOIN (SELECT bidx,count(*) AS cnt FROM pj_comment WHERE delyn='N' GROUP BY BIDX) c ON a.bidx = c.bidx "
					+"WHERE DELYN='N' and boardtag=? "+str+" order by a.bidx desc) A"
					+")B WHERE rnum BETWEEN ? AND ? and delyn = 'N'";
		}
		try {
			pstmt = conn.prepareStatement(sql);
			if(scri.getCategory()==0) {
				if(dbs == 0) {
					pstmt.setString(1, "%"+scri.getKeyword()+"%");
					pstmt.setInt(2, (scri.getPage()-1)*scri.getPerPageNum()+1);
					pstmt.setInt(3, scri.getPage()*scri.getPerPageNum());
				}
				else {
					pstmt.setString(1, "%"+scri.getKeyword()+"%");
					pstmt.setString(2, "%"+scri.getKeyword()+"%");
					pstmt.setInt(3, (scri.getPage()-1)*scri.getPerPageNum()+1);
					pstmt.setInt(4, scri.getPage()*scri.getPerPageNum());
				}
			}else {
				if(dbs == 0) {
					pstmt.setInt(1, scri.getCategory());
					pstmt.setString(2, "%"+scri.getKeyword()+"%");
					pstmt.setInt(3, (scri.getPage()-1)*scri.getPerPageNum()+1);
					pstmt.setInt(4, scri.getPage()*scri.getPerPageNum());
				}
				else {
					pstmt.setInt(1, scri.getCategory());
					pstmt.setString(2, "%"+scri.getKeyword()+"%");
					pstmt.setString(3, "%"+scri.getKeyword()+"%");
					pstmt.setInt(4, (scri.getPage()-1)*scri.getPerPageNum()+1);
					pstmt.setInt(5, scri.getPage()*scri.getPerPageNum());
				}
			}
			
			rs = pstmt.executeQuery();
			
			//다음값이 존재할동안 반복
			while(rs.next()) {
				BoardVo bv = new BoardVo();
				bv.setCategory(rs.getInt("boardtag"));
				bv.setTitle(rs.getString("title"));
				bv.setWriter(rs.getString("writer"));
				bv.setBidx(rs.getInt("bidx"));
				String str2 = rs.getString("content");
				String piece = str2.substring(str2.indexOf("<img"));
				bv.setContent(piece);
				bv.setBoardComment(rs.getInt("cnt"));
				alist.add(bv);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {			
			close();		
		}		
		return alist;
	}
	
	public ArrayList<BoardVo> boardGalleryIndexAll() {
		Dbconn db = new Dbconn();
		conn = db.getConnection();
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
		ResultSet rs = null;
		String sql = "SELECT * FROM pj_BOARD a JOIN pj_board_detail b ON a.bidx = b.bdidx "
				+ "left JOIN (SELECT bidx,count(*) AS cnt FROM pj_comment WHERE delyn='N' GROUP BY BIDX) c ON a.bidx = c.bidx "
				+ "WHERE delyn = 'N' and boardCategory=6 order by originbidx desc, depth asc limit 0,4;";
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			//다음값이 존재할동안 반복
			while(rs.next()) {
				BoardVo bv = new BoardVo();
				bv.setCategory(rs.getInt("boardcategory"));
				bv.setTitle(rs.getString("boardtitle"));
				bv.setWriter(rs.getString("boardwriter"));
				bv.setBidx(rs.getInt("bidx"));
				String str2 = rs.getString("boardcontent");
				String piece = str2.substring(str2.indexOf("<img"));
				bv.setContent(piece);
				bv.setBoardComment(rs.getInt("cnt"));
				alist.add(bv);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {			
			close();		
		}		
		return alist;
	}
	
	private void close() {
		try {
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
