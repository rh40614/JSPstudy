package domain;

public class Criteria {
	
	private int page;	
	private int perPageNum;
	
	//������ 
	public Criteria() {
		this.page =1;		//���� ȭ�鿡�� ���� ��ġ�� ������ ��ȣ
		this.perPageNum=15;	// �� ȭ��� �Խù��� � ����Ұ�����. �⺻������ ��� ����
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
		
		//�Խù��� 10�� �����϶�. ex) 4���̸� 10������ �������� ���� ��� 0�̾ 10�� �ڸ�Ȯ��
		//100�� ������ �����͸� �����ؼ� �������������� �Խñ��� 15���� �ȵǸ� 10�� �ڸ� Ȯ��
		if(perPageNum <=0 || perPageNum >100) {	
			this.perPageNum =10;
			return;
		}
		this.perPageNum = perPageNum;
	}
	
	
	
	
	public void setPage(int page) {
		//controller���� page �Ķ���͸� 1�� �����ϰ�  ���⼭�� 1�� �����ϱ�
		if(page<=1){
			this.page =1;
			return;
		}
		
		this.page = page;
	}
	
	
	
	
	
}
