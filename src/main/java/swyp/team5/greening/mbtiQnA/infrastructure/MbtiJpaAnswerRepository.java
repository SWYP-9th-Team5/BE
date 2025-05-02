package swyp.team5.greening.mbtiQnA.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.team5.greening.mbtiQnA.domain.entity.MbtiAnswer;
import swyp.team5.greening.mbtiQnA.domain.repository.MbtiAnswerRepository;

public interface MbtiJpaAnswerRepository extends JpaRepository<MbtiAnswer, Long>,
        MbtiAnswerRepository {

}
