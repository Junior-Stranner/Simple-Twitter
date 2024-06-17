package br.com.judev.simpletwitter.dto;

import java.util.List;

public class FeedItemDto {
    private List<FeedItemDto> feedItems;
    private int page;
    private int pageSize;
    private int totalPages;
    private long totalElements;

    // Construtor vazio (default)
    public FeedItemDto() {
    }

    // Construtor com todos os campos
    public FeedItemDto(List<FeedItemDto> feedItems, int page, int pageSize, int totalPages, long totalElements) {
        this.feedItems = feedItems;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    // Getters e Setters
    public List<FeedItemDto> getFeedItems() {
        return feedItems;
    }

    public void setFeedItems(List<FeedItemDto> feedItems) {
        this.feedItems = feedItems;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
