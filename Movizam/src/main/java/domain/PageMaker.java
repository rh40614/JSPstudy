package domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PageMaker {
	
	
	private int totalCount;
	private int startPage;
	private int endPage;
	private boolean prev;
	private boolean next;
	private int displayPageNum =10;	//<12345678910>
	private SearchCriteria scri;
	
	
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		clacData();
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public boolean isPrev() {
		return prev;
	}
	public void setPrev(boolean prev) {
		this.prev = prev;
	}
	public boolean isNext() {
		return next;
	}
	public void setNext(boolean next) {
		this.next = next;
	}
	public int getDisplayPageNum() {
		return displayPageNum;
	}
	public void setDisplayPageNum(int displayPageNum) {
		this.displayPageNum = displayPageNum;
	}
	public SearchCriteria getScri() {
		return scri;
	}
	public void setScri(SearchCriteria scri) {
		this.scri = scri;
	}
	
	
	
	//게시물의 갯수에 따라 총 몇페이지가 나와야하는가 
	public void clacData() {
		//끝 페이지 번호 = (현재 페이지 번호 / 화면에 보여질 페이지 번호의 갯수) * 화면에 보여질 페이지 번호의 갯수
		//displayPageNum= 10으로 설정해주었음으로 페이지수를 ceil로 올림 처리해서 10으로 나눠지게 한다. 그 후에 다시 곱해서 정리!
		//현재 3페이지면 끝 페이지는 10. 현재 12면 끝은 20. 
		endPage = (int)(Math.ceil(scri.getPage()/(double)displayPageNum)*displayPageNum);
		
		//끝페이지가 10,20,30,, 일때 시작 페이지는 (10-10)+1=1.  (20-10)+1=11
		startPage =(endPage - displayPageNum)+1;
		
		//실제로 나오는 페이지 개수 = (총게시물의 수/한 페이지당 게시물수)
		//마지막 페이지가 실제 개수보다 크면 실제 개수만큼만 출력을해라
		int tempEndPage = (int)(Math.ceil(totalCount/(double)scri.getPerPageNum()));
		if(endPage > tempEndPage) {
			endPage = tempEndPage;
		}
		
		//시작 페이지가 1이면 이전페이지 없음. 
		//마지막 페이지에 출력게시물을 곱해서 실제개수보다 많거나 같으면 다음 페이지 없음
		prev = startPage == 1 ? false : true;
		next = endPage* scri.getPerPageNum() >= totalCount? false:true;
	}
	
	
	
	
	
	//검색을할때 한국어 키워드로 검색을하면 주소창에 parameter값으로 한글이 올라가게된다. 주소창에 한글이 올라가도 깨지지 않도록 인코딩해준다.
		public String encoding(String keyword) {	
		
			String str="";
			
			try {
					
				if(keyword != null) {	//값이 존재한다면
				str = URLEncoder.encode(keyword, "UTF-8");
				}
					
			} catch (UnsupportedEncodingException e) {
					
				e.printStackTrace();
			}
				
				return str;
			
		}
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
