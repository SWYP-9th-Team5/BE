package swyp.team5.greening.mbti.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.team5.greening.mbti.domain.entity.Mbti;
import swyp.team5.greening.mbti.domain.repository.MbtiRepository;

public interface MbtiJpaRepository extends JpaRepository<Mbti , Long>, MbtiRepository {

}
