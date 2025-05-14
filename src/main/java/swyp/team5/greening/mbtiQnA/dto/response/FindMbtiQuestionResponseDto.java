package swyp.team5.greening.mbtiQnA.dto.response;

import java.util.List;
import swyp.team5.greening.mbtiQnA.domain.entity.MbtiQuestion;

public record FindMbtiQuestionResponseDto(
        Long mbtiQuestionId,
        String questionText,
        Integer sequence,
        List<MbtiAnswerResponseDto> answers
) {

    public static FindMbtiQuestionResponseDto of(MbtiQuestion mbtiQuestion) {

        return new FindMbtiQuestionResponseDto(
                mbtiQuestion.getId(),
                mbtiQuestion.getQuestionText(),
                mbtiQuestion.getSequence(),
                mbtiQuestion.getAnswers().stream()
                        .map(MbtiAnswerResponseDto::of)
                        .toList()
        );
    }
}
