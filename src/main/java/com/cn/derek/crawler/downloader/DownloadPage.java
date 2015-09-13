package com.cn.derek.crawler.downloader;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.omg.CORBA.PUBLIC_MEMBER;

import com.cn.derek.crawler.manager.VisitedUrlQueue;
import com.cn.derek.crawler.utils.UrlUtils;
import com.sun.corba.se.impl.presentation.rmi.StubFactoryFactoryStaticImpl;

public class DownloadPage
{
    private static Logger logger = Logger.getLogger(DownloadPage.class.getClass()) ;
	//	private static ￼￼￼￼ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy(){
	//		public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
	//			// Honor 'keep-alive' header
	//			HeaderElementIterator it = new BasicHeaderElementIterator(
	//					response.headerIterator(HTTP.CONN_KEEP_ALIVE));
	//			while (it.hasNext()) {
	//				HeaderElement he = it.nextElement();
	//				String param = he.getName();
	//				String value = he.getValue();
	//				if (value != null && param.equalsIgnoreCase("timeout")) {
	//					try {
	//						return Long.parseLong(value) * 1000;
	//					} catch(NumberFormatException ignore) {
	//					} }
	//			}
	//			HttpHost target = (HttpHost) context.getAttribute(
	//					HttpClientContext.HTTP_TARGET_HOST);
	//			if ("www.naughty-server.com".equalsIgnoreCase(target.getHostName())) {
	//				// Keep alive for 5 seconds only
	//				return 5 * 1000;
	//			} else {
	//				// otherwise keep alive for 30 seconds
	//				return 30 * 1000;
	//			}
	//		}
	//	};
	private static HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
		public boolean retryRequest(
				IOException exception,
				int executionCount,
				HttpContext context) {
			if (executionCount >= 5) {
				// Do not retry if over max retry count
				return false;
			}
			if (exception instanceof InterruptedIOException) {
				// Timeout
				return false;
			}
			if (exception instanceof UnknownHostException) {
				// Unknown host
				return false;
			}
			if (exception instanceof ConnectTimeoutException) {
				// Connection refused
				return false;
			}
			if(exception instanceof SocketTimeoutException){
				// Socket Time out
				return false ;
			}
			if (exception instanceof SSLException) {
				// SSL handshake exception
				return false;
			}
			HttpClientContext clientContext = HttpClientContext.adapt(context);
			HttpRequest request = clientContext.getRequest();
			boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
			if (idempotent) {
				// Retry if the request is considered idempotent
				return true;
			}
			return false;
		}
	};
	private static HttpResponseInterceptor httpResponseInterceptor = new HttpResponseInterceptor() {
		public void process(final HttpResponse response, final HttpContext context) throws HttpException,  
		IOException {  
			HttpEntity entity = response.getEntity();  
			Header ceheader = entity.getContentEncoding();  
			if (ceheader != null) {  
				for (HeaderElement element : ceheader.getElements()) {  
					if (element.getName().equalsIgnoreCase("gzip")) {  
						response.setEntity(new GzipDecompressingEntity(response.getEntity()));  
						return;  
					}
				}
			}
		}
	};
	/**
      * 根据URL抓取网页内容
      * 
      * @param url
      * @return
    
	 * @throws IOException */
	public static String getContentFormUrl(String url) throws IOException {
		/* 实例化一个HttpClient客户端 */
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).build();

		CloseableHttpClient client = HttpClients.custom()
				.addInterceptorLast(httpResponseInterceptor)
				.setRetryHandler(retryHandler)
				.build() ;
		HttpGet getHttp = new HttpGet(url);
		getHttp.setConfig(requestConfig);
		String content = null;

		CloseableHttpResponse response=null;
		try{
			/*获得信息载体*/
			response = client.execute(getHttp);
			HttpEntity entity = response.getEntity();
			VisitedUrlQueue.addElem(url);
			if (entity != null)
			{
				/* 转化为文本信息 */
				content = EntityUtils.toString(entity);

				/* 判断是否符合下载网页源代码到本地的条件 */
				if (UrlUtils.isCreateFile(url)
						&& UrlUtils.isHasGoalContent(content) != -1)
				{
					UrlUtils.createFile(UrlUtils
							.getGoalContent(content), url);
				}
			}

		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
			logger.error( e.getMessage() );
		} catch (IOException e)
		{
			e.printStackTrace();
			logger.error( e.getMessage() );			
		} finally
		{
			//client.getConnectionManager().shutdown();
			if(response != null )
				response.close();
			if( client != null)
				client.close();

		}
		return content;
	}

}