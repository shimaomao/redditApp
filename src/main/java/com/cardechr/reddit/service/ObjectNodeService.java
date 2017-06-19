package com.cardechr.reddit.service;

import java.io.IOException;

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

		Data data = mapper.readValue(childData.toString(), Data.class);

		// Pretty Print JSON
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
	}
}
