package swyp.team5.greening.post.infrastructure;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.repository.PostQueryRepository;
import swyp.team5.greening.post.dto.PostUserNameProjection;

public interface PostJpaQueryRepository extends JpaRepository<Post, Long>, PostQueryRepository {

    // 홈 화면 오늘 작성된 게시글 중 카테고리별 좋아요 순 top 6
    @Query(value = """
            SELECT
                p.post_id AS postId,
                p.category_id AS categoryId,
                p.user_id AS userId,
                u.user_name AS userName,
                p.title AS title,
                pc.content AS content,
                p.like_count AS likeCount,
                p.comment_count AS commentCount,
                p.created_at AS createdAt,
                p.updated_at AS lastModifiedAt
            FROM posts p
            JOIN users u ON p.user_id = u.user_id
            LEFT JOIN (
                SELECT pc1.*
                FROM post_contents pc1
                WHERE pc1.sequence = (
                    SELECT MIN(pc2.sequence)
                    FROM post_contents pc2
                    WHERE pc2.post_id = pc1.post_id
                      AND pc2.type = 'TEXT'
                )
            ) pc ON pc.post_id = p.post_id
            WHERE p.category_id = :categoryId
              AND p.state = 'IN_PROGRESS'
            ORDER BY p.like_count DESC
            LIMIT 6
            """, nativeQuery = true)
    List<PostUserNameProjection> findTop6TodayByCategoryWithUserName(
            @Param("categoryId") Long categoryId
    );

    // 카테고리 별 조회
    @Query(value = """
            SELECT
                p.post_id AS postId,
                p.category_id AS categoryId,
                p.user_id AS userId,
                u.user_name AS userName,
                p.title AS title,
                NULL AS content, -- 페이지 조회에서는 content 제외
                p.like_count AS likeCount,
                p.comment_count AS commentCount,
                p.created_at AS createdAt,
                p.updated_at AS lastModifiedAt
            FROM posts p
            JOIN users u ON p.user_id = u.user_id
            WHERE p.category_id = :categoryId
              AND p.state = :state
            ORDER BY p.created_at DESC
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM posts p
                    WHERE p.category_id = :categoryId
                      AND p.state = :state
                    """,
            nativeQuery = true)
    Page<PostUserNameProjection> findAllByCategoryWithUserName(
            @Param("categoryId") Long categoryId,
            @Param("state") String state,
            Pageable pageable
    );

}
