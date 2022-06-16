package mysqlProject.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import mysqlProject.domain.SearchCriteria;

public class PageMaker {

	private int totalCount;
	private int startPage;
	private int endPage;
	private boolean prev;
	private boolean next;
	private int displayPageNum = 10;		// < 1 2 3 4 5 6 7 8 9 10 >
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
		calcData();
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
	
	public void calcData() {
		//endPage = 1						올림(1/10) 후 *10 10이하 = 10, 11이상 = 20
		endPage = (int)(Math.ceil(scri.getPage()/(double)displayPageNum) * displayPageNum);
		//startPage = -8 		(10-10)+1
		startPage = (endPage - displayPageNum) + 1;
		//tempEndPage = 13			185/15 = 올림(12.3333)
		int tempEndPage = (int)(Math.ceil(totalCount/(double)scri.getPerPageNum()));
		
		if(endPage>tempEndPage) {
			endPage = tempEndPage;
		}
		//prev = -8 == 1? false
		prev = startPage == 1 ? false:true;
		//next = 10 * 15 >= 185? false
		next = endPage* scri.getPerPageNum() >= totalCount? false:true;
	}
	
	public String encoding(String keyword) {
		String str = "";
		
		try {
			if(keyword != null) {
			str = URLEncoder.encode(keyword,"UTF-8");
			System.out.println(str);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return str;
	}
}
