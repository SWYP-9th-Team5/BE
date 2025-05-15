package swyp.team5.greening.post.domain.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import swyp.team5.greening.post.dto.PostUserNameProjection;

public interface PostQueryRepository {

    List<PostUserNameProjection> findTop6TodayByCategoryWithUserName(
            Long categoryId
    );

    Page<PostUserNameProjection> findAllByCategoryWithUserName(
            Long categoryId,
            String state,
            Pageable pageable
    );

}
