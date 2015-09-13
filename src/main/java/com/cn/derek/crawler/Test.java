package com.cn.derek.crawler;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.cn.derek.crawler.manager.UrlQueue;
public class Test
{
	private static  Logger logger  =  Logger.getLogger( Test.class );
	public static void main(String[] args) throws SQLException
	{
//		String url = "http://www.oschina.net/code/explore/achartengine/client/AndroidManifest.xml";
//		String url1 = "http://www.oschina.net/code/explore";
//		String url2 = "http://www.oschina.net/code/explore/achartengine";
//		String url3 = "http://www.oschina.net/code/explore/achartengine/client";
		System.out.println(System.getProperty("file.encoding"));
        String url = "http://news.baidu.com/" ;
        logger.info("start from url:"+url);
		UrlQueue.addElem(url);
//		UrlQueue.addElem(url1);
//		UrlQueue.addElem(url2);
//		UrlQueue.addElem(url3);

		UrlDataHanding[] url_Handings = new UrlDataHanding[10];

		for(int i = 0 ; i < 10 ; i++)
		{
			url_Handings[i] = new UrlDataHanding();
			logger.info("Thread "+i + " has started "+"开始 ");
			new Thread(url_Handings[i]).start();
		}

	}
}