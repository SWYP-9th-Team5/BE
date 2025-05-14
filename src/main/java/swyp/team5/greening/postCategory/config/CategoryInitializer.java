package swyp.team5.greening.postCategory.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swyp.team5.greening.postCategory.domain.entity.Category;
import swyp.team5.greening.postCategory.domain.entity.CategoryType;
import swyp.team5.greening.postCategory.domain.repository.CategoryRepository;

@Component
@RequiredArgsConstructor
public class CategoryInitializer {

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        for (CategoryType type : CategoryType.values()) {
            if (!categoryRepository.existsByCategoryType(type)) {
                categoryRepository.save(new Category(type)); // 여기서 오류 날 수 있음
            }
        }
    }
}