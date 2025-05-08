package swyp.team5.greening.common.dto.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record PaginationApiResponseDto<T>(
        List<T> data,
        PaginationResponseDto pagination
) {

    public static <T, P extends Page<T>> PaginationApiResponseDto<T> of(P page) {
        PaginationResponseDto paginationResponseDto = PaginationResponseDto.of(
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber() + 1,
                page.getSize()
        );

        return new PaginationApiResponseDto<>(page.getContent(), paginationResponseDto);
    }


}
