package voter;

public class VoteResponse {
	// the results can be either
	// jsonp1303924843141({"errors":[],"success":1});
	// or
	// jsonp1303925617610({"errors":["Invalid Captcha Entry"],"success":0});
	//private String[] errors;
	private int success;
	
	public int getSuccess(){
		return this.success;
	}

}
