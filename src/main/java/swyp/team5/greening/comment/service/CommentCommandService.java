package swyp.team5.greening.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swyp.team5.greening.comment.domain.entity.Comment;
import swyp.team5.greening.comment.domain.repository.CommentRepository;
import swyp.team5.greening.comment.dto.request.SaveCommentRequestDto;
import swyp.team5.greening.comment.dto.response.SaveCommentResponseDto;
import swyp.team5.greening.comment.exception.PostExceptionMessage;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.domain.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class CommentCommandService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    //댓글 작성 로직
    @Transactional
    public SaveCommentResponseDto saveComment(
            Long userId,
            SaveCommentRequestDto requestDto
    ) {
        //게시물 존재 여부 확인
        if (!postRepository.existsById(requestDto.postId())) {
            throw new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST);
        }

        Comment saveComment = commentRepository.save(Comment.builder()
                .postId(requestDto.postId())
                .userId(userId)
                .comment(requestDto.comment())
                .build());

        return new SaveCommentResponseDto(saveComment.getId());
    }

}
