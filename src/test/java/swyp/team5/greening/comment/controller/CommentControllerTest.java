package swyp.team5.greening.comment.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import swyp.team5.greening.comment.domain.entity.Comment;
import swyp.team5.greening.comment.domain.repository.CommentRepository;
import swyp.team5.greening.comment.dto.request.DeleteCommentRequestDto;
import swyp.team5.greening.comment.dto.request.SaveCommentRequestDto;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.support.ApiTestSupport;
import swyp.team5.greening.user.domain.entity.LoginType;
import swyp.team5.greening.user.domain.entity.User;
import swyp.team5.greening.user.domain.repository.UserRepository;

@DisplayName("댓글 통합 테스트")
class CommentControllerTest extends ApiTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void init() {
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Nested
    @DisplayName("댓글이 존재하지 않는다.")
    class TestCase1 {

        Post post;

        @BeforeEach
        void setUp() {
            post = Post.builder()
                    .title("테스트")
                    .likeCount(0L)
                    .commentCount(0L)
                    .state(PostState.IN_PROGRESS)
                    .categoryId(1L)
                    .userId(loginUser.getId())
                    .build();

            postRepository.save(post);
        }


        @Test
        @DisplayName("사용자는 게시물에 댓글을 작성할 수 있다.")
        void addCommentTest() throws Exception {
            //given
            String comment = "테스트 댓글";
            SaveCommentRequestDto requestDto = new SaveCommentRequestDto(post.getId(), comment);

            //when
            ResultActions perform = mockMvc.perform(post("/api/comments")
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestDto))
            );

            //then
            perform.andExpect(status().isOk());
        }
    }

    @DisplayName("댓글이 2개 존재한다.")
    @Nested
    class TestCase2 {

        User anotherUser;
        String anotherEmail = "test@kakao.com";
        String anotherName = "다른 테스트 이름";

        Post post;

        Comment comment1;
        String c1 = "테스트 댓글 1";
        Comment comment2;
        String c2 = "테스트 댓글 2";

        @BeforeEach
        void setUp() {
            anotherUser = User.builder()
                    .email(anotherEmail)
                    .loginType(LoginType.KAKAO)
                    .userName(anotherName)
                    .nickname(anotherName)
                    .build();
            userRepository.save(anotherUser);

            post = Post.builder()
                    .title("테스트")
                    .likeCount(0L)
                    .commentCount(0L)
                    .state(PostState.IN_PROGRESS)
                    .categoryId(1L)
                    .userId(loginUser.getId())
                    .build();

            postRepository.save(post);

            comment1 = Comment.builder()
                    .comment(c1)
                    .userId(loginUser.getId())
                    .postId(post.getId())
                    .build();
            commentRepository.save(comment1);

            comment2 = Comment.builder()
                    .comment(c2)
                    .userId(anotherUser.getId())
                    .postId(post.getId())
                    .build();
            commentRepository.save(comment2);
        }

        @Test
        @DisplayName("게시물에 작성된 댓글을 모두 조회할 수 있다. 최신순으로 정렬되며, 자신이 작성한 댓글에 대한 boolean 값을 알맞게 내려준다")
        void getAllCommentTest() throws Exception {
            //when
            ResultActions perform = mockMvc.perform(get("/api/comments/posts/" + post.getId())
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .param("pageNumber", "1")
                    .param("pageSize", "100")
            );

            //then
            perform.andExpectAll(
                    jsonPath("$.data.size()").value(2),
                    jsonPath("$.data[0].commentId").value(comment2.getId()),
                    jsonPath("$.data[0].comment").value(comment2.getComment()),
                    jsonPath("$.data[0].isWriter").value(false),
                    jsonPath("$.data[1].commentId").value(comment1.getId()),
                    jsonPath("$.data[1].comment").value(comment1.getComment()),
                    jsonPath("$.data[1].isWriter").value(true)
            );
        }

        @Test
        @DisplayName("내가 작성한 댓글을 모두 조회할 수 있다.")
        void getMyComments() throws Exception {
            //when
            ResultActions perform = mockMvc.perform(get("/api/comments/my")
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .param("pageNumber", "1")
                    .param("pageSize", "100")
            );

            //then
            perform.andExpectAll(
                    jsonPath("$.data.size()").value(1),
                    jsonPath("$.data[0].commentId").value(comment1.getId()),
                    jsonPath("$.data[0].comment").value(comment1.getComment()),
                    jsonPath("$.data[0].postId").value(post.getId()),
                    jsonPath("$.data[0].postTitle").value(post.getTitle())
            );
        }

        @Test
        @DisplayName("자신이 작성한 댓글을 삭제할 수 있다.")
        void deleteComment1() throws Exception {
            //given
            DeleteCommentRequestDto requestDto = new DeleteCommentRequestDto(comment1.getId());

            //when
            ResultActions perform = mockMvc.perform(delete("/api/comments")
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestDto)));

            //then
            perform.andExpect(status().isOk());
        }

        @Test
        @DisplayName("다른 사용자가 작성한 댓글을 삭제시도 할 경우 예외가 발생한다")
        void deleteComment2() throws Exception {
            //given
            DeleteCommentRequestDto requestDto = new DeleteCommentRequestDto(comment2.getId());

            //when
            ResultActions perform = mockMvc.perform(delete("/api/comments")
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestDto)));

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(result ->
                            assertThat(result.getResolvedException()).getClass()
                                    .isAssignableFrom(
                                            GreeningGlobalException.class));
        }
    }

}