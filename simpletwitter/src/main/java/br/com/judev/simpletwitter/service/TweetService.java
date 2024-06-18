package br.com.judev.simpletwitter.service;

import br.com.judev.simpletwitter.dto.CreateTweetDto;
import br.com.judev.simpletwitter.dto.FeedDto;
import br.com.judev.simpletwitter.entities.Tweet;
import br.com.judev.simpletwitter.repository.TweetRepository;
import br.com.judev.simpletwitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Autowired
    public TweetService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public FeedDto getFeed(int page, int pageSize) {
        var tweets = tweetRepository.findAll(
                        PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
                .map(tweet ->
                        new FeedDto(
                                tweet.getTweetId(),
                                tweet.getContent(),
                                tweet.getUser().getUsername())
                );

        return new FeedDto(
                tweets.getContent(), page, pageSize, tweets.getTotalPages(), tweets.getTotalElements());
    }

    public void createTweet(CreateTweetDto dto, String userId) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        var tweet = new Tweet();
        tweet.setUser(user);
        tweet.setContent(dto.getContent());

        tweetRepository.save(tweet);
    }

    public void deleteTweet(Long tweetId, String userId) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        var tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tweet not found"));

        var isAdmin = user.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ADMIN"));

        if (isAdmin || tweet.getUser().getUserId().equals(UUID.fromString(userId))) {
            tweetRepository.deleteById(tweetId);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this tweet");
        }
    }
}
