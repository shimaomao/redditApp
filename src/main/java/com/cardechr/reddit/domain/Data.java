package com.cardechr.reddit.domain;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Data {

  private String subreddit;
  private String title;
  private String author;
  private int created;
  private int ups;
  private int downs;
  private String url;
  
  public String getSubreddit() {
	return subreddit;
}

public void setSubreddit(String subreddit) {
	this.subreddit = subreddit;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public String getAuthor() {
	return author;
}

public void setAuthor(String author) {
	this.author = author;
}

public String getCreated() {
	return formatCreated(created);
}

public void setCreated(int created) {
	this.created = created;
}

public String getUps() {
	return formatUpsDowns(ups);
}

public void setUps(int ups) {
	this.ups = ups;
}

public String getDowns() {
	return formatUpsDowns(downs);
}

public void setDowns(int downs) {
	this.downs = downs;
}

public String getUrl() {
	return url;
}

public void setUrl(String url) {
	this.url = url;
}

public String formatCreated(int created) {
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");
	sdf.setTimeZone(TimeZone.getTimeZone("MST"));
	Long epoch = created * 1000L;
	return sdf.format(epoch);
}

public String formatUpsDowns(int votes) {
	return NumberFormat.getNumberInstance(Locale.US).format(votes);
}
@Override
  public String toString(){
    return subreddit + title + author + created + ups + downs;
  }
}
