package com.cn.derek.crawler.extractor;

import org.apache.log4j.Logger;

import com.cn.derek.crawler.downloader.DownloadPage;
import com.cn.derek.crawler.manager.UrlQueue;
import com.cn.derek.crawler.manager.VisitedUrlQueue;
import com.cn.derek.crawler.utils.UrlUtils;

public class HrefOfPage
{
	/**
	 * 获得页面源代码中超链接
	 */
    private static Logger logger = Logger.getLogger(HrefOfPage.class.getClass()) ;

	public static void getHrefOfContent(String content)
	{
		logger.info("start resolving content:");
		if( content == null)
			return;
		String[] contents = content.split("<a href=\"");
		for (int i = 1; i < contents.length; i++)
		{
			int endHref = contents[i].indexOf("\"");

			String href = UrlUtils.getHrefOfInOut(contents[i].substring(
					0, endHref)).trim();

			if (href != null)
			{
//				String href = UrlUtils.getHrefOfInOut(aHref);
                logger.info("extract url:"+ href);
				if (!UrlQueue.isContains(href) && UrlUtils.isValidUrl(href) && !VisitedUrlQueue.isContains(href))
				{
					logger.info("save url:" + href);
					UrlQueue.addElem(href);
				}
			}
		}

		logger.info(UrlQueue.size() + "--抓取到的连接数");
		logger.info(VisitedUrlQueue.size() + "--已处理的页面数");

	}

}