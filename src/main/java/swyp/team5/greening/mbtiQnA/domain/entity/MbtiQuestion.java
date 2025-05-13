package swyp.team5.greening.mbtiQnA.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import swyp.team5.greening.common.base.BaseTimeEntity;

@Entity
@Table(name = "mbti_questions")
public class MbtiQuestion extends BaseTimeEntity {

    @Id
    @Column(name = "mbti_question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text")
    private String questionText;

    @Column(name = "sequence")
    private Integer sequence;

}
