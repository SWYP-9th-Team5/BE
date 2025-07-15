package swyp.team5.greening.comment.service;

import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swyp.team5.greening.comment.domain.entity.Comment;
import swyp.team5.greening.comment.domain.repository.CommentRepository;
import swyp.team5.greening.comment.dto.request.DeleteCommentRequestDto;
import swyp.team5.greening.comment.dto.request.SaveCommentRequestDto;
import swyp.team5.greening.comment.dto.request.UpdateCommentRequestDto;
import swyp.team5.greening.comment.dto.response.SaveCommentResponseDto;
import swyp.team5.greening.comment.exception.CommentExceptionMessage;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.post.exception.PostExceptionMessage;

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
        //게시물 존재 조회
        Post post = postRepository
                .findByIdAndStateWithLock(requestDto.postId(), PostState.IN_PROGRESS)
                .orElseThrow(() ->
                        new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST));

        //댓글 저장
        Comment saveComment = commentRepository.save(Comment.builder()
                .postId(post.getId())
                .userId(userId)
                .comment(requestDto.comment())
                .build());

        //게시글 댓글 수 증가
        post.increaseCommentCount();

        return new SaveCommentResponseDto(saveComment.getId());
    }

    //댓글 수정 로직
    @Transactional
    public void updateComment(
            Long userId,
            UpdateCommentRequestDto requestDto
    ) {
        //댓글 조회
        Comment comment = commentRepository.findById(requestDto.commentId())
                .orElseThrow(() -> new GreeningGlobalException(
                        CommentExceptionMessage.NOT_FOUND_COMMENT));

        //댓글 수정 권한 확인
        if (!Objects.equals(comment.getUserId(), userId)) {
            throw new GreeningGlobalException(CommentExceptionMessage.BAD_REQUEST_COMMENT_WRITER);
        }

        comment.update(requestDto.comment());
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(
            Long userId,
            DeleteCommentRequestDto requestDto
    ) {
        //댓글 조회
        Comment comment = commentRepository.findById(requestDto.commentId())
                .orElseThrow(() -> new GreeningGlobalException(
                        CommentExceptionMessage.NOT_FOUND_COMMENT));

        //게시물 존재 조회
        Post post = postRepository.findByIdAndStateWithLock(comment.getPostId(), PostState.IN_PROGRESS)
                .orElseThrow(() ->
                        new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST));

        //댓글 수정 권한 확인
        if (!Objects.equals(comment.getUserId(), userId)) {
            throw new GreeningGlobalException(CommentExceptionMessage.BAD_REQUEST_COMMENT_WRITER);
        }

        //댓글 삭제
        commentRepository.deleteById(requestDto.commentId());

        //게시물 댓글 수 감소
        post.decreaseCommentCount();
    }

}
