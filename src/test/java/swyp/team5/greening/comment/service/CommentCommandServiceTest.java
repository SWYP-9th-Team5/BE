package swyp.team5.greening.comment.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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
import swyp.team5.greening.comment.dto.request.SaveCommentRequestDto;
import swyp.team5.greening.comment.dto.response.SaveCommentResponseDto;
import swyp.team5.greening.comment.exception.PostExceptionMessage;
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
}