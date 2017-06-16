package com.cardechr.reddit.service;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Children extends Data{

  public List<String> children;
  
  public List<String> getChildren(){
    return children;
  }
}
