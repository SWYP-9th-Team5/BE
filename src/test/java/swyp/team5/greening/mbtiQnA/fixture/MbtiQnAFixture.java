package swyp.team5.greening.mbtiQnA.fixture;

import swyp.team5.greening.mbtiQnA.domain.entity.MbtiAnswer;
import swyp.team5.greening.mbtiQnA.domain.entity.MbtiAnswerType;
import swyp.team5.greening.mbtiQnA.domain.entity.MbtiEach;
import swyp.team5.greening.mbtiQnA.domain.entity.MbtiQuestion;

public final class MbtiQnAFixture {

    private MbtiQnAFixture() {
    }

    public static MbtiQuestion getMbtiQuestion(String question, Integer sequence) {
        return MbtiQuestion.builder()
                .questionText(question)
                .sequence(sequence)
                .build();
    }

    public static MbtiAnswer getMbtiAnswer(
            String answer,
            MbtiAnswerType answerType,
            MbtiEach mbtiEach,
            MbtiQuestion mbtiQuestion
    ) {
        return MbtiAnswer.builder()
                .answer(answer)
                .answerType(answerType)
                .mbtiEach(mbtiEach)
                .imageUrl("testImageUrl")
                .mbtiQuestion(mbtiQuestion)
                .build();
    }

}
