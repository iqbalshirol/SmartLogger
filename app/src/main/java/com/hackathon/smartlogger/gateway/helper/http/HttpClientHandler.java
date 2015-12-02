/**
 * ============================================================================================================
 * File Name       	: HttpClientHandler.java
 * Author          	: Apex Team @ Endeavour Software Technologies pvt. ltd., Bangalore.
 * Version         	: 1.0.0
 * Copyright       	: Endeavour
 * Description     	: This class creates http client communication requests sends the request to server and receive response.
 * History         	:
 * ============================================================================================================
 *  Sr. No.	    Date		          Name				 Description
 * ============================================================================================================
 *  1.	   	    16.10.2014           Apex Team            Initial Version.
 *  2.                               Apex Team            Check SVN.
 * ============================================================================================================
 */

package com.hackathon.smartlogger.gateway.helper.http;

import android.util.Log;

import com.hackathon.smartlogger.ConnectionConstants;
import com.hackathon.smartlogger.gateway.exception.ClientTimeOutException;
import com.hackathon.smartlogger.gateway.exception.HttpRequestException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class HttpClientHandler {

	private static final String TAG = "HttpClientHandler";

	private final String serverUrl;
	private final String contentType;
	private HttpClient httpClient = null;

	/**
	 * Constructor of http client handler
	 * 
	 * @param serverUrl
	 *            Server URL to deliver the request data
	 * @param contentType
	 *            request content type
	 */
	public HttpClientHandler(final String serverUrl, final String contentType) {

		this.serverUrl = serverUrl;
		this.contentType = contentType;
	}


	public HttpResponse executeGet() throws ClientTimeOutException, HttpRequestException, IOException {

		try {

			Log.d(TAG, "GET URL: " + serverUrl);

			final HttpParams params = getHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, ConnectionConstants.CONN_TIME_OUT * 1000);
			HttpConnectionParams.setSoTimeout(params, ConnectionConstants.READ_TIME_OUT * 1000);
			HttpConnectionParams.setTcpNoDelay(params, true);
			final ClientConnectionManager clientConnMgr = trustHttps(params);

			httpClient = new DefaultHttpClient(clientConnMgr, params);

			final HttpGet httpget = new HttpGet(serverUrl);
			httpget.setHeader("content-type", contentType);

			return httpClient.execute(httpget);

		} catch (ConnectTimeoutException exception) {
			throw new ClientTimeOutException(exception);

		} catch (HttpHostConnectException exception) {
			throw new HttpRequestException(exception);

		} catch (IOException exception) {
			throw exception;

		}

	}

	/**
	 * Executes the request in POST method
	 * 
	 * @param reqData
	 *            Request data to be posted to server
	 * @return HttpResponse object if request has been executed successfully
	 * @throws ClientTimeOutException
	 *             If time out error occurred
	 * @throws HttpRequestException
	 *             If HTTP host is not proper
	 * @throws IOException
	 *             If error in executing the HTTP client request
	 */
	public HttpResponse executePost(final String reqData) throws ClientTimeOutException, HttpRequestException,
			IOException {

		try {
			Log.d(TAG, "POST URL: " + serverUrl);
			Log.d(TAG, "POST Data: " + reqData);

			final HttpParams params = getHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, ConnectionConstants.CONN_TIME_OUT * 100000);
			HttpConnectionParams.setTcpNoDelay(params, true);
			final ClientConnectionManager clientConnMgr = trustHttps(params);
			httpClient = new DefaultHttpClient(clientConnMgr, params);
			final HttpPost httpPost = new HttpPost(serverUrl);
			httpPost.setHeader("content-type", contentType);
			httpPost.setEntity(new StringEntity(reqData));

			return httpClient.execute(httpPost);

		} catch (ConnectTimeoutException e) {
			throw new ClientTimeOutException(e);

		} catch (HttpHostConnectException e) {
			throw new HttpRequestException(e);

		} catch (IOException e) {
			throw e;

		}
	}

	/**
	 * Closes the connection
	 */
	public void closeConnection() {
		if (httpClient != null) {
			httpClient.getConnectionManager().shutdown();
			httpClient = null;
		}
	}

	/**
	 * method to convert Stream to String
	 * 
	 * @param inputStream
	 * @return String
	 * @throws IOException
	 */
	public static String convertStreamToString(final InputStream inputStream) throws IOException {
		// Initialize variables
		String responce;

		if (inputStream == null) {
			responce = "";
		} else {

			final Writer writer = new StringWriter();
			final char[] buffer = new char[1024];
			try {
				// Reader
				final Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				int size;
				while ((size = reader.read(buffer)) != -1) {
					// writing
					writer.write(buffer, 0, size);
				}
				responce = writer.toString();
				writer.close();
			} finally {
				// closing InputStream
				inputStream.close();
			}

		}
		return responce;
	}

	private ClientConnectionManager trustHttps(final HttpParams params) {

		try {
			final KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			final SSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
			socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			final SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", socketFactory, 443));

			final ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
			return ccm;
		} catch (Exception e) {
			return null;
		}
	}

	private HttpParams getHttpParams() {
		final HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		return params;

	}

	class MySSLSocketFactory extends SSLSocketFactory {

		final private SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(final KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(final X509Certificate[] chain, final String authType)
						throws CertificateException {
					// checkClientTrusted
				}

				public void checkServerTrusted(final X509Certificate[] chain, final String authType)
						throws CertificateException {
					// checkServerTrusted
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose)
				throws IOException {

			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
