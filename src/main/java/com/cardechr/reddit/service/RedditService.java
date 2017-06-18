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

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class RedditService {

	private ObjectNodeService ons;
	private final RestTemplate restTemplate;

	private String[] urls = { "https://reddit.com/r/IAmA/top.json?limit=1",
			"https://reddit.com/r/programming/top.json?limit=1",
			"https://reddit.com/r/mildlyinteresting/top.json?limit=1",
			"https://reddit.com/r/AskReddit/top.json?limit=1", "https://reddit.com/r/tastyfood/top.json?limit=1" };

	public RedditService(RestTemplateBuilder restTemplateBuilder, ObjectNodeService ons) {
		this.restTemplate = restTemplateBuilder.build();
		this.ons = ons;
	}


	@Async
	public CompletableFuture<String> getSubreddit(String sub) throws JsonProcessingException, IOException {
		
		String url = String.format("https://reddit.com/r/%s/top.json?limit=1", sub);
		System.out.println(url);
		HttpHeaders headers = new HttpHeaders();
		headers.set("User-Agent", "other:com.cardechr.reddit:v1.0 (by /u/redprog12)");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		String r = results.getBody();
		String response = ons.getJsonBody(r.toString());
		return CompletableFuture.completedFuture(response);
	}

	public String getPost(String sub) throws IOException {
		
		String url = String.format("https://reddit.com/r/%s/top.json?limit=1", sub);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("User-Agent", "other:com.cardechr.reddit:v1.0 (by /u/redprog12)");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		
		String resultBody = result.getBody();
		String response = ons.getJsonBody(resultBody);

		return response;
	}

	// AsyncRestTemplate / ListenableFuture
	public List<String> getTopPosts() throws IOException, InterruptedException, ExecutionException {

		AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.set("User-Agent", "other:com.cardechr.reddit:v1.0 (by /u/redprog12)");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		List<ListenableFuture<ResponseEntity<String>>> futures = new ArrayList<>();
		for (String url : urls) {
			ListenableFuture<ResponseEntity<String>> responseEntityFuture = asyncRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);

			futures.add(responseEntityFuture);
		}

		//List<ResponseEntity<String>> response = new ArrayList<>();
		List<String> response = new ArrayList<>();
		for (ListenableFuture<ResponseEntity<String>> future : futures) {		
			String pretty = ons.getJsonBody(future.get().getBody());	
			response.add(pretty);
		}
		
		return response;
	}
}
