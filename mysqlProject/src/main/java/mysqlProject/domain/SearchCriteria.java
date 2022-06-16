package mysqlProject.domain;

public class SearchCriteria extends Criteria{
	
	private String searchType;
	private String keyword;
	private int category;
	
	public SearchCriteria() {
		this.searchType = "";
		this.keyword = "";
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

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

}
