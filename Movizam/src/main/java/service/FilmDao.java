package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbconn.Dbconn;
import domain.FilmVo;
import domain.SearchCriteria;

public class FilmDao {
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	
	//�޼��徲���� �����ҋ� ��� ���� �����ϱ�
	public FilmDao(){
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	//��ȭ ���
	public int filmInsert(String filmCategory, String filmName, String filmDate, String filmStatus, String filmDetail, String filmPoster, int midx) {
		
		int value= 0;
		String sql ="insert into film(FILMCATEGORY,FILMNAME,FILMDATE,FILMSTATUS,FILMDETAIL,FILMPOSTER,MIDX) "
				+ "values(?,?,?,?,?,?,?)";
		
	
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, filmCategory);
			pstmt.setString(2, filmName);
			pstmt.setString(3, filmDate);
			pstmt.setString(4, filmStatus);
			pstmt.setString(5, filmDetail);
			pstmt.setString(6, filmPoster);
			pstmt.setInt(7, midx);
			
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		return value;
	}
	
	
	//��ȭ����Ʈ �ҷ����� //����¡ ó�� (�˻��� ��� ó��)
	public ArrayList<FilmVo> FilmSelectAll(SearchCriteria scri) {
		
		ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
		FilmVo fv = null;
		ResultSet rs = null;
		
		String str ="";
		if(scri.getSearchType().equals("filmName")) {
			str = " and filmName like ? ";		
		}
		
		String sql= "select * from film where delYN = 'N' "+str+" order by fidx desc limit ?, ?";
		
		try {
			pstmt = conn.prepareStatement(sql);	
			pstmt.setString(1, "%"+scri.getKeyword()+"%");
			pstmt.setInt(2, ((scri.getPage()-1)*15+1));  
			pstmt.setInt(3, (scri.getPage()*15));
			rs = pstmt.executeQuery();	
		
			while(rs.next()) {	// while. ���� �ִ� ���� ���ƶ� (if�� �ϳ� �����ö�!)
				
				fv = new FilmVo();	//�������� ��ȭ �ϳ��� ����
				
				fv.setFidx(rs.getInt("fidx"));
				fv.setFilmCategory(rs.getString("filmCategory"));
				fv.setFilmName(rs.getString("filmName"));
				fv.setFilmDate(rs.getString("filmDate"));
				fv.setFilmStatus(rs.getString("filmStatus"));
				fv.setFilmDetail(rs.getString("filmDetail"));
				fv.setFilmPoster(rs.getString("filmPoster"));
				fv.setMidx(rs.getInt("midx"));
				
				alist.add(fv);
			}
			
			} catch (SQLException e) {
			
				e.printStackTrace();
			}

		
		return alist;
	}
	
	
	//��ȭ����Ʈ �ҷ����� //����¡ ó�� (�˻��� ��� ó��)
		public ArrayList<FilmVo> FilmSelectAll_main(SearchCriteria scri) {
			
			ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
			FilmVo fv = null;
			ResultSet rs = null;
			
			
			
			String sql="select * from film where delYN = 'N' and  filmName like ? order by fidx desc";
			
			try {
				pstmt = conn.prepareStatement(sql);	
				pstmt.setString(1, "%"+scri.getKeyword()+"%");
				
				rs = pstmt.executeQuery();	
			
				while(rs.next()) {	// while. ���� �ִ� ���� ���ƶ� (if�� �ϳ� �����ö�!)
					
					fv = new FilmVo();	//�������� ��ȭ �ϳ��� ����
					
					fv.setFidx(rs.getInt("fidx"));
					fv.setFilmCategory(rs.getString("filmCategory"));
					fv.setFilmName(rs.getString("filmName"));
					fv.setFilmDate(rs.getString("filmDate"));
					fv.setFilmStatus(rs.getString("filmStatus"));
					fv.setFilmDetail(rs.getString("filmDetail"));
					fv.setFilmPoster(rs.getString("filmPoster"));
					fv.setMidx(rs.getInt("midx"));
					
					alist.add(fv);
				}
				
				} catch (SQLException e) {
				
					e.printStackTrace();
				}

			
			return alist;
		}
	
