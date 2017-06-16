package com.cardechr.reddit.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Data {

  public List<String> data;
  
  public List<String> getData(){
    return data;
  }
}
