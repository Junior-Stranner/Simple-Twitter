package br.com.judev.simpletwitter.dto;

public class FeedItemDto {
    private Long tweetId;
    private String content;
    private String username;



    public FeedItemDto(Long tweetId, String content, String username) {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }
}
