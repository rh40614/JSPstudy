package domain;

public class Criteria {
	
	private int page;	
	private int perPageNum;
	
	//생성자 
	public Criteria() {
		this.page =1;		//현재 화면에서 내가 위치한 페이지 번호
		this.perPageNum=15;	// 한 화면당 게시물울 몇개 출력할것인지. 기본적으로 출력 개수
	}
	
	//getter
	public int getPage() {
		return page;
	}
	
	public int getPerPageNum() {
		return perPageNum;
	}
	
	
	//setter
	public void setPerPageNum(int perPageNum) {
		
		//게시물이 10개 이하일때. ex) 4개이면 10개까지 나오도록 라인 잡기 0이어도 10개 자리확보
		//100개 정도의 데이터를 생각해서 마지막페이지에 게시글이 15개가 안되면 10개 자리 확보
		if(perPageNum <=0 || perPageNum >100) {	
			this.perPageNum =10;
			return;
		}
		this.perPageNum = perPageNum;
	}
	
	
	
	
	public void setPage(int page) {
		//controller에서 page 파라미터를 1로 설정하고  여기서도 1로 설정하기
		if(page<=1){
			this.page =1;
			return;
		}
		
		this.page = page;
	}
	
	
	
	
	
}
