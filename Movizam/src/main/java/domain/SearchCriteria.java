package domain;

public class SearchCriteria extends Criteria{	//����¡ ���� 
	
	private String searchType;
	private String keyword;
	
	
	//������ �˻�Ű���尡 ���� ���� �⺻������ ������ �ʱ�ȭ�ؼ� �����ϱ�
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
