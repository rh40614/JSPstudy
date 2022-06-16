package mysqlProject.domain;

public class BoardVo {
	
	private int bidx;
	private int originbidx;
	private int depth;
	private int level_;
	private int category;
	private String title;
	private String content;
	private String writer;
	private String writeday;
	private String boardip;
	private String delyn;
	private int midx;
	private int boardViews;
	private int boardComment;
	private String notice;
	private String profile;
	
	public int getBoardComment() {
		return boardComment;
	}
	public void setBoardComment(int boardComment) {
		this.boardComment = boardComment;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	public int getBoardViews() {
		return boardViews;
	}
	public void setBoardViews(int boardView) {
		this.boardViews = boardView;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getBidx() {
		return bidx;
	}
	public void setBidx(int bidx) {
		this.bidx = bidx;
	}
	public int getOriginbidx() {
		return originbidx;
	}
	public void setOriginbidx(int originbidx) {
		this.originbidx = originbidx;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getLevel_() {
		return level_;
	}
	public void setLevel_(int level_) {
		this.level_ = level_;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getWriteday() {
		return writeday;
	}
	public void setWriteday(String writeday) {
		this.writeday = writeday;
	}
	public String getBoardip() {
		return boardip;
	}
	public void setBoardip(String boardip) {
		this.boardip = boardip;
	}
	public String getDelyn() {
		return delyn;
	}
	public void setDelyn(String delyn) {
		this.delyn = delyn;
	}
	public int getMidx() {
		return midx;
	}
	public void setMidx(int midx) {
		this.midx = midx;
	}
	
	
}
