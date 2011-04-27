/*
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package voter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

/**
 * This program will automatically vote for the tum-tum-pa competition GET
 * /tumtumpa_usa_voting
 * /request.json.php?r=vote&v=25&cid=81028&ckey=878746&format
 * =jsonp&callback=jsonp1303925617610&_=1303925660055 HTTP/1.1
 */
public class ClientFormLogin {

	public static void main(String[] args) {
		int votes = 0;
		int tries = 0;
		Random r = new Random(40L);
		// lets get it to vote just as many times as we need
		while (votes < Integer.MAX_VALUE - 1) {
			try {
				// TODO: need to pull the while outside the try

				boolean voted = tumVote();

				if (voted) {
					votes++;
					tries = 0;

					// wait 25min + 0-11min
					Thread.sleep(1500000 + r.nextInt(660000));
					System.out.println("Voted!");
				}else{
					System.out.println("NO Vote. prob captcha.");
					if(tries < 3){
						
					}else{
						// wait a while longer
						// 45min + 0-20min
						Thread.sleep(2700000 + r.nextInt(1200000));
					}
				}

			} catch (Exception e) {
				System.out.println("Vote Failed");
				e.printStackTrace();
				if (tries < 3) {
					tries++;
				} else {
					return;
				}
			}
		}

	}

	public static boolean tumVote() throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			// //Get the captcha

			// http://redbulli.com//tumtumpa_usa_voting/request.json.php?r=captcha&format=jsonp
			String captchaRequest = "http://redbulli.com//tumtumpa_usa_voting/request.json.php?r=captcha&format=json";
			HttpGet httpget2 = new HttpGet(captchaRequest);

			HttpResponse response2 = httpclient.execute(httpget2);
			HttpEntity entity2 = response2.getEntity();

			// Response should be
			// {"errors":[],"cid":85442,"image":"c27041114393527721b.jpg"}

			// retrieve the output and display it in console
			String rdata = ClientFormLogin.convertInputStreamToString(response2.getEntity().getContent());
			System.out.print(rdata);

			// put the json into an object
			Gson gson = new Gson();
			Captcha captchaObject = gson.fromJson(rdata, Captcha.class);

			EntityUtils.consume(entity2);

			System.out.println("Initial set of cookies:");
			List<Cookie> cookies2 = httpclient.getCookieStore().getCookies();
			if (cookies2.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies2.size(); i++) {
					System.out.println("- " + cookies2.get(i).toString());
				}
			}

			// //Run the captcha through the ocr
			downloadCaptchaImage(captchaObject.getImageURL());
			String captchaText = reverseCaptcha(captchaObject.getImageName());

			// //Build the new get url with the params

			String domain = "http://redbulli.com/tumtumpa_usa_voting/request.json.php?r=vote";
			// v is the video id
			String v = "&v=25";
			// cid is the captcha-id
			String cid = "&cid=" + captchaObject.getCid();
			// ckey is what the user entered into the text field
			String ckey = "&ckey=" + captchaText;
			// format is the type we want back
			String format = "&format=json";

			String sRequest = domain + v + cid + ckey + format;

			HttpGet httpget = new HttpGet(sRequest);

			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			// System.out.println("Respone was: " + response.getStatusLine());

			// retrieve the output and display it in console
			String voteResponseData = ClientFormLogin.convertInputStreamToString(response.getEntity().getContent());
			System.out.print(voteResponseData);

			// put the json into an object
			Gson gson2 = new Gson();
			VoteResponse voteResponseObject = gson2.fromJson(voteResponseData, VoteResponse.class);

			EntityUtils.consume(entity);

			System.out.println("New set of cookies:");
			List<Cookie> cookies = httpclient.getCookieStore().getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
				}
			}

			// the results can be either
			// jsonp1303924843141({"errors":[],"success":1});
			// or
			// jsonp1303925617610({"errors":["Invalid Captcha Entry"],"success":0});

			// //now either do it again if success is 0
			// //or sleep until its time to vote again
			if (voteResponseObject.getSuccess() == 0) {
				// it failed
				return false;
			} else if (voteResponseObject.getSuccess() == 1) {
				// it passed
				return true;
			} else {
				// im gonna go with it failed
				return false;
			}

			//			
			//
			// List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			// nvps.add(new BasicNameValuePair("char", "Stringstix"));
			// nvps.add(new BasicNameValuePair("vote5", "Vote"));
			//
			// httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			//
			// response = httpclient.execute(httpost);
			// entity = response.getEntity();
			//
			// System.out.println("Login form get: " +
			// response.getStatusLine());
			//
			// // retrieve the output and display it in console
			// System.out.print(convertInputStreamToString(response.getEntity().getContent()));
			//
			// EntityUtils.consume(entity);
			//
			// System.out.println("Post logon cookies:");
			// cookies = httpclient.getCookieStore().getCookies();
			// if (cookies.isEmpty()) {
			// System.out.println("None");
			// } else {
			// for (int i = 0; i < cookies.size(); i++) {
			// System.out.println("- " + cookies.get(i).toString());
			// }
			// }

		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
	}

	public static void downloadCaptchaImage(String imageURL) throws InterruptedException {

		try {

			// String imageURL =
			// "http://redbulli.com/tumtumpa_usa_voting/images/captchas/c270411170633ae3e10.jpg";
			String command = "wget -P /tmp/" + " " + imageURL;

			// run the Unix "ps -ef" command
			// using the Runtime exec method:
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String reverseCaptcha(String imageName) {
		String s = null;

		try {

			// String imageName = "/tmp/c270411160310c119d3.jpg";
			String command = "gocr" + " " + "/tmp/" + imageName;

			// run the Unix "ps -ef" command
			// using the Runtime exec method:
			Process p = Runtime.getRuntime().exec(command);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			StringBuilder b = new StringBuilder();

			// read the output from the command
			// System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				b.append(s);
			}

			String result = b.toString();
			result = result.replaceAll("\\W|_", "");
			System.out.println(result);
			return result;

			// read any errors from the attempted command
			// System.out.println("Here is the standard error of the command (if any):\n");
			// while ((s = stdError.readLine()) != null) {
			// System.out.println(s);
			// }
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void vote() throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpGet httpget = new HttpGet("http://gamezaion.com/index.php?page=vote");

			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			System.out.println("Respone was: " + response.getStatusLine());
			EntityUtils.consume(entity);

			System.out.println("Initial set of cookies:");
			List<Cookie> cookies = httpclient.getCookieStore().getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
				}
			}

			HttpPost httpost = new HttpPost("http://gamezaion.com/index.php?page=vote");

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("char", "Stringstix"));
			nvps.add(new BasicNameValuePair("vote5", "Vote"));

			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

			response = httpclient.execute(httpost);
			entity = response.getEntity();

			System.out.println("Login form get: " + response.getStatusLine());

			// retrieve the output and display it in console
			System.out.print(convertInputStreamToString(response.getEntity().getContent()));

			EntityUtils.consume(entity);

			System.out.println("Post logon cookies:");
			cookies = httpclient.getCookieStore().getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
				}
			}

		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
	}

	/**
	 * method to convert an InputStream to a string using the
	 * BufferedReader.readLine() method this methods reads the InputStream line
	 * by line until the null line is encountered it appends each line to a
	 * StringBuilder object for optimal performance
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String convertInputStreamToString(InputStream inputStream) throws IOException {
		if (inputStream != null) {
			StringBuilder stringBuilder = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line).append("\n");
				}
			} finally {
				inputStream.close();
			}

			return stringBuilder.toString();
		} else {
			return null;
		}
	}
}
