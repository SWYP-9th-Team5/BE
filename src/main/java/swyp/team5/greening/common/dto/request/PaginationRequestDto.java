package swyp.team5.greening.common.dto.request;

import jakarta.validation.constraints.NotNull;

public record PaginationRequestDto(
        @NotNull
        Integer pageNumber,

        @NotNull
        Integer pageSize
) {

    public Integer pageNumber() {
        return pageNumber - 1;
    }

    public Integer pageSize() {
        return pageSize;
    }

    public int pageOffset() {
        return pageNumber() * pageSize();
    }
}
