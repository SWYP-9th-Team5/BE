package swyp.team5.greening.postLike.controller;

import static org.assertj.core.api.Assertions.assertThat;
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
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.post.fixture.PostFixture;
import swyp.team5.greening.postLike.domain.entity.Like;
import swyp.team5.greening.postLike.domain.repository.LikeRepository;
import swyp.team5.greening.postLike.dto.PostLikeRequestDto;
import swyp.team5.greening.postLike.fixture.LikeFixture;
import swyp.team5.greening.support.ApiTestSupport;

@DisplayName("게시글 좋아요 관련 통합 테스트")
class PostLikeControllerTest extends ApiTestSupport {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    private Post post;

    @BeforeEach
    void init() {
        postRepository.deleteAll();
        likeRepository.deleteAll();
    }

    @Nested
    @DisplayName("게시글이 존재한다.")
    class TestCase1 {

        Like like;
        Long likeCount = 20L;

        @BeforeEach
        void setUp() {
            post = PostFixture.getPost(likeCount, loginUser.getId());
            postRepository.save(post);
        }


        @Test
        @DisplayName("좋아요 하지 않은 상태라면 좋아요 처리 된다.")
        void likeTest() throws Exception {
            //when
            ResultActions perform = mockMvc.perform(post("/api/likes")
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(new PostLikeRequestDto(post.getId()))));

            Post resultPost = postRepository.findByIdAndState(post.getId(), PostState.IN_PROGRESS)
                    .orElseThrow();

            Like resultLike = likeRepository.findByUserIdAndPostId(loginUser.getId(), post.getId())
                    .orElse(Like.builder().build());

            //then
            perform.andExpectAll(
                    status().isOk(),
                    jsonPath("$.data.isLike").value(true)
            );

            assertThat(resultPost.getLikeCount()).isEqualTo(likeCount + 1);
            assertThat(resultLike.getId()).isNotNull();
        }

        @Test
        @DisplayName("좋아요 한 상태라면 좋아요 취소 처리 된다.")
        void likeCancelTest() throws Exception {
            //given
            like = LikeFixture.getLike(post.getId(), loginUser.getId());
            likeRepository.save(like);

            //when
            ResultActions perform = mockMvc.perform(post("/api/likes")
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(new PostLikeRequestDto(post.getId()))));

            Post resultPost = postRepository.findByIdAndState(post.getId(), PostState.IN_PROGRESS)
                    .orElseThrow();

            Like resultLike = likeRepository.findByUserIdAndPostId(loginUser.getId(), post.getId())
                    .orElse(Like.builder().build());

            //then
            perform.andExpectAll(
                    status().isOk(),
                    jsonPath("$.data.isLike").value(false)
            );

            assertThat(resultPost.getLikeCount()).isEqualTo(likeCount - 1);
            assertThat(resultLike.getId()).isNull();
        }
    }
}
