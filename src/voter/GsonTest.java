package voter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GsonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		try {

			String imageURL = "http://redbulli.com/tumtumpa_usa_voting/images/captchas/c270411170633ae3e10.jpg";
			String command = "wget -P /tmp/" + " " + imageURL;

			// run the Unix "ps -ef" command
			// using the Runtime exec method:
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
