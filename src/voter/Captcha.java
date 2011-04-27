package voter;

public class Captcha {
	//{"errors":[],"cid":85442,"image":"c27041114393527721b.jpg"}
	//private String[] errors;
	private int cid;
	private String image;
	
	public String getImageName(){
		return this.image;
	}
	
	public int getCid(){
		return this.cid;
	}
	
	public String getImageURL(){
		return "http://redbulli.com/tumtumpa_usa_voting/images/captchas/" + this.image;
	}

}
