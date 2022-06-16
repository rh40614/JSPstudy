package mysqlProject.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mysqlProject.dbconn.Dbconn;
import mysqlProject.domain.MemberVo;

public class MemberDao {
	//¿¬°á°´Ã¼
		private Connection conn;
		//±¸¹®°´Ã¼
		private PreparedStatement pstmt;
		
		public MemberDao() {
			Dbconn db = new Dbconn();
			this.conn = db.getConnection();
		}
		
		public int insertMember(MemberVo mv){
			int value = 0, value2 = 0;	
			
			String sql="INSERT INTO pj_member(memberid, memberpwd) VALUES (?,?)";
					
			try{
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, mv.getMemberid());
				pstmt.setString(2, mv.getMemberpwd());
				value = pstmt.executeUpdate();
				if(value == 1) {
					sql = "insert INTO pj_member_detail(MEMBERNICNAME,MEMBERNAME,MEMBEREMAIL,MEMBERPHONEAGENCY,MEMBERPHONE,MEMBERGENDER,JOINIP)"
							+ "VALUES(?,?,?,?,?,?,?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, mv.getMembernicname());
					pstmt.setString(2, mv.getMembername());
					pstmt.setString(3, mv.getMemberemail());
					pstmt.setString(4, mv.getMemberphoneagency());
					pstmt.setString(5, mv.getMemberphone());
					pstmt.setString(6, mv.getMembergender());
					pstmt.setString(7, mv.getJoinip());
					value2 = pstmt.executeUpdate();
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
			System.out.println("value : "+value);
			return value2;		
		}
		public int loginCheck(String userId, String userPwd) {
			int value = 0;
			ResultSet rs = null;
			String sql = "SELECT * FROM pj_member a JOIN pj_member_detail b"
					+ " ON a.MIDX = b.MDIDX WHERE DELYN ='N' AND MEMBERID =? AND memberpwd=?";
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userId);
				pstmt.setString(2, userPwd);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					value = 0;
				}else value = 1;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}
			return value;
		}
		public int delCheck(String userPwd) {
			int value = 0;
			ResultSet rs = null;
			String sql = "SELECT * FROM pj_member where memberpwd=?";
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userPwd);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					value = 0;
				}else value = 1;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}
			return value;
		}
		public int delClear(String id) {
			int value = 0;
			String sql = "update pj_member set delyn = 'Y' where memberid=?";
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				value = pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}
			return value;
		}
		
		public MemberVo memberLogin(String memberId, String memberPwd) {
			ResultSet rs = null;
			MemberVo mv = null;
			String sql = "SELECT * FROM pj_member a JOIN pj_member_detail b"
					+ " ON a.MIDX = b.MDIDX WHERE DELYN ='N' AND MEMBERID =? AND memberpwd=?";
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, memberId);
				pstmt.setString(2, memberPwd);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					mv = new MemberVo();
					mv.setMidx(rs.getInt("midx"));
					mv.setMemberid(rs.getString("memberId"));
					mv.setMembernicname(rs.getString("membernicname"));
					mv.setOper(rs.getString("oper"));
					mv.setMemberProfile(rs.getString("memberprofile"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}
			return mv;
		}
		public int idCheck(String id) {
			int result = 0;
			
			ResultSet rs = null;
			
			String sql = "select memberid from pj_member where memberid=?";
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					result = 1;
				}else {
					result = 0;
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
			return result;
		}
		
		public int changeProfile(int midx , String filename) {
			int value=0;
			String sql = "update pj_member_detail set memberprofile = ? where mdidx = ?";
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, filename);
				pstmt.setInt(2, midx);
				value = pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					pstmt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return value;
		}
		
		public int checkid(String id) {
			int value = 0;
			ResultSet rs = null;
			String sql = "select * from pj_member where memberid = ? and delyn = 'N'";
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					value = 0;
				}else value = 1;
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
			return value;
		}
		
		public int checknic(String nic) {
			int value = 0;
			ResultSet rs = null;
			String sql = "select * from pj_member a join pj_member_detail b on a.midx = b.mdidx where membernicname = ? and delyn = 'N'";
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, nic);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					value = 0;
				}else value = 1;
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
			return value;
		}
		
		public int findId(String name, String email) {
			int value = 0;
			ResultSet rs = null;
			String sql = "select * from pj_member_detail where membername=? and memberemail=?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, name);
				pstmt.setString(2, email);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					value = 0;
				}else value = 1;
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
			return value;
		}
		
		public int findPwd(String id, String name, String email) {
			int value = 0;
			ResultSet rs = null;
			String sql = "select * from pj_member a join pj_member_detail b "
					+ "on a.midx=b.mdidx where memberid=? and membername=? and memberemail=?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.setString(2, name);
				pstmt.setString(3, email);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					value = 0;
				}else value = 1;
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
			return value;
		}
		
		public MemberVo findresult(MemberVo mv) {
			ResultSet rs = null;
			
			String sql = "SELECT memberid from pj_member a join pj_member_detail b "
					+ "on a.midx = b.mdidx where membername=? and memberemail=?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, mv.getMembername());
				pstmt.setString(2, mv.getMemberemail());
				rs = pstmt.executeQuery();
				if(rs.next()) {
					mv.setMemberid(rs.getString("memberid"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
			
			return mv;
		}
		
		public int changePwd(String id, String pwd) {
			int value = 0;
			String sql = "update pj_member set memberpwd = ? where memberid=?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, pwd);
				pstmt.setString(2, id);
				value = pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
			
			return value;
		}

		public String ajaxProfile(String midx) {
			String value = null;
			ResultSet rs = null;
			String sql = "select memberprofile from pj_member_detail where mdidx=?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, midx);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					value = rs.getString("memberprofile");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
			return value;
		}
		
		public MemberVo infoIdx(int midx) {
			ResultSet rs = null;
			MemberVo mv = new MemberVo();
			String sql = "SELECT * FROM pj_member a JOIN pj_member_detail b ON a.midx = b.mdidx WHERE midx = ?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, midx);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					mv.setMemberid(rs.getString("memberid"));
					mv.setMemberpwd(rs.getString("memberpwd"));
					mv.setMembernicname(rs.getString("membernicname"));
					mv.setMembername(rs.getString("membername"));
					mv.setMemberemail(rs.getString("memberemail"));
					mv.setMemberphone(rs.getString("memberphone"));
					mv.setMemberphoneagency(rs.getString("memberphoneagency"));
					mv.setMembergender(rs.getString("membergender"));					
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
			return mv;			
		}
		
		public int modifyMember(MemberVo mv){
			int value = 0;
			Dbconn db = new Dbconn();
			conn = db.getConnection();
			String sql="UPDATE pj_member set memberpwd=? where midx = ?";
			try{
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, mv.getMemberpwd());
				pstmt.setInt(2, mv.getMidx());
				value = pstmt.executeUpdate();
			}catch(Exception e){
				e.printStackTrace();
			}
			conn = db.getConnection();
			sql ="update pj_member_detail set membernicname=?, membername=?, memberemail=?, memberphoneagency=?, memberphone=?, membergender=? where mdidx=?";
			try{
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, mv.getMembernicname());
				pstmt.setString(2, mv.getMembername());
				pstmt.setString(3, mv.getMemberemail());
				pstmt.setString(4, mv.getMemberphoneagency());
				pstmt.setString(5, mv.getMemberphone());
				pstmt.setString(6, mv.getMembergender());
				pstmt.setInt(7, mv.getMidx());
				value = pstmt.executeUpdate();
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
			conn = db.getConnection();
			sql ="update pj_board set writer=? where midx=?";
			try{
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, mv.getMembernicname());
				pstmt.setInt(2, mv.getMidx());
				value = pstmt.executeUpdate();
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
			System.out.println("value : "+value);
			return value;		
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
