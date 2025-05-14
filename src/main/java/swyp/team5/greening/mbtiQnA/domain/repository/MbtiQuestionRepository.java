package swyp.team5.greening.mbtiQnA.domain.repository;

import java.util.List;
import swyp.team5.greening.mbtiQnA.domain.entity.MbtiQuestion;

public interface MbtiQuestionRepository {

    List<MbtiQuestion> findAllByOrderBySequenceAsc();

}
