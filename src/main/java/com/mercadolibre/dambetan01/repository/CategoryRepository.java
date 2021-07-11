package com.mercadolibre.dambetan01.repository;

import com.mercadolibre.dambetan01.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
