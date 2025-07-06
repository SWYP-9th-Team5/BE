package swyp.team5.greening.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record FindAllCommentResponseDto(
        Long commentId,

        Long userId,

        String userName,

        String comment,

        @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
        LocalDateTime createdAt,

        boolean isWriter
) {

}
