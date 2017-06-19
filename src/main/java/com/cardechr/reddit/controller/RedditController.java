package com.cardechr.reddit.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cardechr.reddit.service.RedditService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class RedditController {
  
  private RedditService redditService;
  private final String[] SUBREDDITS = {"Programming","IAmA","AskReddit","DIY","mildlyinteresting"};
  
  @Autowired
  public RedditController(RedditService redditService){
    this.redditService = redditService;
  }

  @RequestMapping(value ="/future", method = RequestMethod.GET ,produces = "application/json; charset=UTF-8")
  public List<String> getSubreddits() throws InterruptedException, ExecutionException, JsonProcessingException, IOException {
    
    List<String> list = new ArrayList<>();
    CompletableFuture<String> r1 = redditService.getSubreddit(SUBREDDITS[0]);
    CompletableFuture<String> r2 = redditService.getSubreddit(SUBREDDITS[1]);
    CompletableFuture<String> r3 = redditService.getSubreddit(SUBREDDITS[2]);
    CompletableFuture<String> r4 = redditService.getSubreddit(SUBREDDITS[3]);
    CompletableFuture<String> r5 = redditService.getSubreddit(SUBREDDITS[4]);
    CompletableFuture.allOf(r1,r2,r3,r4,r5).join();

    list.add(r1.get());
    list.add(r2.get());
    list.add(r3.get());
    list.add(r4.get());
    list.add(r5.get());	

    return list;
  }
  
  @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json")
  public List<String> getSubredditsAsync() throws InterruptedException, ExecutionException, IOException {
    return redditService.getTopPosts();
  }
  
  @RequestMapping(value = "/singlePost", method = RequestMethod.GET, headers = "Accept=application/json")
  public String getSubreddit() throws IOException {
    return redditService.getPost(SUBREDDITS[1]);
  }
}
