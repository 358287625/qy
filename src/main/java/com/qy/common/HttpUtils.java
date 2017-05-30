package com.qy.common;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils
{
	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);
	private static PoolingHttpClientConnectionManager httpClientConnectionManager = null;
	private static final HttpUtils httpUtils = new HttpUtils();
	/**
	 *  Determines the timeout in milliseconds until a connection is established.
	 */
	private static int CONNECT_TIMEOUT = 3000;// 3秒
	/**
	 *  Defines the socket timeout ({@code SO_TIMEOUT}) in milliseconds,
	 *   which is the timeout for waiting for data  or, put differently,
	 *    a maximum period inactivity between two consecutive data packets).
	 */
	private static int SOCKET_TIMEOUT = 10000;// 10秒
	/**
	 * Returns the timeout in milliseconds used when requesting a connection from the connection manager
	 */
//	private static int REQUEST_TIMEOUT = 2000;// 2秒
	private static int MAX_TOTAL_CONN = 5000;
	private static int MAX_PER_ROUTE = 2500;
	private static int HTTP_RETRY_TIMES = 2;// http重试请求次数
	private static int BUFFER_SIZE = 8096;// 缓冲区大小
	// 需要通过以下代码声明对https连接支持
	Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
			.register("http", PlainConnectionSocketFactory.getSocketFactory())
			.register("https", SSLConnectionSocketFactory.getSocketFactory()).build();

	// 请求重试机制
	private HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler()
	{

		
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context)
		{
			if (executionCount >= HTTP_RETRY_TIMES) // 超过三次则不再重试请求
				return false;

			else if (exception instanceof NoHttpResponseException)
			{// 如果服务器丢掉了连接，那么就重试
				return true;
			}
			else if (exception instanceof SSLHandshakeException)
			{// 不要重试SSL握手异常
				return false;
			}
			else if (exception instanceof InterruptedIOException)
			{// 超时
				return true;
			}
			else if (exception instanceof UnknownHostException)
			{// 目标服务器不可达
				log.error(exception.getMessage(), exception);
				return false;
			}
			else if (exception instanceof ConnectTimeoutException)
			{// 连接被拒绝
				log.error(exception.getMessage(), exception);
				return false;
			}
			else if (exception instanceof SSLException)
			{// ssl握手异常
				return false;
			}

			HttpClientContext clientContext = HttpClientContext.adapt(context);
			HttpRequest request = clientContext.getRequest();
			boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
			if (idempotent)// Retry if the request is considered idempotent
				return true;

			return true;
		}
	};

	private HttpUtils()
	{
		httpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
	}

	public static HttpUtils getInstance()
	{
		return httpUtils;
	}

	// 创建全局的requestConfig
	private static RequestConfig reqconfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT)/*.setConnectTimeout(CONNECT_TIMEOUT)*/
			.setConnectionRequestTimeout(CONNECT_TIMEOUT).setStaleConnectionCheckEnabled(true).build();

	private CloseableHttpClient getHttpClient()
	{
		httpClientConnectionManager.setMaxTotal(MAX_TOTAL_CONN);
		httpClientConnectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpClientConnectionManager)
				.setDefaultRequestConfig(reqconfig).setRedirectStrategy(new LaxRedirectStrategy())// 声明重定向策略对象
				.setRetryHandler(myRetryHandler).setMaxConnPerRoute(MAX_PER_ROUTE).setMaxConnTotal(MAX_TOTAL_CONN).build();

		return httpClient;
	}

	private void configHttpRequst(HttpRequestBase httprequestbase)
	{
		httprequestbase.setConfig(reqconfig);
		httprequestbase.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
		httprequestbase.setProtocolVersion(HttpVersion.HTTP_1_0);
		httprequestbase.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
		httprequestbase.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, CONNECT_TIMEOUT);
		httprequestbase.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);
	}

	/**
	 * 发送get请求 callHtmlRequestGet
	 * 
	 * @param httpRequestUrl
	 * @return String
	 */
	public String callHtmlRequestGet(String httpRequestUrl)
	{

		HttpGet get = new HttpGet(httpRequestUrl);
		configHttpRequst(get);
		CloseableHttpResponse resp = null;
		try
		{
			resp = HttpUtils.getInstance().getHttpClient().execute(get);
			HttpEntity entity = resp.getEntity();
//			System.out.println(resp.getEntity().getContentType());
			InputStream in = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null)
			{
				builder.append(line + "\n");
			}
			return builder.toString();
		}
		catch (Exception e)
		{
			log.error("error-Exception= " + e.getMessage() + "---" + httpRequestUrl, e);
		}
		finally
		{
			try
			{
				if (resp != null)
				{
					EntityUtils.consume(resp.getEntity()); // 会自动释放连接
					resp.close();
				}
				get.abort();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * post方式发送xml数据 callHttpPostXmlData
	 * 
	 * @param httpRequestUrl
	 * @param xml
	 * @return String
	 */
	public String callHttpPostXmlData(String httpRequestUrl, String xml)
	{

		HttpPost post = new HttpPost(httpRequestUrl);
		configHttpRequst(post);
		CloseableHttpResponse resp = null;
		try
		{
			post.setHeader("Content-Type", "text/xml;charset=utf-8");
			StringEntity strEntity = new StringEntity(xml, "UTF-8");
			post.setEntity(strEntity);
			resp = getInstance().getHttpClient().execute(post);
			int code = resp.getStatusLine().getStatusCode();
			if (code == 200)
			{
				HttpEntity entity = resp.getEntity();
				InputStream in = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				StringBuilder builder = new StringBuilder();
				String line = null;
				while ((line = br.readLine()) != null)
				{
					builder.append(line + "\n");
				}
				return builder.toString();
			}
		}
		catch (Exception ex)
		{
			log.error("timeout-error-Exception= " + httpRequestUrl);
		}
		finally
		{
			try
			{
				if (resp != null)
				{
					EntityUtils.consume(resp.getEntity()); // 会自动释放连接
					resp.close();
				}
				post.abort();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 以流的方式写一个数据 callHttpPostDataNoName
	 * 
	 * @param httpRequestUrl
	 * @param data
	 * @return String
	 */
	public String callHttpPostWithStream(String httpRequestUrl, String data)
	{

		HttpPost post = new HttpPost(httpRequestUrl);
		configHttpRequst(post);
		CloseableHttpResponse resp = null;
		try
		{
			StringEntity s = new StringEntity(data, "UTF-8");
			post.setEntity(s);
			resp = getInstance().getHttpClient().execute(post);
			int code = resp.getStatusLine().getStatusCode();
			if (code == 200)
			{
				HttpEntity entity = resp.getEntity();
				InputStream in = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				StringBuilder builder = new StringBuilder();
				String line = null;
				while ((line = br.readLine()) != null)
				{
					builder.append(line + "\n");
				}
				return builder.toString();
			}
		}
		catch (Exception ex)
		{
			log.error("timeout-error-Exception= " + httpRequestUrl);
		}
		finally
		{
			try
			{
				if (resp != null)
				{
					EntityUtils.consume(resp.getEntity()); // 会自动释放连接
					resp.close();
				}
				post.abort();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * post方式发送json数据 callHttpPostJsonData 中文有问题
	 * 
	 * @param httpRequestUrl
	 * @param json
	 * @return String
	 */

	public String callHttpPostJsonData(String httpRequestUrl, String json)
	{

		HttpPost post = new HttpPost(httpRequestUrl);
		configHttpRequst(post);
		CloseableHttpResponse resp = null;
		try
		{
			StringEntity s = new StringEntity(json, "UTF-8");
			s.setContentType("application/json");
			post.setEntity(s);
			resp = getInstance().getHttpClient().execute(post);
			int code = resp.getStatusLine().getStatusCode();
			if (code == 200)
			{
				HttpEntity entity = resp.getEntity();
				InputStream in = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				StringBuilder builder = new StringBuilder();
				String line = null;
				while ((line = br.readLine()) != null)
				{
					builder.append(line + "\n");
				}
				return builder.toString();
			}else{
				return "";
			}
		}
		catch (Exception ex)
		{
			log.error("timeout-error-Exception= " + httpRequestUrl);
		}
		finally
		{
			try
			{
				if (resp != null)
				{
					EntityUtils.consume(resp.getEntity()); // 会自动释放连接
					resp.close();
				}
				post.abort();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * post发送Map类型数据 callHttpPost
	 * 
	 * @param path
	 * @param content
	 * @return String
	 */
	public String callHttpPost(String path, Map content)
	{

		HttpPost post = new HttpPost(path);
		configHttpRequst(post);
		CloseableHttpResponse resp = null;
		try
		{
			List pairs = new ArrayList();
			if (null != content)
			{
				Iterator iter = content.keySet().iterator();
				while (iter.hasNext())
				{
					String key = (String) iter.next();
					Object val = content.get(key);
					pairs.add(new BasicNameValuePair(key, val.toString()));
				}
				post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
			}
			resp = getInstance().getHttpClient().execute(post);
			int code = resp.getStatusLine().getStatusCode();
			if (code == 200)
			{
				HttpEntity entity = resp.getEntity();
				InputStream in = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				StringBuilder builder = new StringBuilder();
				String line = null;
				while ((line = br.readLine()) != null)
				{
					builder.append(line + "\n");
				}
				return builder.toString();
			}
		}
		catch (Exception ex)
		{
			log.error("timeout-error-Exception= " + path);
		}
		finally
		{
			try
			{
				if (resp != null)
				{
					EntityUtils.consume(resp.getEntity()); // 会自动释放连接
					resp.close();
				}
				post.abort();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * post发送一个字符串，key固定为data callHttpPostStringData
	 * 
	 * @param path
	 * @param data
	 * @return String
	 */
	public String callHttpPostStringData(String path, String data)
	{

		HttpPost post = new HttpPost(path);
		configHttpRequst(post);
		CloseableHttpResponse resp = null;
		try
		{
			List pairs = new ArrayList();
			if (StringUtils.isNotEmpty(data))
			{
				pairs.add(new BasicNameValuePair("data", data.toString()));
				post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
			}
			resp = getInstance().getHttpClient().execute(post);
			int code = resp.getStatusLine().getStatusCode();
			if (code == 200)
			{
				HttpEntity entity = resp.getEntity();
				InputStream in = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				StringBuilder builder = new StringBuilder();
				String line = null;
				while ((line = br.readLine()) != null)
				{
					builder.append(line + "\n");
				}
				return builder.toString();
			}
		}
		catch (Exception ex)
		{
			log.error("timeout-error-Exception= " + path);
		}
		finally
		{
			try
			{
				if (resp != null)
				{
					EntityUtils.consume(resp.getEntity()); // 会自动释放连接
					resp.close();
				}
				post.abort();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 特殊情况下，发送一个请求，需要固定一些header doGetWithHeader
	 * 
	 * @param path
	 * @param header
	 * @return String
	 */
	public String doGetWithHeader(String path, Map<String, String> header,String json)
	{

		HttpGet get = new HttpGet(path);
		configHttpRequst(get);
		CloseableHttpResponse resp = null;
		try
		{
			if (header != null)
			{
				Iterator iter = header.keySet().iterator();
				while (iter.hasNext())
				{
					String key = (String) iter.next();
					String value = (String) header.get(key);
					get.setHeader(key, value);
				}
			}

			/*if(StringUtils.isNotEmpty(json)){
				StringEntity s = new StringEntity(json, "UTF-8");
				s.setContentType("application/json");
				post.setEntity(s);
			}*/
			resp = HttpUtils.getInstance().getHttpClient().execute(get);
			HttpEntity entity = resp.getEntity();
			InputStream in = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null)
			{
				builder.append(line + "\n");
			}
			return builder.toString();
		}
		catch (Exception e)
		{
			log.error("error-Exception= " + e.getMessage() + "---" + path, e);
		}
		finally
		{
			try
			{
				if (resp != null)
				{
					EntityUtils.consume(resp.getEntity()); // 会自动释放连接
					resp.close();
				}
				get.abort();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 特殊情况下，发送一个请求，需要固定一些header doGetWithHeader
	 * 
	 * @param path
	 * @param header
	 * @return String
	 */
	public String doPostWithHeader(String path, Map<String, String> header,String json)
	{

		HttpPost post = new HttpPost(path);
		configHttpRequst(post);
		CloseableHttpResponse resp = null;
		try
		{
			if (header != null)
			{
				Iterator iter = header.keySet().iterator();
				while (iter.hasNext())
				{
					String key = (String) iter.next();
					String value = (String) header.get(key);
					post.setHeader(key, value);
				}
			}

			if(StringUtils.isNotEmpty(json)){
				StringEntity s = new StringEntity(json, "UTF-8");
				s.setContentType("application/json");
				post.setEntity(s);
			}
			resp = HttpUtils.getInstance().getHttpClient().execute(post);
			HttpEntity entity = resp.getEntity();
			InputStream in = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null)
			{
				builder.append(line + "\n");
			}
			return builder.toString();
		}
		catch (Exception e)
		{
			log.error("error-Exception= " + e.getMessage() + "---" + path, e);
		}
		finally
		{
			try
			{
				if (resp != null)
				{
					EntityUtils.consume(resp.getEntity()); // 会自动释放连接
					resp.close();
				}
				post.abort();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * post key value数据类型 doPostByKeyVal
	 * 
	 * @param path
	 * @param pairs
	 * @return String
	 */
	public String doPostByKeyVal(String path, List<BasicNameValuePair> pairs)
	{

		HttpPost post = new HttpPost(path);
		configHttpRequst(post);
		CloseableHttpResponse resp = null;
		try
		{
			if (null != pairs && pairs.size() > 0)
				post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));

			resp = getInstance().getHttpClient().execute(post);
			int code = resp.getStatusLine().getStatusCode();
			if (code == 200)
			{
				HttpEntity entity = resp.getEntity();
				InputStream in = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				StringBuilder builder = new StringBuilder();
				String line = null;
				while ((line = br.readLine()) != null)
				{
					builder.append(line + "\n");
				}
				return builder.toString();
			}
		}
		catch (Exception ex)
		{
			log.error("timeout-error-Exception= " + path);
		}
		finally
		{
			try
			{
				if (resp != null)
				{
					EntityUtils.consume(resp.getEntity()); // 会自动释放连接
					resp.close();
				}
				post.abort();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public boolean saveToFileOnce(String url, String fileName)
	{

		if (StringUtils.isEmpty(url) || StringUtils.isEmpty(fileName))
		{
			return false;
		}
		if(url.indexOf("?")>-1){//对问号后面的内容编码一下
			
			String param = url.substring(url.indexOf("?")+1, url.length());  
			try
			{
				url = url.substring(0, url.indexOf("?")+1)+URLEncoder.encode(param,"utf-8");
			}
			catch (UnsupportedEncodingException e1)
			{
				log.error("error-Exception= " + e1.getMessage() + "---" + url, e1);
			}
		}
		byte[] buf = new byte[BUFFER_SIZE];
		BufferedInputStream bis = null;
		int size = 0;
		HttpGet get = new HttpGet(url);
		configHttpRequst(get);
		CloseableHttpResponse resp = null;
		FileOutputStream fos = null;
		try
		{
			resp = HttpUtils.getInstance().getHttpClient().execute(get);
			HttpEntity entity = resp.getEntity();
			InputStream in = entity.getContent();

			bis = new BufferedInputStream(in);
			// 建立文件
			File distFile = new File(fileName);
			if (!distFile.getParentFile().exists())
			{
				distFile.getParentFile().mkdirs();
			}
			fos = new FileOutputStream(distFile);

			// 保存文件
			while ((size = bis.read(buf)) != -1)
			{
				fos.write(buf, 0, size);
			}
			return true;
		}

		catch (Exception e)
		{
			log.error("error-Exception= " + e.getMessage() + "---" + url, e);
			return false;
		}
		finally
		{
			try
			{
				if (resp != null)
				{
					EntityUtils.consume(resp.getEntity()); // 会自动释放连接
					resp.close();
				}

				if (bis != null)
					bis.close();
				if (fos != null)
					fos.close();
				get.abort();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	
}
