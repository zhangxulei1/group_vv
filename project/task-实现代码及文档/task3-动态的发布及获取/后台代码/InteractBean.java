package bean;

public class InteractBean {
	private int inteactId;
    private String userName;
    private String userTouxiang;
    private String interactTime;
    private String interactContent;
    private String interactPhoto;
    private String interactPraise;
    
    public InteractBean() {
    	this.inteactId = inteactId;
    	this.userName = userName;
    	this.userTouxiang = userTouxiang;
    	this.interactTime = interactTime;
    	this.interactContent = interactContent;
    	this.interactPhoto = interactPhoto;
    	this.interactPraise = interactPraise;
    }
    
    
    public  InteractBean(String userName,String userTouxiang,
    		String interactTime,String interactContent,String interactPhoto,
    		String interactPraise) {
    	this.userName = userName;
    	this.userTouxiang = userTouxiang;
    	this.interactTime = interactTime;
    	this.interactContent = interactContent;
    	this.interactPhoto = interactPhoto;
    	this.interactPraise = interactPraise;
    }

	public int getInteactId() {
		return inteactId;
	}


	public void setInteactId(int inteactId) {
		this.inteactId = inteactId;
	}


	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserTouxiang() {
		return userTouxiang;
	}
	public void setUserTouxiang(String userTouxiang) {
		this.userTouxiang = userTouxiang;
	}
	public String getInteractTime() {
		return interactTime;
	}
	public void setInteractTime(String interactTime) {
		this.interactTime = interactTime;
	}
	public String getInteractContent() {
		return interactContent;
	}
	public void setInteractContent(String interactContent) {
		this.interactContent = interactContent;
	}
	public String getInteractPhoto() {
		return interactPhoto;
	}
	public void setInteractPhoto(String interactPhoto) {
		this.interactPhoto = interactPhoto;
	}
	public String getInteractPraise() {
		return interactPraise;
	}
	public void setInteractPraise(String interactPraise) {
		this.interactPraise = interactPraise;
	}

}
