package swyp.team5.greening.postLike.domain.repository;

import swyp.team5.greening.postLike.domain.entity.Like;

public interface LikeRepository {

    Like save(Like like);

    void deleteAll();

}
