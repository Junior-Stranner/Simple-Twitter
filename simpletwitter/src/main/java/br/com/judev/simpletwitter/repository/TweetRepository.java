package br.com.judev.simpletwitter.repository;

import br.com.judev.simpletwitter.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
