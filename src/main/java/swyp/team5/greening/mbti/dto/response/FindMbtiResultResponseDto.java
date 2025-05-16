package swyp.team5.greening.mbti.dto.response;

public record FindMbtiResultResponseDto(
        Long mbtiId,
        String plantName,
        String plantDescription,
        String plantPersonality,
        String suitablePlant,
        String unsuitablePlant,
        String recommendedPlant,
        String plantImageUrl
) {

}