	//��ȭ���� �ϳ� �ҷ�����
	public FilmVo FilmSelectOne(int fidx) {
		FilmVo fv = null;
		ResultSet rs = null;
		
		String sql ="select * from film where fidx=?";		
		
		
		try {
			pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, fidx);
			
			rs= pstmt.executeQuery();
			
			if(rs.next()) {
				fv = new FilmVo();
				
				fv.setFidx(rs.getInt("fidx"));
				fv.setFilmCategory(rs.getString("filmCategory"));
				fv.setFilmDate(rs.getString("filmDate"));
				fv.setFilmName(rs.getString("filmName"));
				fv.setFilmStatus(rs.getString("filmStatus"));
				fv.setFilmDetail(rs.getString("filmDetail"));
				fv.setFilmPoster(rs.getString("filmPoster"));
				
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return fv;
	}
	
	
	
	
	
	//��ȭ���� �����ϱ�
	public int filmEdit(int fidx, String filmCategory, String filmName, String filmDate, String filmStatus, String filmDetail, String filmPoster) {
		int value =0;
		
		String sql="update film set FILMCATEGORY =?, FILMNAME =?, FILMDATE =?, "
				+ "FILMSTATUS =?, FILMDETAIL =?, FILMPOSTER =? "
				+ "where fidx=?";
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, filmCategory);
			pstmt.setString(2, filmName);
			pstmt.setString(3, filmDate);
			pstmt.setString(4, filmStatus);
			pstmt.setString(5, filmDetail);
			pstmt.setString(6, filmPoster);
			pstmt.setInt(7, fidx);
			
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return value;
				
	}
	
	
	
