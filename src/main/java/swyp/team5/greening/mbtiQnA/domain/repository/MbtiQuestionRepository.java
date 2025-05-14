package swyp.team5.greening.mbtiQnA.domain.repository;

import java.util.List;
import swyp.team5.greening.mbtiQnA.domain.entity.MbtiQuestion;

public interface MbtiQuestionRepository {

    MbtiQuestion save(MbtiQuestion mbtiQuestion);

    List<MbtiQuestion> findAllByOrderBySequenceAsc();

    void deleteAll();

}
