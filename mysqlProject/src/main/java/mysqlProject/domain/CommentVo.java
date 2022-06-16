package mysqlProject.domain;

public class CommentVo {
	
	private int cidx;
	private int bidx;
	private String writer;
	private String content;
	private String day;
	private String delyn;
	private int level_;
	private int originCidx;
	private int count;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getOriginCidx() {
		return originCidx;
	}
	public void setOriginCidx(int originCidx) {
		this.originCidx = originCidx;
	}
	public int getLevel_() {
		return level_;
	}
	public void setLevel_(int level_) {
		this.level_ = level_;
	}
	public int getCidx() {
		return cidx;
	}
	public void setCidx(int cidx) {
		this.cidx = cidx;
	}
	public int getBidx() {
		return bidx;
	}
	public void setBidx(int bidx) {
		this.bidx = bidx;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getDelyn() {
		return delyn;
	}
	public void setDelyn(String delyn) {
		this.delyn = delyn;
	}
	
	
}
