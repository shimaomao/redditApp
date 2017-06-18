# redditApp
A Spring REST application that makes Async calls to Reddits API &amp; uses Jackson to parse the one JSON payload

END POINTS: root , /future , /singlePost

Root uses AsyncRestTemplate which is a bit quicker than /future which uses CompletableFutures. /singlePost just uses RestTemplate nothing special.
