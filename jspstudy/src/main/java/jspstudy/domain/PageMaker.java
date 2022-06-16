package jspstudy.domain;

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
	
	
	
	public SearchCriteria getScri() {
		return scri;
	}
	public void setScri(SearchCriteria scri) {
		this.scri = scri;
	}
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
	
	
	

	public void clacData() { //게시물의 개수에 따라 몇페이지가 총 나와야하는지를 계산하는 메서드
		
		//끝 페이지 번호 = (현재 페이지 번호 / 화면에 보여질 페이지 번호의 갯수) * 화면에 보여질 페이지 번호의 갯수
		//내가 지금 5페이지면 끝페이지는 10. 내가지금 14페이지이면 끝페이지는 20.
		endPage= (int)(Math.ceil(scri.getPage()/(double)displayPageNum)*displayPageNum);	   
		
		//10개씩 나오도록 설정함으로 끝페이지 번호는 10,20,30,40 등등이다.
		startPage =(endPage - displayPageNum)+1;
		
		//총게시물의 수를 화면에 띄우는 게시물의 수로 나누었을때 나오는 수가 실제 페이지 개수 .
		//마지막 페이지가 실제 개수보다 크면 실제 개수만큼만 출력을해라
		int tempEndPage = (int)(Math.ceil(totalCount/(double)scri.getPerPageNum()));
		if(endPage > tempEndPage) {
			endPage = tempEndPage;
		}
		
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
