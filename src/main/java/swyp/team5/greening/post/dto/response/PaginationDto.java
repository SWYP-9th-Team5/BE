package swyp.team5.greening.post.dto.response;

public record PaginationDto(
    int currentPage,
    int pageSize,
    int totalPages,
    long totalCounts
) {
}
