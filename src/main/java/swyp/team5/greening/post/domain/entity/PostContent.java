package swyp.team5.greening.post.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.team5.greening.common.base.BaseTimeEntity;

@Getter
@Entity
@Table(name = "post_contents")
@NoArgsConstructor
public class PostContent extends BaseTimeEntity {

    @Id
    @Column(name = "post_content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PostType type;

    @Column(name = "sequence")
    private Integer sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostContent(String content, PostType type, Integer sequence, Post post) {
        this.content = content;
        this.type = type;

        if (!Objects.isNull(sequence)) {
            this.sequence = sequence;
        }

        if (!Objects.isNull(post)) {
            this.post = post;
            post.getPostContents().add(this);
        }
    }

    public void setPost(Post post) {
        this.post = post;
        post.getPostContents().add(this);
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}


