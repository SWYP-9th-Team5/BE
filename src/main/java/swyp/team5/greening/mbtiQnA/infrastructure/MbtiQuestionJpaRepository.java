package swyp.team5.greening.mbtiQnA.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.team5.greening.mbtiQnA.domain.entity.MbtiQuestion;
import swyp.team5.greening.mbtiQnA.domain.repository.MbtiQuestionRepository;

public interface MbtiQuestionJpaRepository extends JpaRepository<MbtiQuestion, Long>,
        MbtiQuestionRepository {

}
