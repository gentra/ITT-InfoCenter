package com.alphabet7.ittinfocenter.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Log;
import android.util.Xml;

public class BaseFeedParser {

	static String feedUrlString = "http://alphabet7.blogspot.com/feeds/posts/default";

	// names of the XML tags
	static final String RSS = "rss";
	static final String CHANNEL = "channel";
	static final String ITEM = "item";

	static final String PUB_DATE = "pubDate";
	static final String DESCRIPTION = "description";
	static final String LINK = "link";
	static final String TITLE = "title";
	private final URL feedUrl;

	public BaseFeedParser(String urlFeed) {

		try {
			if (urlFeed.length() > 1)
				feedUrlString = urlFeed;
		} catch (Exception e) {
		}

		try {
			this.feedUrl = new URL(feedUrlString);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getInputStream() {
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Message> parse() {
		final Message currentMessage = new Message();
		RootElement root = new RootElement(RSS);
		final List<Message> messages = new ArrayList<Message>();
		Element itemlist = root.getChild(CHANNEL);
		Element item = itemlist.getChild(ITEM);

		item.setEndElementListener(new EndElementListener() {
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		item.getChild(TITLE).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						Log.i("BaseFeedParser", "TITLE:" + body);
						currentMessage.setTitle(body);
					}
				});
		item.getChild(LINK).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						Log.i("BaseFeedParser", "LINK" + body);
						currentMessage.setLink(body);
					}
				});
		item.getChild(DESCRIPTION).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						Log.i("BaseFeedParser", "DESCRIPTION" + body);
						currentMessage.setDescription(body);
					}
				});
		item.getChild(PUB_DATE).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						Log.i("BaseFeedParser", "PUB_DATE" + body);
						currentMessage.setDate(body);
					}
				});

		try {
			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8,
					root.getContentHandler());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return messages;
	}
}