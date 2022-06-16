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
	
	private Connection conn; //Connection ��ü�� �̿��ؼ� ���ڿ��� ����ȭ ��Ų��. 
	private PreparedStatement pstmt;
	
	
	public BoardDao(){
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	
	
	//db�� �����͸� �Է��ϴ� �޼���.//String fileName �߰�
	public int insertBoard(String subject, String writer, String content, String ip, int midx, String fileName) 
	{	
	
		int value=0;
		ResultSet rs =null;
		
		String sql ="insert into a_board(originBidx, DEPTH, LEVEL_, SUBJECT, WRITER, CONTENT, IP, MIDX, FileName)"
		+"values(0,0,0,?,?,?,?,?,?)";   
		
		String sql2 ="select bidx from a_board where originBidx = 0";
		String sql3 ="update a_board set originBidx =? where bidx =?";
		
	try {
		pstmt = conn.prepareStatement(sql); 		//���� ���ڿ��� sql ��ü�� �ٲ���
		pstmt.setString(1,subject);					//bidx�� �Է��ϱ� �ʾƵ� �ڵ����� ���������� ������ ���������� �ʴ´�. 
		pstmt.setString(2,writer);
		pstmt.setString(3,content);
		pstmt.setString(4,ip);
		pstmt.setInt(5,midx);
		pstmt.setString(6, fileName);
		value = pstmt.executeUpdate();  
		
		//��� ���� �Խù��� �����ͼ� originBidx�� 0�̸� ��  bidx�� ��������  �װ��� update�� �־��ش�. 
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
	System.out.println("�Խñ� �ۼ��Ϸ�");
	
	return value;
	
	}
	
	
	
	
	
	
	//���� ��� ��ü Value Object �� ���� ��� ���� �޼��� 
	//�������� �����͸� �����ð��̱� ������ �迭�� �޾ƿ´�. 
	public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri){		
		
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
		BoardVo bv = null;
		ResultSet rs =null; //resultSet�� ���� �����͸� �����ؼ� �״�� ����ִ� Ŭ���� 
		// String sql="select * from a_board where delyn='N' order by ORIGINBIDX desc, depth asc"; //�÷����� ���� ���� �������ֱ� 
	
		//like������ �̿��Ͽ� Ư�� �˻�� �����Ͽ� ������ ������ str�� ��´�. �׸��� ���������� str�����μ� ��������. 
		String str ="";
		if(scri.getSearchType().equals("subject")) {
			str = "and subject like ?";
		}else {
			str = "and writer like ?";
		}
		
		//limit���� ����
		String sql = "SELECT * FROM a_board WHERE DELYN='N' "+str+"  ORDER BY ORIGINBIDX desc, depth ASC limit ?,?  ";
		
		try{			
			
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, "%"+scri.getKeyword()+"%");	//keyword(�˻�â)�� �ԷµǴ� ���� ù����?�� �ִ´�. �����̸� ������ ?,�ۼ��ڸ� �ۼ�����?�� ����.
		pstmt.setInt(2, 1);  
		pstmt.setInt(3, 15);
		rs= pstmt.executeQuery(); 
		
		//�ݺ��� ����. rs.next�� �������� ���� ������ true ������ false�� ��ȯ�Ѵ�. ���� ������ �� ������ Ŀ���� �̵��Ѵ�. 
		while(rs.next()){
			//�ݺ��Ҷ����� ��ü�����ؼ� ��ü���� �Խñ��� �ϳ������
			bv = new BoardVo();
			//�Ű� ���
			bv.setBidx(rs.getInt("bidx"));
			bv.setSubject(rs.getString("subject"));
			bv.setWriter(rs.getString("writer"));
			bv.setWriteday(rs.getString("writeday"));
			bv.setLevel_(rs.getInt("level_"));
			
			alist.add(bv); //������ bv ��ü�� alist�� �߰��Ѵ�. 
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
	
	
	
	
	
	//ȭ��ܿ� ������ ���(�ϳ����� ������) // bean�� ��Ƽ� �ѱ�⶧���� boardVo ��ü�� �����ϴ� ���� �����͸� ���� bean�� ����°�
	public BoardVo boardSelectOne(int bidx) {
		BoardVo bv = null;
		ResultSet rs =null; // ���������� �״�� ����
		
		String sql= "select * from a_board where bidx=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);	//������  bidx�� �ش�Ǵ� ��� ������ �ۼ��������� �ϳ��� �޾ƿ��� �ȴ�. //bidx�� ������ �� �����ͼ� rs�� ����ָ�ȴ�.
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				//���� �����ϸ� bean �����
				bv = new BoardVo();
				//bean�� ��� (Ÿ�Զ����� ���پ��� ���)
				bv.setBidx(rs.getInt("bidx"));
				bv.setOriginbidx(rs.getInt("originbidx")); //�����ϱ� �亯�ϱ� ���� ����� ����ϱ� ����
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
	
	
	
	//�����ϴ� �޼���. �Ű������� ȭ��ܿ��� �Ѿ�� �����͵� ���� ���ֵ����ϱ�
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
			
			value = pstmt.executeUpdate(); //���๮	
			//sql������ select�̸� ��񿡼� �����͸� �����ͼ� ó���ϴ� ���̱� ������ rs�� �̿��ؼ� ��ƾ��Ѵ�. ������ insert, update �� �����ͺ��汸���� ����Ѵٸ�
			//rs�� ����ϴ� ���� �ƴ϶� int value�� ����ؼ� 1�̸� ���� 0�̸� ���� ���� ����� �� �� �ֵ����Ѵ�. 
		
			
				
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



	public int boardReply(BoardVo bv) //�Ű������� ������ ���°��̶� �ƴ϶� bean�� �����ؼ� ��Ƽ� �ѱ�� 
	{	
		int value=0;
		String sql1 ="update a_board set depth = depth+1 where originbidx=? and depth > ?";	//������ depth�÷���
		String sql2 ="insert into a_board( ORIGINBIDX, DEPTH, LEVEL_, SUBJECT, WRITER, CONTENT, IP, MIDX)" //�亯��
		+"values(?,?,?,?,?,?,?,?)";   
		
		//tranjection  �ΰ����� ��������� ������ �ΰ����� ���۾������� ���� ���۾� �ΰ��� ��� ����� ���������� ��� �ݿ��̵ȴ�. 
		//�Ѳ����� ��μ���Ǿ���ϴ� ����. ������ �ٿ� ������ �ϳ����� ���� ���� �۾������� ���ΰ͵� ��� ��ҵȴ�. 
	try {
		conn.setAutoCommit(false); //���� �ڹٿ����� �����ϰ��� �ڵ�Ŀ�Եǵ��ϵǾ��ִµ� �̰��� false�� �ٲ���
		pstmt = conn.prepareStatement(sql1); 		
		pstmt.setInt(1,bv.getOriginbidx());		//���� �Ű��������� bv�� ��ƿ������� bv���� ������ ����.		
		pstmt.setInt(2,bv.getDepth());
		value = pstmt.executeUpdate();  
		
		pstmt = conn.prepareStatement(sql2); 		
		pstmt.setInt(1,bv.getOriginbidx());		//���� �Ű��������� bv�� ��ƿ������� bv���� ������ ����.		
		pstmt.setInt(2,bv.getDepth()+1);
		pstmt.setInt(3,bv.getLevel_()+1);
		pstmt.setString(4, bv.getSubject());
		pstmt.setString(5, bv.getWriter());
		pstmt.setString(6, bv.getContent());
		pstmt.setString(7, bv.getIp());
		pstmt.setInt(8, bv.getMidx());
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
	
	
	
	
	
	public int boardTotal(SearchCriteria scri) {	//�Ű������� ������ ���鼭 Ű����˻��� ��������
		int cnt = 0;
		ResultSet rs= null;
	
		//����ڰ� �Է��� Ű���� �Է¹��� ���ֵ��� str���������
		String str ="";
		if(scri.getSearchType().equals("subject")) {
			str = " and subject like ?";
		}else {
			str = " and writer like ?";
		}
		
		
		String sql ="select count(*) as cnt from a_board where delyn='N'"+str;	// �Խù� ��ü ����	. // ����ڰ� �Է��� �˻�� �� �Խù��� �� ����� ���� 	
		
		try {
			pstmt= conn.prepareStatement(sql);
			pstmt.setString(1, "%"+scri.getKeyword()+"%");		//������ �����ϰ� str�κ��� ���� str�� �����ϰ� ?���� ȭ�鿡�� �Է¹������� �����ͼ� ���� ���ֵ����Ѵ�.
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
