package swyp.team5.greening.user.dto.request;

public record LogInRequestDto(
        String redirectURI,
        String code
) {

}
