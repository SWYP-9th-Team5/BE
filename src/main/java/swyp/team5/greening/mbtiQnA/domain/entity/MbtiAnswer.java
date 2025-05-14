package swyp.team5.greening.mbtiQnA.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.team5.greening.common.base.BaseTimeEntity;

@Entity
@Table(name = "mbti_answers")
@Getter
@NoArgsConstructor
public class MbtiAnswer extends BaseTimeEntity {

    @Id
    @Column(name = "mbti_answer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer")
    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(name = "mbti_answer_type")
    private MbtiAnswerType answerType;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "mbti_each")
    private MbtiEach mbtiEach;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mbti_question_id")
    private MbtiQuestion mbtiQuestion;

    @Builder
    public MbtiAnswer(
            String answer,
            MbtiAnswerType answerType,
            String imageUrl,
            MbtiEach mbtiEach,
            MbtiQuestion mbtiQuestion
    ) {
        this.answer = answer;
        this.answerType = answerType;
        this.imageUrl = imageUrl;
        this.mbtiEach = mbtiEach;
        this.mbtiQuestion = mbtiQuestion;
        mbtiQuestion.getAnswers().add(this);
    }
}
