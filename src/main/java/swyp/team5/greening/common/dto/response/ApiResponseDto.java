package swyp.team5.greening.common.dto.response;

public record ApiResponseDto<T>(
        T data
) {

    public static <T> ApiResponseDto<T> of(T data) {
        return new ApiResponseDto<>(data);
    }

}
