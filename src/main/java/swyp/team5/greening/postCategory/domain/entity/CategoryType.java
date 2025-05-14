package swyp.team5.greening.postCategory.domain.entity;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.exception.PostExceptionMessage;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
    QNA("QnA"),
    FREE_BULLETIN_BOARD("자유게시판"),
    SHARING("나눔");


    private final String description;

    public static CategoryType fromDescription(String description) {
        return Arrays.stream(CategoryType.values())
                .filter(type -> type.getDescription().equalsIgnoreCase(description))
                .findFirst()
                .orElseThrow(
                        () -> new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_CATEGORY));
    }
}
