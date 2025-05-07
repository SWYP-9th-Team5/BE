package swyp.team5.greening.comment.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import swyp.team5.greening.comment.dto.request.SaveCommentRequestDto;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.support.ApiTestSupport;

@DisplayName("댓글 통합 테스트")
class CommentControllerTest extends ApiTestSupport {

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void init() {
        postRepository.deleteAll();
    }

    @DisplayName("댓글 생성 테스트")
    @Nested
    class addCommentTest {

        Post post;

        @BeforeEach
        void setUp() {
            post = Post.builder()
                    .title("테스트")
                    .content("테스트 게시물")
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
        void testCase1() throws Exception {
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
            perform.andExpect(jsonPath("$.data.id").value(post.getId()));
        }
    }

}