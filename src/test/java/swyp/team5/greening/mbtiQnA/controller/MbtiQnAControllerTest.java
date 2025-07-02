package swyp.team5.greening.mbtiQnA.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import swyp.team5.greening.mbtiQnA.domain.entity.MbtiAnswer;
import swyp.team5.greening.mbtiQnA.domain.entity.MbtiAnswerType;
import swyp.team5.greening.mbtiQnA.domain.entity.MbtiEach;
import swyp.team5.greening.mbtiQnA.domain.entity.MbtiQuestion;
import swyp.team5.greening.mbtiQnA.domain.repository.MbtiQuestionRepository;
import swyp.team5.greening.mbtiQnA.fixture.MbtiQnAFixture;
import swyp.team5.greening.support.ApiTestSupport;

@DisplayName("Mbti QnA 통합 테스트")
class MbtiQnAControllerTest extends ApiTestSupport {

    @Autowired
    private MbtiQuestionRepository questionRepository;

    @BeforeEach
    void init() {
        questionRepository.deleteAll();
    }

    @Nested
    @DisplayName("Mbti 질문이 2개 존재하며, 그에 대한 답변이 각각 2개씩 존재한다.")
    class TestCase1 {

        MbtiQuestion mbtiQuestion1;
        String question1 = "1번 질문";
        MbtiQuestion mbtiQuestion2;
        String question2 = "2번 질문";

        MbtiAnswer mbtiAnswer1;
        String answer1 = "1번 답변 1";
        MbtiAnswer mbtiAnswer2;
        String answer2 = "1번 답변 2";
        MbtiAnswer mbtiAnswer3;
        String answer3 = "2번 답변 1";
        MbtiAnswer mbtiAnswer4;
        String answer4 = "2번 답변 2";

        @BeforeEach
        void setUp() {
            mbtiQuestion1 = MbtiQnAFixture.getMbtiQuestion(question1, 1);
            mbtiQuestion2 = MbtiQnAFixture.getMbtiQuestion(question2, 2);

            mbtiAnswer1 = MbtiQnAFixture.getMbtiAnswer(answer1, MbtiAnswerType.A, MbtiEach.E,
                    mbtiQuestion1);
            mbtiAnswer2 = MbtiQnAFixture.getMbtiAnswer(answer2, MbtiAnswerType.B, MbtiEach.I,
                    mbtiQuestion1);
            mbtiAnswer3 = MbtiQnAFixture.getMbtiAnswer(answer3, MbtiAnswerType.A, MbtiEach.S,
                    mbtiQuestion2);
            mbtiAnswer4 = MbtiQnAFixture.getMbtiAnswer(answer4, MbtiAnswerType.B, MbtiEach.T,
                    mbtiQuestion2);

            questionRepository.save(mbtiQuestion1);
            questionRepository.save(mbtiQuestion2);
        }

        @Test
        @DisplayName("Mbti 관련 문제 및 답변 목록을 알맞게 출력한다.")
        void getMbtiQnATest() throws Exception {
            //when
            ResultActions perform = mockMvc.perform(get("/api/mbti-questions"));

            //then
            perform.andExpectAll(
                    jsonPath("$.data.size()").value(2),
                    jsonPath("$.data[0].mbtiQuestionId").value(mbtiQuestion1.getId()),
                    jsonPath("$.data[0].questionText").value(mbtiQuestion1.getQuestionText()),
                    jsonPath("$.data[0].sequence").value(1),
                    jsonPath("$.data[0].answers.size()").value(2),

                    jsonPath("$.data[0].answers[0].mbtiAnswerId").value(mbtiAnswer1.getId()),
                    jsonPath("$.data[0].answers[0].answer").value(answer1),
                    jsonPath("$.data[0].answers[0].answerType").value(
                            mbtiAnswer1.getAnswerType().name()),
                    jsonPath("$.data[0].answers[0].mbtiType").value(
                            mbtiAnswer1.getMbtiEach().name()),

                    jsonPath("$.data[0].answers[1].mbtiAnswerId").value(mbtiAnswer2.getId()),
                    jsonPath("$.data[0].answers[1].answer").value(answer2),
                    jsonPath("$.data[0].answers[1].answerType").value(
                            mbtiAnswer2.getAnswerType().name()),
                    jsonPath("$.data[0].answers[1].mbtiType").value(
                            mbtiAnswer2.getMbtiEach().name())
            );
        }

    }
}
