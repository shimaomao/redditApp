package com.cardechr.reddit.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cardechr.reddit.domain.Data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class ObjectNodeService {
	
	private ObjectMapper mapper;

	public String getJsonBody(String result) throws JsonProcessingException, IOException {
		mapper = new ObjectMapper();
		
		ObjectNode root = (ObjectNode) mapper.readTree(result);
		ObjectNode parentData = (ObjectNode) root.path("data");
		ArrayNode children = (ArrayNode) parentData.path("children");
		ObjectNode childArray = (ObjectNode) children.get(0);
		ObjectNode childData = (ObjectNode) childArray.path("data");
		
		// This element was causing me issues (╯°□°）╯︵ ┻━┻ DESTROY IT!!
		//childData.remove("selftext_html");
		
		Data data = mapper.readValue(childData.toString(), Data.class);
		
		// Pretty Print JSON
		String pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		return pretty;		
	}
	
	public String getJsonBody(List<String> result) throws JsonProcessingException, IOException {
		mapper = new ObjectMapper();
		String pretty = "";
		for(String s : result){
		ObjectNode root = (ObjectNode) mapper.readTree(s);
		ObjectNode data = (ObjectNode) root.path("data");
		ArrayNode children = (ArrayNode) data.path("children");
		ObjectNode childArray = (ObjectNode) children.get(0);
		ObjectNode data2 = (ObjectNode) childArray.path("data");
		
		// This element was causing me issues (╯°□°）╯︵ ┻━┻ DESTROY IT!!
		data2.remove("selftext_html");
		String response = data2.toString();
		
		Data data3 = mapper.readValue(response, Data.class);
		
		// Pretty Print JSON
		pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data3);
		}
		//ObjectNode ss = mapper.valueToTree(pretty);
		return pretty;
				
	}
}
