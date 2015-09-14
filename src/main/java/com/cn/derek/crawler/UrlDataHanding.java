package com.cn.derek.crawler;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.cn.derek.crawler.downloader.DownloadPage;
import com.cn.derek.crawler.extractor.HrefOfPage;
import com.cn.derek.crawler.manager.UrlQueue;

public class UrlDataHanding implements Runnable
{
	/**
	 * 下载对应页面并分析出页面对应的URL放在未访问队列中。
	 * @param url
	 * @throws IOException 
	 */
	private static  Logger logger  =  Logger.getLogger( Test.class );
	public void dataHanding(String url) throws IOException
	{
		HrefOfPage.getHrefOfContent(DownloadPage.getContentFormUrl(url));
	}

	public void run()
	{
		while(!UrlQueue.isEmpty())
		{
			try {
				dataHanding(UrlQueue.outElem());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error( e.getMessage(), e );
			}
		}
	}
}