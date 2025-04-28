package swyp.team5.greening.mbtiQnA.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import swyp.team5.greening.common.base.BaseTimeEntity;

@Entity
@Table(name = "mbti_questions")
public class MbtiQuestion extends BaseTimeEntity {

    @Id
    @Column(name = "mbti_question_id")
    private Long id;

    @Column(name = "question_text")
    private String questionText;

    @Column(name = "sequence")
    private Integer sequence;

}
