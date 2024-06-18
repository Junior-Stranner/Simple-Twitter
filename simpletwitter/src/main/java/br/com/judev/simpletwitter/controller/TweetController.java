package br.com.judev.simpletwitter.controller;

import br.com.judev.simpletwitter.dto.CreateTweetDto;
import br.com.judev.simpletwitter.dto.FeedDto;
import br.com.judev.simpletwitter.repository.TweetRepository;
import br.com.judev.simpletwitter.repository.UserRepository;
import br.com.judev.simpletwitter.service.TweetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class TweetController {

    private final TweetService tweetService;
    private final UserRepository userRepository;

    public TweetController(TweetService tweetService, UserRepository userRepository) {
        this.tweetService = tweetService;
        this.userRepository = userRepository;
    }

    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        FeedDto feed = tweetService.getFeed(page , pageSize);
        return ResponseEntity.ok(feed);
    }
    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto dto,
                                            JwtAuthenticationToken token) {
      tweetService.createTweet(dto , token.getName());
      return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/tweets/{id")
    public ResponseEntity<Void> deleteTweet(@PathVariable ("id") long tweetId, JwtAuthenticationToken token){
        tweetService.deleteTweet(tweetId, token.getName());
        return ResponseEntity.ok().build();
    }
}
