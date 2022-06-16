package jspstudy.domain;

//용도는 db에서 member 컬럼 값을 담는 역할(bean)로 멤버 변수를 db와 똑같이 설정해준다. 
public class MemberVo {
//ctrl+shith+y 소문자로 바꾸기
	
	
	private String memberId;
	private int midx;
	private String memberpwd; 
	private String membername;
	private String memberemail;
	private String membergender;
	private String memberadress;
	private String memberphone;
	private String memberjumin; 
	private String writeday;
	private String delyn;
	private String hobby; 
	private String memberip;
	
	private String memberGrade;
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getMidx() {
		return midx;
	}
	public void setMidx(int midx) {
		this.midx = midx;
	}
	public String getMemberpwd() {
		return memberpwd;
	}
	public void setMemberpwd(String memberpwd) {
		this.memberpwd = memberpwd;
	}
	public String getMembername() {
		return membername;
	}
	public void setMembername(String membername) {
		this.membername = membername;
	}
	public String getMemberemail() {
		return memberemail;
	}
	public void setMemberemail(String memberemail) {
		this.memberemail = memberemail;
	}
	public String getMembergender() {
		return membergender;
	}
	public void setMembergender(String membergender) {
		this.membergender = membergender;
	}
	public String getMemberadress() {
		return memberadress;
	}
	public void setMemberadress(String memberadress) {
		this.memberadress = memberadress;
	}
	public String getMemberphone() {
		return memberphone;
	}
	public void setMemberphone(String memberphone) {
		this.memberphone = memberphone;
	}
	public String getMemberjumin() {
		return memberjumin;
	}
	public void setMemberjumin(String memberjumin) {
		this.memberjumin = memberjumin;
	}
	public String getWriteday() {
		return writeday;
	}
	public void setWriteday(String writeday) {
		this.writeday = writeday;
	}
	public String getMemberip() {
		return memberip;
	}
	public void setMemberip(String memberip) {
		this.memberip = memberip;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public String getdelyn() {
		return delyn;
	}
	public void setdelyn(String delyn) {
		this.delyn = delyn;
	}
	public String getMemberGrade() {
		return memberGrade;
	}
	public void setMemberGrade(String memberGrade) {
		this.memberGrade = memberGrade;
	}
	
	
	
	
	
	
	
}
