package jspstudy.domain;

public class Criteria {

	private int page;	//���� ȭ�鿡�� ���� ��ġ�� ������ ��ȣ
	private int perPageNum;	// �� ȭ��� �Խù� ��� ����
	
	
	//������
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
