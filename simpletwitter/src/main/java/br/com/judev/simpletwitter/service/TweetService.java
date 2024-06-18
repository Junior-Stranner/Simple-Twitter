package br.com.judev.simpletwitter.service;

import br.com.judev.simpletwitter.dto.CreateTweetDto;
import br.com.judev.simpletwitter.dto.FeedDto;
import br.com.judev.simpletwitter.dto.FeedItemDto;
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
        var pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp");
        var tweetPage = tweetRepository.findAll(pageRequest);

        var feedItems = tweetPage.getContent().stream()
                .map(tweet -> new FeedItemDto(
                        tweet.getTweetId(),
                        tweet.getContent(),
                        tweet.getUser().getUsername()))
                .toList();

        return new FeedDto(feedItems, page, pageSize, tweetPage.getTotalPages(), tweetPage.getTotalElements());
    }

    public void createTweet(CreateTweetDto dto, String userId) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        var tweet = new Tweet();
        tweet.setUser(user);
        tweet.setContent(dto.getContent());

        var savedTweet = tweetRepository.save(tweet);
    }
    public void deleteTweet(Long tweetId, String userId) {
        // Encontrar o usuário que está fazendo a solicitação
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Encontrar o tweet que está sendo solicitado para exclusão
        var tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tweet not found"));

        // Verificar se o usuário é administrador
        boolean isAdmin = user.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ADMIN"));

        // Verificar se o usuário é o proprietário do tweet ou administrador
        if (isAdmin || tweet.getUser().getUserId().equals(UUID.fromString(userId))) {
            // Excluir o tweet pelo ID
            tweetRepository.deleteById(tweetId);
        } else {
            // Se o usuário não tiver permissão, lançar uma exceção de acesso negado
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this tweet");
        }
    }


}
