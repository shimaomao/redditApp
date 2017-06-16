package com.cardechr.reddit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.cardechr.reddit.domain.Subreddit;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class RedditService {

  private final RestTemplate restTemplate;
  
  private String[] urls = { "https://reddit.com/r/IAmA/top.json?limit=1",
    "https://reddit.com/r/programming/top.json?limit=1", "https://reddit.com/r/mildlyinteresting/top.json?limit=1",
    "https://reddit.com/r/AskReddit/top.json?limit=1", "https://reddit.com/r/tastyfood/top.json?limit=1" };
  
  public RedditService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }
  
  // AsyncRestTemplate / ListenableFuture
  
  @Async
  public CompletableFuture<Subreddit> getSubreddit(String sub) {
    
    String url = String.format("https://reddit.com/r/%s/top.json?limit=1", sub);
    System.out.println(url);
    HttpHeaders headers = new HttpHeaders();
    headers.set("User-Agent", "other:com.cardechr.reddit:v1.0 (by /u/redprog12)");
    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

    ResponseEntity<Subreddit> results = restTemplate.exchange(url, HttpMethod.GET, entity, Subreddit.class);
    return CompletableFuture.completedFuture(results.getBody());
  }
  
  public ResponseEntity<String> getPost(String sub) {
    String url = String.format("https://reddit.com/r/%s/top.json?limit=1", sub);
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.set("User-Agent", "other:com.cardechr.reddit:v1.0 (by /u/redprog12)");
    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
    ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    System.out.println(result);
    return result;
  }
  
  public List<String> getTopPosts() throws JsonProcessingException, InterruptedException, ExecutionException {

    AsyncRestTemplate restTemplate = new AsyncRestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.set("User-Agent", "other:com.cardechr.reddit:v1.0 (by /u/redprog12)");
    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
    
    List<ListenableFuture<ResponseEntity<String>>> futures = new ArrayList<>();
    for (String url : urls) {
      ListenableFuture<ResponseEntity<String>> responseEntityFuture = restTemplate.exchange(url, HttpMethod.GET,
        entity, String.class);
      
      futures.add(responseEntityFuture);
      }

    //List<ResponseEntity<String>> response = new ArrayList<>();
    List<String> body = new ArrayList<>();
    for (ListenableFuture<ResponseEntity<String>> future : futures) { 
      body.add(future.get().getBody());
    } 
    return body;
  }
}
