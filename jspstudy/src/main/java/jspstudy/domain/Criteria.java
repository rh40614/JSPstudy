package jspstudy.domain;

public class Criteria {

	private int page;	//현재 화면에서 내가 위치한 페이지 번호
	private int perPageNum;	// 한 화면당 게시물 출력 개수
	
	
	//생성자
	public Criteria() {
		this.page=1;
		this.perPageNum=15;
	}
	
	public int getPage() {
		return page;
	}

	public int getPerPageNum() {
		return perPageNum;
	}

	
	public void setPerPageNum(int perPageNum) {
		if(perPageNum <=0 || perPageNum > 100) {
			this.perPageNum =10;
			return;
		}
		this.perPageNum= perPageNum;
	}

	public void setPage(int page) {
		if(page<=1) {
			this.page =1;
			return;
		}
		this.page=page;
	}
	
	
}
