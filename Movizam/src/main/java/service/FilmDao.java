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
	
	//메서드쓰려고 생성할떄 디비도 같이 생성하기
	public FilmDao(){
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	//영화 등록
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
	
	
	//영화리스트 불러오기 //페이징 처리 (검색어 기능 처리)
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
		
			while(rs.next()) {	// while. 값이 있는 동안 돌아라 (if는 하나 가져올때!)
				
				fv = new FilmVo();	//돌때마다 영화 하나씩 생성
				
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
	
	
	//영화리스트 불러오기 //페이징 처리 (검색어 기능 처리)
		public ArrayList<FilmVo> FilmSelectAll_main(SearchCriteria scri) {
			
			ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
			FilmVo fv = null;
			ResultSet rs = null;
			
			
			
			String sql="select * from film where delYN = 'N' and  filmName like ? order by fidx desc";
			
			try {
				pstmt = conn.prepareStatement(sql);	
				pstmt.setString(1, "%"+scri.getKeyword()+"%");
				
				rs = pstmt.executeQuery();	
			
				while(rs.next()) {	// while. 값이 있는 동안 돌아라 (if는 하나 가져올때!)
					
					fv = new FilmVo();	//돌때마다 영화 하나씩 생성
					
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
	
	//영화정보 하나 불러오기
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
	
	
	
	
	
	//영화정보 수정하기
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
	
	
	
	//영화 삭제
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
	
	
	//개봉 예정 영화 가지고 오기
	public ArrayList<FilmVo> upcommingFilmLimit () {
			
		ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
		ResultSet rs = null;
			
//		String sql = "select * from "
//				+ "(select * from film where delYN = 'N' and filmStatus = '상영예정' order by fidx desc) A "
//				+ "where ROWNUM limit 1,4";
		
		String sql = "select * from film where delYN = 'N' and filmStatus = '상영예정' order by fidx desc limit 1,4";
		
			
		try {
			pstmt = conn.prepareStatement(sql);	
			rs = pstmt.executeQuery();	
				
			while(rs.next()) {	// while. 값이 있는 동안 돌아라 (if는 하나 가져올때!)
					
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
		
	
	//상영중 영화
	public ArrayList<FilmVo> onFilmLimit () {
					
		ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
		ResultSet rs = null;
					
//		String sql = "select * from "
//					+ "(select * from film where delYN = 'N' and filmStatus = '상영중' order by fidx desc) A "
//					+ "where ROWNUM limit 1,4";
					
		String sql ="select * from film where delYN = 'N' and filmStatus = '상영중' order by fidx desc limit 1,4";
		
			try {
				pstmt = conn.prepareStatement(sql);	
				rs = pstmt.executeQuery();	
						
				while(rs.next()) {	// while. 값이 있는 동안 돌아라 (if는 하나 가져올때!)
							
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
	
	
	
	
	//상영종료 영화
	public ArrayList<FilmVo> offFilmLimit () {
					
		ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
		ResultSet rs = null;
				
//		String sql = "select * from "
//				+ "(select * from film where delYN = 'N' and filmStatus = '상영종료' order by fidx desc) A "
//			+ "where ROWNUM limit 1,4";
		
		String sql ="select * from film where delYN = 'N' and filmStatus = '상영종료' order by fidx desc limit 1,4";
		
			try {
				pstmt = conn.prepareStatement(sql);	
				rs = pstmt.executeQuery();	
						
				while(rs.next()) {	// while. 값이 있는 동안 돌아라 (if는 하나 가져올때!)
							
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
	
	
	
	
	
	
	//개봉 예정 영화 가지고 오기
	public ArrayList<FilmVo> upcommingFilm () {
					
		ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
		ResultSet rs = null;
					
		String sql = "select * from film where delYN = 'N' and filmStatus = '상영예정' order by fidx desc ";
			
					
		try {
			pstmt = conn.prepareStatement(sql);	
			rs = pstmt.executeQuery();	
						
			while(rs.next()) {	// while. 값이 있는 동안 돌아라 (if는 하나 가져올때!)
							
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
				
			
	//상영중 영화
	public ArrayList<FilmVo> onFilm () {
							
		ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
		ResultSet rs = null;
							
			String sql = "select * from film where delYN = 'N' and filmStatus = '상영중' order by fidx desc ";
					
							
			try {
				pstmt = conn.prepareStatement(sql);	
				rs = pstmt.executeQuery();	
								
				while(rs.next()) {	// while. 값이 있는 동안 돌아라 (if는 하나 가져올때!)
									
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
			
			
			
			
	// 상영종료 영화
	public ArrayList<FilmVo> offFilm() {

		ArrayList<FilmVo> alist = new ArrayList<FilmVo>();
		ResultSet rs = null;

		String sql = "select * from film where delYN = 'N' and filmStatus = '상영종료' order by fidx desc ";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) { // while. 값이 있는 동안 돌아라 (if는 하나 가져올때!)

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
	
	
	//영화의 총 갯수(페이진+검색기능)
	public int filmTotal(SearchCriteria scri) {
		int cnt =0;
		ResultSet rs =null;
		
		//사용자가 입력한 키워드를 받아서 검색 카테고리에 따라 구분
		String str ="";
		if(scri.getSearchType().equals("filmName")) {
			str = " and filmName like ? ";		
		}
		
		
		//영화 갯수(임시칼럼 cnt 만들어주기 )
		String sql ="select count(*) as cnt from film where delYN='N'"+ str;
		
		try {
			pstmt = conn.prepareStatement(sql);
			// 키워드 가지고 와서 실행되는 쿼리에 맞게 넣어서 실행
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
