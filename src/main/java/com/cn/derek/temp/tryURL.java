package com.cn.derek.temp;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.Consts;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.sun.net.ssl.internal.www.protocol.https.Handler;
import com.sun.xml.internal.stream.Entity;


public class tryURL {
	public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException{
		CloseableHttpClient httpclient = HttpClients.createDefault() ;
		HttpGet httpget = new HttpGet("http://www.baidu.com?h1=百度") ;
		HttpPost httppost = new HttpPost("http://www.baidu.com") ;
		CloseableHttpResponse response = httpclient.execute( httpget) ; 
		HeaderElementIterator it = new BasicHeaderElementIterator(
				response.headerIterator("Set-Cookie"));
		while (it.hasNext()) {
			HeaderElement elem = it.nextElement();
			System.out.println(elem.getName() + " = " + elem.getValue());
			NameValuePair[] params = elem.getParameters();
			for (int i = 0; i < params.length; i++) {
				System.out.println(" " + params[i]);
			}
		}
		HttpEntity httpentity = response.getEntity() ;
		if( httpentity != null){
			InputStream input = (httpentity.getContent()) ;
		}
		System.out.println("Contents:") ;
		System.out.println(response.getEntity().getContent()) ;
		URI uri = new URIBuilder().build() ;
		StringEntity entity = new StringEntity("MyEntity", ContentType.create(""));
//		entity.
//		EntityUtils.toString(entity) ;
		List<NameValuePair> formparameters = new ArrayList<NameValuePair>() ;
		BasicResponseHandler Handler = new BasicResponseHandler();
		CloseableHttpClient httpClient2 = HttpClients.custom().build() ;
		CloseableHttpClient httpclient3 = HttpClients.custom().addInterceptorLast(new HttpRequestInterceptor() {
			public void process(
					final HttpRequest request,
					final HttpContext context) throws HttpException, IOException {
				AtomicInteger count = (AtomicInteger) context.getAttribute("count");
				request.addHeader("Count", Integer.toString(count.getAndIncrement()));
			}
		}).build();
		AtomicInteger count = new AtomicInteger(1);
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAttribute("count", count);
		httpget = new HttpGet("http://localhost/");
		for (int i = 0; i < 10; i++) {
			response = httpclient3.execute(httpget, localContext);
			try {
				HttpEntity entity2 = response.getEntity();
			} finally {
				response.close();
			}
		}
		response.getStatusLine() ;
//		ResponseHandler<T>
	}
}
