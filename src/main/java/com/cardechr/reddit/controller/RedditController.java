package com.cardechr.reddit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cardechr.reddit.domain.Subreddit;
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

  @RequestMapping(value ="/", method = RequestMethod.GET ,produces = "application/json; charset=UTF-8")
  public List<Subreddit> getSubreddit() throws InterruptedException, ExecutionException {
    
    List<Subreddit> list = new ArrayList<>();
    CompletableFuture<Subreddit> r1 = redditService.getSubreddit(SUBREDDITS[0]);
    CompletableFuture<Subreddit> r2 = redditService.getSubreddit(SUBREDDITS[1]);
    CompletableFuture<Subreddit> r3 = redditService.getSubreddit(SUBREDDITS[2]);
    CompletableFuture<Subreddit> r4 = redditService.getSubreddit(SUBREDDITS[3]);
    CompletableFuture<Subreddit> r5 = redditService.getSubreddit(SUBREDDITS[4]);
    CompletableFuture.allOf(r1,r2,r3,r4,r5).join();

    list.add(r1.get());
    list.add(r2.get());
    list.add(r3.get());
    list.add(r4.get());
    list.add(r5.get());

    return list;
  }
  
  @RequestMapping(value = "/top", method = RequestMethod.GET, headers = "Accept=application/json")
  public List<String> getPost() throws JsonProcessingException, InterruptedException, ExecutionException {
    return redditService.getTopPosts();
  }
  
  @RequestMapping(value = "/singlePost", method = RequestMethod.GET, headers = "Accept=application/json")
  public ResponseEntity<String> getAPost() {
    return redditService.getPost(SUBREDDITS[1]);
  }
}
