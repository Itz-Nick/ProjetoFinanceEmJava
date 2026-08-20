package com.nikofinance;

import com.nikofinance.model.Category;
import com.nikofinance.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldSaveAndFindCategory() {
        Category category = Category.builder()
                .name("Teste")
                .icon("🧪")
                .color("#FF0000")
                .isDefault(false)
                .build();

        Category saved = categoryRepository.save(category);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Teste");
        assertThat(saved.getIcon()).isEqualTo("🧪");
        assertThat(saved.getColor()).isEqualTo("#FF0000");
        assertThat(saved.isDefault()).isFalse();
    }

    @Test
    void shouldFindByName() {
        Category category = Category.builder()
                .name("Alimentação")
                .icon("🍔")
                .color("#EF4444")
                .isDefault(true)
                .build();
        categoryRepository.save(category);

        Optional<Category> found = categoryRepository.findByName("Alimentação");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Alimentação");
        assertThat(found.get().isDefault()).isTrue();
    }

    @Test
    void shouldReturnEmptyWhenNameNotFound() {
        Optional<Category> found = categoryRepository.findByName("Inexistente");
        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindDefaultCategories() {
        Category defaultCat = Category.builder()
                .name("Padrão")
                .icon("⭐")
                .color("#FFD700")
                .isDefault(true)
                .build();

        Category customCat = Category.builder()
                .name("Personalizada")
                .icon("🔧")
                .color("#808080")
                .isDefault(false)
                .build();

        categoryRepository.save(defaultCat);
        categoryRepository.save(customCat);

        List<Category> defaults = categoryRepository.findByIsDefaultTrueOrderByNameAsc();

        assertThat(defaults).hasSize(1);
        assertThat(defaults.get(0).getName()).isEqualTo("Padrão");
    }

    @Test
    void shouldFindAllOrderedByName() {
        Category c1 = Category.builder().name("Zebra").icon("🦓").color("#000000").isDefault(false).build();
        Category c2 = Category.builder().name("Abacaxi").icon("🍍").color("#FFFF00").isDefault(false).build();
        Category c3 = Category.builder().name("Banana").icon("🍌").color("#FFFF00").isDefault(false).build();

        categoryRepository.save(c1);
        categoryRepository.save(c2);
        categoryRepository.save(c3);

        List<Category> all = categoryRepository.findAllByOrderByNameAsc();

        assertThat(all).hasSize(3);
        assertThat(all.get(0).getName()).isEqualTo("Abacaxi");
        assertThat(all.get(1).getName()).isEqualTo("Banana");
        assertThat(all.get(2).getName()).isEqualTo("Zebra");
    }

    @Test
    void shouldCheckExistsByName() {
        Category category = Category.builder()
                .name("Existente")
                .icon("✅")
                .color("#00FF00")
                .isDefault(false)
                .build();
        categoryRepository.save(category);

        assertThat(categoryRepository.existsByName("Existente")).isTrue();
        assertThat(categoryRepository.existsByName("Não Existe")).isFalse();
    }
}