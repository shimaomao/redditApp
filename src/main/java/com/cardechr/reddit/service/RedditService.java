package com.cardechr.reddit.service;

import java.io.IOException;
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

import com.cardechr.reddit.domain.Data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class RedditService {

	private final RestTemplate restTemplate;
  private ObjectMapper mapper;

	private String[] urls = { "https://reddit.com/r/IAmA/top.json?limit=1",
			"https://reddit.com/r/programming/top.json?limit=1",
			"https://reddit.com/r/mildlyinteresting/top.json?limit=1",
			"https://reddit.com/r/AskReddit/top.json?limit=1", "https://reddit.com/r/tastyfood/top.json?limit=1" };

	public RedditService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Async
	public CompletableFuture<String> getSubreddit(String sub) throws JsonProcessingException, IOException {
		
		String url = String.format("https://reddit.com/r/%s/top.json?limit=1", sub);		
		HttpEntity<String> entity = setUserAgent();
		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		return CompletableFuture.completedFuture(getJsonBody(results.getBody().toString()));
	}

	public String getPost(String sub) throws IOException {
	  
		String url = String.format("https://reddit.com/r/%s/top.json?limit=1", sub);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = setUserAgent();
		ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		
		return getJsonBody(result.getBody());
	}

	// AsyncRestTemplate / ListenableFuture
	public List<String> getTopPosts() throws IOException, InterruptedException, ExecutionException {

		AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
		HttpEntity<String> entity = setUserAgent();

		List<ListenableFuture<ResponseEntity<String>>> futures = new ArrayList<>();
		for (String url : urls) {
			ListenableFuture<ResponseEntity<String>> responseEntityFuture = asyncRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			futures.add(responseEntityFuture);
		}

		List<String> response = new ArrayList<>();
		for (ListenableFuture<ResponseEntity<String>> future : futures) {
		  response.add(getJsonBody(future.get().getBody()));
		}
		return response;
	}
	
  // Reddit API recommends JAVA Apps to have custom User-Agent else it responds with 429 (Too Many Requests)
	private HttpEntity<String> setUserAgent(){
	  HttpHeaders headers = new HttpHeaders();
    headers.set("User-Agent", "other:com.cardechr.reddit:v1.0 (by /u/redprog12)");
    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
	  return entity;
	}
	
	// Parses requset using Jackson to get elements in Data Class
	private String getJsonBody(String result) throws JsonProcessingException, IOException {
	  
    mapper = new ObjectMapper();
    ObjectNode root = (ObjectNode) mapper.readTree(result);
    ObjectNode parentData = (ObjectNode) root.path("data");
    ArrayNode children = (ArrayNode) parentData.path("children");
    ObjectNode childArray = (ObjectNode) children.get(0);
    ObjectNode childData = (ObjectNode) childArray.path("data");

    Data data = mapper.readValue(childData.toString(), Data.class);

    // Pretty Print JSON
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
  }
}
