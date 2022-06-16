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
	
	
	

	public void clacData() { //�Խù��� ������ ���� ���������� �� ���;��ϴ����� ����ϴ� �޼���
		
		//�� ������ ��ȣ = (���� ������ ��ȣ / ȭ�鿡 ������ ������ ��ȣ�� ����) * ȭ�鿡 ������ ������ ��ȣ�� ����
		//���� ���� 5�������� ���������� 10. �������� 14�������̸� ���������� 20.
		endPage= (int)(Math.ceil(scri.getPage()/(double)displayPageNum)*displayPageNum);	   
		
		//10���� �������� ���������� �������� ��ȣ�� 10,20,30,40 ����̴�.
		startPage =(endPage - displayPageNum)+1;
		
		//�ѰԽù��� ���� ȭ�鿡 ���� �Խù��� ���� ���������� ������ ���� ���� ������ ���� .
		//������ �������� ���� �������� ũ�� ���� ������ŭ�� ������ض�
		int tempEndPage = (int)(Math.ceil(totalCount/(double)scri.getPerPageNum()));
		if(endPage > tempEndPage) {
			endPage = tempEndPage;
		}
		
		prev = startPage == 1 ? false : true;
		next = endPage* scri.getPerPageNum() >= totalCount? false:true;
	}
	
	
	
	
	
	
	
	//�˻����Ҷ� �ѱ��� Ű����� �˻����ϸ� �ּ�â�� parameter������ �ѱ��� �ö󰡰Եȴ�. �ּ�â�� �ѱ��� �ö󰡵� ������ �ʵ��� ���ڵ����ش�.
	public String encoding(String keyword) {	
	
		String str="";
		
		try {
				
			if(keyword != null) {	//���� �����Ѵٸ�
			str = URLEncoder.encode(keyword, "UTF-8");
			}
				
		} catch (UnsupportedEncodingException e) {
				
			e.printStackTrace();
		}
			
			return str;
		
	}
	
	
	
	
	
	
	
	
	
	
}
