package swyp.team5.greening.mbtiQnA.dto.response;

import swyp.team5.greening.mbtiQnA.domain.entity.MbtiAnswer;

public record MbtiAnswerResponseDto(
        Long mbtiAnswerId,
        String answer,
        String answerType,
        String imageUrl,
        String mbtiType
) {

    public static MbtiAnswerResponseDto of(MbtiAnswer mbtiAnswer) {
        return new MbtiAnswerResponseDto(
                mbtiAnswer.getId(),
                mbtiAnswer.getAnswer(),
                mbtiAnswer.getAnswerType().name(),
                mbtiAnswer.getImageUrl(),
                mbtiAnswer.getMbtiEach().name()
        );
    }
}
