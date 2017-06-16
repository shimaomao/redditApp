package com.cardechr.reddit.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Subreddit { //extends Children

  public String subreddit;
  public String title;
  public String author;
  public Date created;
  public int ups;
  public int downs;
  
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
  public Date getCreated() {
    return created;
  }
  public void setCreated(Date created) {
    this.created = created;
  }
  public int getUps() {
    return ups;
  }
  public void setUps(int ups) {
    this.ups = ups;
  }
  public int getDowns() {
    return downs;
  }
  public void setDowns(int downs) {
    this.downs = downs;
  }
  
  @Override
  public String toString(){
    return subreddit + title + author + created + ups + downs;
  }
}
