package swyp.team5.greening.comment.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swyp.team5.greening.comment.domain.entity.Comment;
import swyp.team5.greening.comment.domain.repository.CommentQueryRepository;
import swyp.team5.greening.comment.dto.response.FindAllCommentResponseDto;

public interface CommentJpaQueryRepository extends JpaRepository<Comment, Long>,
        CommentQueryRepository {

    @Override
    @Query("""
          SELECT new swyp.team5.greening.comment.dto.response.FindAllCommentResponseDto(
              comment.id, user.id, user.userName, comment.comment, comment.createdAt,
              case when(user.id = :userId) then true else false end
          )
          FROM Comment comment
          INNER JOIN User user
          ON user.id = comment.userId
          INNER JOIN Post post
          ON post.id = comment.postId
          WHERE post.id = :postId
          ORDER BY comment.createdAt desc, comment.id asc
    """)
    Page<FindAllCommentResponseDto> findAllComment(
            @Param("userId") Long userId,
            @Param("postId") Long postId,
            Pageable pageable
    );
}
