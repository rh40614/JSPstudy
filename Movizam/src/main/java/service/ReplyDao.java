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
	
	//�޼��徲���� �����ҋ� ��� ���� �����ϱ�
	public ReplyDao(){
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	
	
	
	//����ۼ�
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
	
	
	
	
	
	//�Ѱ��ñۿ� �ش��ϴ� ��۸���Ʈ(�˻��� �ʿ���� ����¡��)
	public ArrayList<ReplyVo> ReplySelectAll(Criteria cri, int bidx){		
		
		ArrayList<ReplyVo> alist = new ArrayList<ReplyVo>();
		ReplyVo rv = null;
		ResultSet rs =null; 
		
		
//		String sql="SELECT * FROM ("
//				+ "SELECT ROWNUM AS rnum, A.* FROM ("
//				+ "SELECT * FROM REPLY WHERE DELYN='N' and bidx=? ORDER BY ORIGINRIDX desc, depth ASC) A" //str�� ���� try�� ó��
//				+ ") B  WHERE rnum  BETWEEN ? AND ?";  //1~15 ���Ѿ���� �ϱ�
		
		
		String sql ="SELECT * FROM REPLY WHERE DELYN='N' and bidx=? ORDER BY ORIGINRIDX desc, depth ASC  BETWEEN ? AND ?";
		try{	
			
			pstmt =conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			pstmt.setInt(2, ((cri.getPage()-1)*15+1));  //�ż��� ��ü�� �Ű������� int page�� ����
			pstmt.setInt(3, (cri.getPage()*15));
			
			rs= pstmt.executeQuery();
			
		while(rs.next()){
			//�ݺ��Ҷ����� ��ü�����ؼ� ��ü���� �Խñ��� �ϳ������
			rv = new ReplyVo();
			//�Ű� ���
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
			
			alist.add(rv); //������ bv ��ü�� alist�� �߰��Ѵ�. 
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
	
	
	
	
	
	
	
	
	
	
	
	
	//���� �ۼ�
	//��������� depth �ϳ� �ø��� ���� �޸��� ����� depth, level �ø���
	public int replyComment(ReplyVo rv) //�Ű������� ������ ���°��̶� �ƴ϶� bean�� �����ؼ� ��Ƽ� �ѱ�� 
	{	
		int value=0;
		String sql1 ="update reply set depth = depth+1 where originRidx=? and depth > ?";	//������ depth�÷���
		String sql2= " insert into reply(REPLYWRITER, REPLYCONTENT,REPLYIP,ORIGINRIDX,DEPTH,LEVEL_,MIDX,BIDX) "
				+ "values( ?, ?, ?, ?, ?, ?, ?, ?)";
		
		
		//tranjection  �ΰ����� ��������� ������ �ΰ����� ���۾������� ���� ���۾� �ΰ��� ��� ����� ���������� ��� �ݿ��̵ȴ�. 
		//�Ѳ����� ��μ���Ǿ���ϴ� ����. ������ �ٿ� ������ �ϳ����� ���� ���� �۾������� ���ΰ͵� ��� ��ҵȴ�. 
	try {
		conn.setAutoCommit(false); //���� �ڹٿ����� �����ϰ��� �ڵ�Ŀ�Եǵ��ϵǾ��ִµ� �̰��� false�� �ٲ���
	
		pstmt = conn.prepareStatement(sql1); 		
		pstmt.setInt(1, rv.getOriginRidx());		//���� �Ű��������� rv�� ��ƿ������� rv���� ������ ����.		
		pstmt.setInt(2, rv.getDepth());
		value = pstmt.executeUpdate();  
		
		pstmt = conn.prepareStatement(sql2); 		
		
		pstmt.setString(1, rv.getReplyWriter());
		pstmt.setString(2, rv.getReplyContent());
		pstmt.setString(3, rv.getReplyIp());
		pstmt.setInt(4,rv.getOriginRidx());
		pstmt.setInt(5,rv.getDepth()+1);	//�Ű������� �޾ƿ� Vo����. Vo�� ����ִ� ������ +1 ���� ���� Ŀ���Ѵ�. 
		pstmt.setInt(6,rv.getLevel_()+1);
		pstmt.setInt(7, rv.getMidx());
		pstmt.setInt(8, rv.getBidx());
		value = pstmt.executeUpdate();
		
		conn.commit(); //�������������� �ٵǸ� Ŀ��
		
		} catch (Exception e) {		
			try {
				conn.rollback();
			} catch (SQLException e1) {
				//���� �����ϴٰ� �������� rollback�϶�
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
	
	
	
	// ��� �����ϱ�	
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
	
	
	
	// ��� ����
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
	
	
	
	
	
	
	
	// ��� �� ���� (����¡)	// ������ ��ȯ�ϴµ� rs ����ϳ�
	public int ReplyTotal(SearchCriteria scri) {	
		int cnt = 0;
		ResultSet rs= null;
	
		String str ="";
		
		if(scri.getSearchType().equals("replyContent")) {
			str = "and replyContent like ?";
		}else if(scri.getSearchType().equals("replyContent")){
			str = "and replyWriter like ?";
		}
		
		String sql ="select count(*) as cnt from reply where delyn='N' "+str;	// �Խù� ��ü ����	. 
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
	
	
	
	//�ѰԽñۿ� �ش��ϴ� ��۸���Ʈ(������ ���)
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
					+ ") B WHERE rnum  BETWEEN ? AND ?";  //1~15 ���Ѿ���� �ϱ�
			
			try{	
				
				pstmt =conn.prepareStatement(sql);
				pstmt.setString(1, "%"+scri.getKeyword()+"%");
				pstmt.setInt(2, ((scri.getPage()-1)*15+1));  //�ż��� ��ü�� �Ű������� int page�� ����
				pstmt.setInt(3, (scri.getPage()*15));
				
				rs= pstmt.executeQuery();
				
			while(rs.next()){
				//�ݺ��Ҷ����� ��ü�����ؼ� ��ü���� �Խñ��� �ϳ������
				rv = new ReplyVo();
				//�Ű� ���
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
				
				
				alist.add(rv); //������ bv ��ü�� alist�� �߰��Ѵ�. 
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
	
		//�ѰԽñۿ� �ش��ϴ� ��۸���Ʈ(�Ϲ�ȸ�� ���)
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
					+ ") B WHERE rnum  BETWEEN ? AND ?";  //1~15 ���Ѿ���� �ϱ�
			
			try{	
						
				pstmt =conn.prepareStatement(sql);
				pstmt.setInt(1, midx);
				pstmt.setString(2, "%"+scri.getKeyword()+"%");
				pstmt.setInt(3, ((scri.getPage()-1)*15+1));  //�ż��� ��ü�� �Ű������� int page�� ����
				pstmt.setInt(4, (scri.getPage()*15));
						
				rs= pstmt.executeQuery();
						
			while(rs.next()){
				//�ݺ��Ҷ����� ��ü�����ؼ� ��ü���� �Խñ��� �ϳ������
					rv= new ReplyVo();
				//�Ű� ���
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
						
						
				alist.add(rv); //������ bv ��ü�� alist�� �߰��Ѵ�. 
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
	
	//����ε� ó��
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
	
	
	
	//����ε� ó������
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
