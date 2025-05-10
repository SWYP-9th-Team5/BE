package swyp.team5.greening.comment.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import swyp.team5.greening.comment.domain.entity.Comment;
import swyp.team5.greening.comment.domain.repository.CommentRepository;
import swyp.team5.greening.comment.dto.request.DeleteCommentRequestDto;
import swyp.team5.greening.comment.dto.request.SaveCommentRequestDto;
import swyp.team5.greening.comment.dto.request.UpdateCommentRequestDto;
import swyp.team5.greening.comment.dto.response.SaveCommentResponseDto;
import swyp.team5.greening.comment.exception.CommentExceptionMessage;
import swyp.team5.greening.post.exception.PostExceptionMessage;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.domain.repository.PostRepository;

@DisplayName("댓글 단위 테스트")
@ExtendWith(MockitoExtension.class)
class CommentCommandServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentCommandService commentCommandService;

    @DisplayName("댓글 생성 테스트")
    @Nested
    class saveCommentTest {

        Long userId = 1L;
        Long postId = 2L;
        String comment = "댓글입니다~";

        Comment commentEntity = Comment.builder()
                .postId(postId)
                .userId(userId)
                .comment(comment)
                .build();

        @Test
        @DisplayName("사용자는 게시물에 댓글을 작성할 수 있다.")
        void testCase1() {
            //given
            given(postRepository.existsById(postId)).willReturn(true);
            given(commentRepository.save(any(Comment.class))).willReturn(commentEntity);
            ReflectionTestUtils.setField(commentEntity, "id", 3L);

            //when
            SaveCommentResponseDto responseDto = commentCommandService.saveComment(
                    userId, new SaveCommentRequestDto(postId, comment));

            //then
            verify(postRepository).existsById(postId);
            verify(commentRepository).save(any(Comment.class));
            assertThat(responseDto.id()).isEqualTo(commentEntity.getId());
        }

        @Test
        @DisplayName("게시물이 존재하지 않을 경우, 예외가 발생한다.")
        void testCase2() {
            //given
            given(postRepository.existsById(postId)).willReturn(false);

            //when
            ThrowingCallable when = () -> commentCommandService.saveComment(userId, new SaveCommentRequestDto(postId, comment));

            //then
            verify(commentRepository, times(0)).save(any(Comment.class));
            assertThatThrownBy(when)
                    .isInstanceOf(GreeningGlobalException.class)
                    .hasMessage(PostExceptionMessage.NOT_FOUND_POST.getMessage());
        }
    }

    @DisplayName("댓글 수정 및 삭제 테스트")
    @Nested
    class updateAndDeleteCommentTest {

        Long postId = 1L;
        Long userId = 100L;
        Long commentId = 2L;
        String comment = "테스트 댓글입니다";
        String newComment = "수정한 댓글입니다";

        Comment testComment = Comment.builder()
                .postId(postId)
                .userId(userId)
                .comment(comment)
                .build();

        @Test
        @DisplayName("댓글 내용이 알맞게 변경된다.")
        void testCase1() {
            //given
            given(commentRepository.findById(commentId)).willReturn(Optional.of(testComment));

            //when
            commentCommandService.updateComment(userId, new UpdateCommentRequestDto(commentId, newComment));

            //then
            assertThat(testComment.getComment()).isEqualTo(newComment);
            verify(commentRepository, times(1)).findById(commentId);
        }

        @Test
        @DisplayName("작성자가 아닌 사용자가 변경 시도 시, 예외가 발생한다.")
        void testCase2() {
            //given
            Long anotherUserId = 30L;
            given(commentRepository.findById(commentId)).willReturn(Optional.of(testComment));

            //when
            ThrowingCallable throwingCallable = () -> commentCommandService.updateComment(anotherUserId,
                    new UpdateCommentRequestDto(commentId, newComment));

            //then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(GreeningGlobalException.class)
                    .hasMessageContaining(CommentExceptionMessage.BAD_REQUEST_COMMENT_WRITER.getMessage());
        }

        @Test
        @DisplayName("댓글 삭제할 수 있다.")
        void testCase3() {
            //given
            given(commentRepository.findById(commentId)).willReturn(Optional.of(testComment));

            //when
            commentCommandService.deleteComment(userId,
                    new DeleteCommentRequestDto(commentId));

            //then
            verify(commentRepository, times(1)).deleteById(commentId);
        }
    }
}