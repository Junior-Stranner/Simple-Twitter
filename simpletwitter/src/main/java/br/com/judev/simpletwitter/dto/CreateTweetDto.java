package br.com.judev.simpletwitter.dto;

import br.com.judev.simpletwitter.entities.Tweet;

public class CreateTweetDto {
    private String content;

    public CreateTweetDto() {
    }

    public CreateTweetDto(Tweet entity) {
        content = entity.getContent();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
