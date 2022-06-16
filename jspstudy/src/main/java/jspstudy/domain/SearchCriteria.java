package jspstudy.domain;

public class SearchCriteria extends Criteria{	//상속

	private String searchType;
	private String keyword;
	
	
	
	public SearchCriteria() {	//생성하면 빈값으로나타남
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
