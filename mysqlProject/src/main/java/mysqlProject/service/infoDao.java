package mysqlProject.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mysqlProject.dbconn.Dbconn;
import mysqlProject.domain.BoardVo;
import mysqlProject.domain.SearchCriteria;

public class infoDao {
	//연결객체
	private Connection conn;
	//구문객체
	private PreparedStatement pstmt;
	
	public infoDao() {
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	public int infoBoardList(SearchCriteria scri,int midx) {
		int cnt = 0;
		ResultSet rs = null;
		String sql ="";
			sql = "SELECT COUNT(*) as cnt FROM pj_board a JOIN pj_board_detail b ON a.bidx = b.bdidx WHERE a.midx = ? AND delyn='N'" ;
		try {
			pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, midx);
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
	
	public ArrayList<BoardVo> infoBoardListAll(SearchCriteria scri,int midx) {
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
		ResultSet rs = null;
		String sql = "SELECT * FROM (SELECT ROWNUM AS rnum, A.* FROM("
					+"SELECT * FROM pj_BOARD a JOIN pj_board_detail b ON a.bidx = b.bdidx "
					+"left JOIN (SELECT bidx,count(*) AS cnt FROM pj_comment WHERE delyn='N' GROUP BY BIDX) c ON a.bidx = c.bidx "
					+"WHERE midx = ? and a.DELYN='N' order by a.bidx desc) A"
					+")B WHERE rnum BETWEEN ? AND ?";
		try {
			pstmt = conn.prepareStatement(sql);
			if(scri.getCategory()==0) {
				pstmt.setInt(1, midx);
				pstmt.setInt(2, (scri.getPage()-1)*scri.getPerPageNum()+1);
				pstmt.setInt(3, scri.getPage()*scri.getPerPageNum());
			}
			rs = pstmt.executeQuery();
			
			//다음값이 존재할동안 반복
			while(rs.next()) {
				BoardVo bv = new BoardVo();
				bv.setCategory(rs.getInt("boardtag"));
				bv.setTitle(rs.getString("title"));
				bv.setBidx(rs.getInt("bidx"));
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
	
	private void close() {
		try {
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
