package swyp.team5.greening.mbti.domain.repository;

import java.util.Optional;
import swyp.team5.greening.mbti.domain.entity.Mbti;
import swyp.team5.greening.mbti.domain.entity.MbtiType;

public interface MbtiRepository {

    Optional<Mbti> findByMbtiType(MbtiType mbtiType);

}
