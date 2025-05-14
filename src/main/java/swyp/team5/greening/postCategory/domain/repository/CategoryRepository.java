package swyp.team5.greening.postCategory.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import swyp.team5.greening.postCategory.domain.entity.Category;
import swyp.team5.greening.postCategory.domain.entity.CategoryType;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryType(CategoryType categoryType);

    boolean existsByCategoryType(CategoryType categoryType);

}
