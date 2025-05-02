package swyp.team5.greening.user.dto.response;

public record LoginResponseDto(
        String accessToken,
        boolean newJoin
) {

}
