package swyp.team5.greening.postCategory.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.postCategory.exception.CategoryException;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
    QNA("QnA"),
    FREE_BULLETIN_BOARD("자유게시판"),
    SHARING("나눔");


    private final String description;

    public static CategoryType of(String categoryName) {
        try {
            return CategoryType.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new GreeningGlobalException(CategoryException.NOT_FOUND_CATEGORY);
        }
    }
}