	//��ȭ ����
	public int filmDelete(int fidx) {
		int value = 0;
		
		String sql= "update film set delYn='Y' where fidx=?";
		
		try {
			pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, fidx);
			
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return value;
	}
	
	
	//���� ���� ��ȭ ������ ����
	public ArrayList<FilmVo> upcommingFilmLimit () {
			
		ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
		ResultSet rs = null;
			
//		String sql = "select * from "
//				+ "(select * from film where delYN = 'N' and filmStatus = '�󿵿���' order by fidx desc) A "
//				+ "where ROWNUM limit 1,4";
		
		String sql = "select * from film where delYN = 'N' and filmStatus = '�󿵿���' order by fidx desc limit 1,4";
		
			
		try {
			pstmt = conn.prepareStatement(sql);	
			rs = pstmt.executeQuery();	
				
			while(rs.next()) {	// while. ���� �ִ� ���� ���ƶ� (if�� �ϳ� �����ö�!)
					
				FilmVo fv = new FilmVo();	
					
				fv.setFidx(rs.getInt("fidx"));
				fv.setFilmCategory(rs.getString("filmCategory"));
				fv.setFilmName(rs.getString("filmName"));
				fv.setFilmDate(rs.getString("filmDate"));
				fv.setFilmStatus(rs.getString("filmStatus"));
				fv.setFilmDetail(rs.getString("filmDetail"));
				fv.setFilmPoster(rs.getString("filmPoster"));
				
					
				alist.add(fv);
			}
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}

			
		return alist;
	}
		
	
	//���� ��ȭ
	public ArrayList<FilmVo> onFilmLimit () {
					
		ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
		ResultSet rs = null;
					
//		String sql = "select * from "
//					+ "(select * from film where delYN = 'N' and filmStatus = '����' order by fidx desc) A "
//					+ "where ROWNUM limit 1,4";
					
		String sql ="select * from film where delYN = 'N' and filmStatus = '����' order by fidx desc limit 1,4";
		
			try {
				pstmt = conn.prepareStatement(sql);	
				rs = pstmt.executeQuery();	
						
				while(rs.next()) {	// while. ���� �ִ� ���� ���ƶ� (if�� �ϳ� �����ö�!)
							
					FilmVo fv = new FilmVo();	
							
						fv.setFidx(rs.getInt("fidx"));
						fv.setFilmCategory(rs.getString("filmCategory"));
						fv.setFilmName(rs.getString("filmName"));
						fv.setFilmDate(rs.getString("filmDate"));
						fv.setFilmStatus(rs.getString("filmStatus"));
						fv.setFilmDetail(rs.getString("filmDetail"));
						fv.setFilmPoster(rs.getString("filmPoster"));
						
							
						alist.add(fv);
					}
						
					} catch (SQLException e) {
						
						e.printStackTrace();
					}

					
				return alist;
			}
	
	
	
	
	//������ ��ȭ
	public ArrayList<FilmVo> offFilmLimit () {
					
		ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
		ResultSet rs = null;
				
//		String sql = "select * from "
//				+ "(select * from film where delYN = 'N' and filmStatus = '������' order by fidx desc) A "
//			+ "where ROWNUM limit 1,4";
		
		String sql ="select * from film where delYN = 'N' and filmStatus = '������' order by fidx desc limit 1,4";
		
			try {
				pstmt = conn.prepareStatement(sql);	
				rs = pstmt.executeQuery();	
						
				while(rs.next()) {	// while. ���� �ִ� ���� ���ƶ� (if�� �ϳ� �����ö�!)
							
					FilmVo fv = new FilmVo();	
						
					fv.setFidx(rs.getInt("fidx"));
					fv.setFilmCategory(rs.getString("filmCategory"));
					fv.setFilmName(rs.getString("filmName"));
					fv.setFilmDate(rs.getString("filmDate"));
					fv.setFilmStatus(rs.getString("filmStatus"));
					fv.setFilmDetail(rs.getString("filmDetail"));
					fv.setFilmPoster(rs.getString("filmPoster"));
					
							
					alist.add(fv);
				}
						
				} catch (SQLException e) {
						
					e.printStackTrace();
				}

					
			return alist;
		}
	
	
	
	
	
	
	//���� ���� ��ȭ ������ ����
	public ArrayList<FilmVo> upcommingFilm () {
					
		ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
		ResultSet rs = null;
					
		String sql = "select * from film where delYN = 'N' and filmStatus = '�󿵿���' order by fidx desc ";
			
					
		try {
			pstmt = conn.prepareStatement(sql);	
			rs = pstmt.executeQuery();	
						
			while(rs.next()) {	// while. ���� �ִ� ���� ���ƶ� (if�� �ϳ� �����ö�!)
							
				FilmVo fv = new FilmVo();	
							
				fv.setFidx(rs.getInt("fidx"));
				fv.setFilmCategory(rs.getString("filmCategory"));
				fv.setFilmName(rs.getString("filmName"));
				fv.setFilmDate(rs.getString("filmDate"));
				fv.setFilmStatus(rs.getString("filmStatus"));
				fv.setFilmDetail(rs.getString("filmDetail"));
				fv.setFilmPoster(rs.getString("filmPoster"));
						
							
				alist.add(fv);
			}
						
			} catch (SQLException e) {
						
				e.printStackTrace();
			}

					
		return alist;
	}
				
			
	//���� ��ȭ
	public ArrayList<FilmVo> onFilm () {
							
		ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
		ResultSet rs = null;
							
			String sql = "select * from film where delYN = 'N' and filmStatus = '����' order by fidx desc ";
					
							
			try {
				pstmt = conn.prepareStatement(sql);	
				rs = pstmt.executeQuery();	
								
				while(rs.next()) {	// while. ���� �ִ� ���� ���ƶ� (if�� �ϳ� �����ö�!)
									
					FilmVo fv = new FilmVo();	
									
						fv.setFidx(rs.getInt("fidx"));
						fv.setFilmCategory(rs.getString("filmCategory"));
						fv.setFilmName(rs.getString("filmName"));
						fv.setFilmDate(rs.getString("filmDate"));
						fv.setFilmStatus(rs.getString("filmStatus"));
						fv.setFilmDetail(rs.getString("filmDetail"));
						fv.setFilmPoster(rs.getString("filmPoster"));
									
									
						alist.add(fv);
					}
								
					} catch (SQLException e) {
					
						e.printStackTrace();
					}

							
				return alist;
			}
			
			
			
			
	// ������ ��ȭ
	public ArrayList<FilmVo> offFilm() {

		ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
		ResultSet rs = null;

		String sql = "select * from film where delYN = 'N' and filmStatus = '������' order by fidx desc ";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) { // while. ���� �ִ� ���� ���ƶ� (if�� �ϳ� �����ö�!)

				FilmVo fv = new FilmVo();

				fv.setFidx(rs.getInt("fidx"));
				fv.setFilmCategory(rs.getString("filmCategory"));
				fv.setFilmName(rs.getString("filmName"));
				fv.setFilmDate(rs.getString("filmDate"));
				fv.setFilmStatus(rs.getString("filmStatus"));
				fv.setFilmDetail(rs.getString("filmDetail"));
				fv.setFilmPoster(rs.getString("filmPoster"));

				alist.add(fv);
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return alist;
	}
	
	
	//��ȭ�� �� ����(������+�˻����)
	public int filmTotal(SearchCriteria scri) {
		int cnt =0;
		ResultSet rs =null;
		
		//����ڰ� �Է��� Ű���带 �޾Ƽ� �˻� ī�װ��� ���� ����
		String str ="";
		if(scri.getSearchType().equals("filmName")) {
			str = " and filmName like ? ";		
		}
		
		
		//��ȭ ����(�ӽ�Į�� cnt ������ֱ� )
		String sql ="select count(*) as cnt from film where delYN='N'"+ str;
		
		try {
			pstmt = conn.prepareStatement(sql);
			// Ű���� ������ �ͼ� ����Ǵ� ������ �°� �־ ����
			pstmt.setString(1, "%"+scri.getKeyword()+"%");
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt = rs.getInt("cnt");
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		return cnt;
	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
