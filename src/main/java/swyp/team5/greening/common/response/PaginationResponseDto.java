package swyp.team5.greening.common.response;

public record PaginationResponseDto(

        Integer totalCounts,

        Integer totalPages,

        Integer currentPage,

        Integer pageSize

) {

    public static PaginationResponseDto of(
            Integer totalCounts,
            Integer totalPages,
            Integer currentPage,
            Integer pageSize
    ) {
        return new PaginationResponseDto(
                totalCounts, totalPages, currentPage, pageSize
        );
    }

}
