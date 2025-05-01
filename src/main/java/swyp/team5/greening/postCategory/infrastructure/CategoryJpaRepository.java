package swyp.team5.greening.postCategory.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.team5.greening.postCategory.domain.entity.Category;
import swyp.team5.greening.postCategory.domain.repository.CategoryRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long>, CategoryRepository {

}
