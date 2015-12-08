package com.u14n.sandbox.spring.rest.service;

import sun.misc.BASE64Encoder;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.get;
import com.jayway.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.CoreMatchers.containsString;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LocationsResourceWithRestAssuredTest {
	private static final int MAX_BUFFER_SIZE = 256; //Maximal size of the chars

	private static Server server;

	/**
	 * @throws Exception
	 * @see <a href="http://stackoverflow.com/questions/5267423/using-a-jetty-server-with-junit-tests">Using a Jetty Server with JUnit tests</a>
	 */
	@BeforeClass
	public static void setUpClass() throws Exception {
		server = new Server(8080);
		server.setStopAtShutdown(true);
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/");
		webAppContext.setResourceBase("src/main/webapp");
		webAppContext.setClassLoader(
				LocationsResourceWithRestAssuredTest.class.getClassLoader());
		server.addHandler(webAppContext);
		server.start();
	}

	@Ignore @Test
	public void shouldBeAuthenticated() throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpUriRequest request =
				new HttpGet("http://localhost:8080/restapi/locations");
		BASE64Encoder encoder = new BASE64Encoder();
		String encoded = encoder.encode("test:pass".getBytes());
		request.setHeader("Authorization", "Basic " + encoded);
		HttpResponse response = client.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		Reader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));
		StringBuilder builder = new StringBuilder();
		char[] chars = new char[MAX_BUFFER_SIZE];
		int read;
		while ((read = reader.read(chars, 0, MAX_BUFFER_SIZE)) > 0) {
			builder.append(chars, 0, read);
		}
		String string = builder.toString();
		assertEquals("[{\"countryCode\":\"USA\",\"regionCode\":\"NC\",\"postalCode\":\"27601\",\"city\":\"Raleigh\",\"street\":\"500 S McDowell St\",\"revision\":-1,\"id\":9223372035280100646},{\"countryCode\":\"USA\",\"regionCode\":\"NC\",\"postalCode\":\"27601\",\"city\":\"Raleigh\",\"street\":\"100 E Davie Street\",\"revision\":-1,\"id\":9223372034730584180}]", string);
																				System.out.println("LocationsResourceWithRestAssuredTest.shouldBeAuthenticated() string=" + string);
		reader.close();
	}

	@Ignore @Test
	public void shouldNotBeAuthenticated() throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpUriRequest request =
				new HttpGet("http://localhost:8080/restapi/locations");
		BASE64Encoder encoder = new BASE64Encoder();
		String encoded = encoder.encode("test:pass1".getBytes());
		request.setHeader("Authorization", "Basic " + encoded);
		HttpResponse response = client.execute(request);
		assertEquals(401, response.getStatusLine().getStatusCode());
		assertEquals("Bad credentials",
				response.getStatusLine().getReasonPhrase());
		Reader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));
		StringBuilder builder = new StringBuilder();
		char[] chars = new char[MAX_BUFFER_SIZE];
		int read;
		while ((read = reader.read(chars, 0, MAX_BUFFER_SIZE)) > 0) {
			builder.append(chars, 0, read);
		}
		String string = builder.toString();
		assertThat(string, containsString("Error 401 Bad credentials"));
																				System.out.println("LocationsResourceWithRestAssuredTest.shouldBeAuthenticated() string=" + string);
		reader.close();
	}

	@Ignore @Test
	public void shouldNotBeGotten() throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpUriRequest request =
				new HttpGet("http://localhost:8080/restapi/locations");
		HttpResponse response = client.execute(request);
		assertEquals(401, response.getStatusLine().getStatusCode());
		assertEquals(
				"Full authentication is required to access this resource",
				response.getStatusLine().getReasonPhrase());
		Reader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));
		StringBuilder builder = new StringBuilder();
		char[] chars = new char[MAX_BUFFER_SIZE];
		int read;
		while ((read = reader.read(chars, 0, MAX_BUFFER_SIZE)) > 0) {
			builder.append(chars, 0, read);
		}
		String string = builder.toString();
		assertThat(string, containsString(
			"Error 401 Full authentication is required to access this resource"));
																				System.out.println("LocationsResourceWithRestAssuredTest.shouldBeAuthenticated() string=" + string);
		reader.close();
	}
}