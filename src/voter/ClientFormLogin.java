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

/**
 * This program will automatically vote for the tum-tum-pa competition
 * GET /tumtumpa_usa_voting/request.json.php?r=vote&v=25&cid=81028&ckey=878746&format=jsonp&callback=jsonp1303925617610&_=1303925660055 HTTP/1.1
 */
public class ClientFormLogin {

	public static void main(String[] args) {

	}

	public static void tumVote() throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpGet httpget2 = new HttpGet("http://gamezaion.com/index.php?page=vote");

			HttpResponse response2 = httpclient.execute(httpget2);
			HttpEntity entity2 = response2.getEntity();

			System.out.println("Respone was: " + response2.getStatusLine());
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

			String domain = "http://redbulli.com/tumtumpa_usa_voting/request.json.php?r=vote";
			// v is the video id
			String v = "&v=25";
			// cid is the captcha-id
			String cid = "&cid=27931";
			//ckey is what the user entered into the text field
			String ckey = "&ckey=907867";
			//format is the type we want back
			String format = "&format=jsonp";
			
			String sRequest = domain + v + cid + ckey + format;
			
			HttpGet httpget = new HttpGet(sRequest);
			
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			System.out.println("Respone was: " + response.getStatusLine());
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
			//jsonp1303924843141({"errors":[],"success":1});
			// or
			//jsonp1303925617610({"errors":["Invalid Captcha Entry"],"success":0});
			
//			
//
//			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//			nvps.add(new BasicNameValuePair("char", "Stringstix"));
//			nvps.add(new BasicNameValuePair("vote5", "Vote"));
//
//			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
//
//			response = httpclient.execute(httpost);
//			entity = response.getEntity();
//
//			System.out.println("Login form get: " + response.getStatusLine());
//
//			// retrieve the output and display it in console
//			System.out.print(convertInputStreamToString(response.getEntity().getContent()));
//
//			EntityUtils.consume(entity);
//
//			System.out.println("Post logon cookies:");
//			cookies = httpclient.getCookieStore().getCookies();
//			if (cookies.isEmpty()) {
//				System.out.println("None");
//			} else {
//				for (int i = 0; i < cookies.size(); i++) {
//					System.out.println("- " + cookies.get(i).toString());
//				}
//			}

		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
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
