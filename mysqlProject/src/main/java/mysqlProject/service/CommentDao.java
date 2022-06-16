package mysqlProject.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mysqlProject.dbconn.Dbconn;
import mysqlProject.domain.CommentVo;
import mysqlProject.domain.SearchCriteria;

public class CommentDao {
	//연결객체
	private Connection conn;
	//구문객체
	private PreparedStatement pstmt;
	
	public CommentDao() {
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	public String commentWrite(String bidx, String nicName, String commContent) throws UnknownHostException {
		int bidx_ = Integer.parseInt(bidx);
		String ip = InetAddress.getLocalHost().getHostAddress();
		Date now = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		String day = simpleDateFormat.format(now);
		
		String sql = "insert into pj_comment (bidx,writer,content,ip,origincidx) "
				+ "values(?,?,?,?,cidx)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,bidx_);
			pstmt.setString(2,nicName);
			pstmt.setString(3,commContent);
			pstmt.setString(4, ip);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return day;
	}

	public String reCommentWrite(String cidx, String bidx, String nicName, String reContent) throws UnknownHostException {
		int originCidx = Integer.parseInt(cidx);
		int bidx_ = Integer.parseInt(bidx);
		String ip = InetAddress.getLocalHost().getHostAddress();
		Date now = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		String day = simpleDateFormat.format(now);
		
		String sql = "insert into pj_comment (bidx,writer,content,ip,origincidx,level_) "
				+ "values(?,?,?,?,?,1)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,bidx_);
			pstmt.setString(2,nicName);
			pstmt.setString(3,reContent);
			pstmt.setString(4, ip);
			pstmt.setInt(5, originCidx);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return day;
	}
	
	public int commentDelete(int cidx) {
		int value = 0;
		
		String sql = "update pj_comment set delyn='Y' where cidx = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cidx);
			value = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return value;
	}
	
	public int commentList(int bidx) {
		int cnt = 0;
		ResultSet rs = null;
		String sql = "select count(*) as cnt from pj_comment a JOIN pj_board b ON a.bidx = b.bidx where a.bidx=? " ;
		
		try {
			pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				cnt = rs.getInt("cnt");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return cnt;
	}
	
	public ArrayList<CommentVo> commentListAll(SearchCriteria scri, int bidx) {
		Dbconn db = new Dbconn();
		conn = db.getConnection();
		ArrayList<CommentVo> clist = new ArrayList<CommentVo>();
		ResultSet rs = null;
		String sql = "select * from pj_comment where bidx=? order by cidx limit ?,?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			pstmt.setInt(2, (scri.getPage()-1)*scri.getPerPageNum()+1);
			pstmt.setInt(3, scri.getPage()*scri.getPerPageNum());
			rs = pstmt.executeQuery();
			//다음값이 존재할동안 반복
			while(rs.next()) {
				CommentVo cv = new CommentVo();
				
				cv.setCidx(rs.getInt("cidx"));
				cv.setBidx(rs.getInt("bidx"));
				cv.setContent(rs.getString("content"));
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				String day = simpleDateFormat.format(rs.getDate("commentday"));
				cv.setDay(day);
				cv.setWriter(rs.getString("writer"));
				cv.setDelyn(rs.getString("delyn"));
				cv.setLevel_(rs.getInt("level_"));
				cv.setOriginCidx(rs.getInt("origincidx"));
				clist.add(cv);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {			
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}		
		return clist;
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
