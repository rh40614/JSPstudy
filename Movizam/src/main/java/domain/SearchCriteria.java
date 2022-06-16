package domain;

public class SearchCriteria extends Criteria{	//페이징 기준 
	
	private String searchType;
	private String keyword;
	
	
	//생성자 검색키워드가 없는 것이 기본임으로 빈값으로 초기화해서 생성하기
	public SearchCriteria() {
		this.searchType="";
		this.keyword="";
	}
	
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	
	
	
	
	
}
