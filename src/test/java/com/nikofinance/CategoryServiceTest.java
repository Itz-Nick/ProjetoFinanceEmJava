package com.nikofinance;

import com.nikofinance.dto.CategoryRequestDTO;
import com.nikofinance.dto.CategoryResponseDTO;
import com.nikofinance.model.Category;
import com.nikofinance.repository.CategoryRepository;
import com.nikofinance.service.CategoryService;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    void shouldCreateCategory() {
        CategoryRequestDTO request = CategoryRequestDTO.builder()
                .name("Nova Categoria")
                .icon("\u2b50")
                .color("#FFD700")
                .build();

        CategoryResponseDTO created = categoryService.create(request);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Nova Categoria");
        assertThat(created.getIcon()).isEqualTo("\u2b50");
        assertThat(created.getColor()).isEqualTo("#FFD700");
        assertThat(created.isDefault()).isFalse();
    }

    @Test
    void shouldThrowWhenCreatingDuplicateName() {
        CategoryRequestDTO request = CategoryRequestDTO.builder()
                .name("Duplicada")
                .icon("\ud83d\udd04")
                .color("#000000")
                .build();

        categoryService.create(request);

        assertThatThrownBy(() -> categoryService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("j\u00e1 existe");
    }

    @Test
    void shouldFindAllCategories() {
        categoryService.create(CategoryRequestDTO.builder().name("A").icon("1").color("#111").build());
        categoryService.create(CategoryRequestDTO.builder().name("B").icon("2").color("#222").build());
        categoryService.create(CategoryRequestDTO.builder().name("C").icon("3").color("#333").build());

        List<CategoryResponseDTO> all = categoryService.findAll();

        assertThat(all).hasSize(3);
    }

    @Test
    void shouldFindById() {
        CategoryResponseDTO created = categoryService.create(CategoryRequestDTO.builder()
                .name("Teste")
                .icon("\ud83e\uddea")
                .color("#FF0000")
                .build());

        Optional<CategoryResponseDTO> found = categoryService.findById(created.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Teste");
    }

    @Test
    void shouldReturnEmptyWhenCategoryNotFound() {
        Optional<CategoryResponseDTO> found = categoryService.findById(999L);
        assertThat(found).isEmpty();
    }

    @Test
    void shouldUpdateCategory() {
        CategoryResponseDTO created = categoryService.create(CategoryRequestDTO.builder()
                .name("Original")
                .icon("1")
                .color("#111111")
                .build());

        CategoryRequestDTO updateRequest = CategoryRequestDTO.builder()
                .name("Atualizada")
                .icon("2")
                .color("#222222")
                .build();

        CategoryResponseDTO updated = categoryService.update(created.getId(), updateRequest);

        assertThat(updated.getName()).isEqualTo("Atualizada");
        assertThat(updated.getIcon()).isEqualTo("2");
        assertThat(updated.getColor()).isEqualTo("#222222");
    }

    @Test
    void shouldThrowWhenUpdatingToDuplicateName() {
        categoryService.create(CategoryRequestDTO.builder().name("Existente").icon("1").color("#111").build());
        CategoryResponseDTO toUpdate = categoryService.create(CategoryRequestDTO.builder().name("Outra").icon("2").color("#222").build());

        CategoryRequestDTO updateRequest = CategoryRequestDTO.builder()
                .name("Existente")
                .icon("3")
                .color("#333")
                .build();

        assertThatThrownBy(() -> categoryService.update(toUpdate.getId(), updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("j\u00e1 existe");
    }

    @Test
    void shouldDeleteCategory() {
        CategoryResponseDTO created = categoryService.create(CategoryRequestDTO.builder()
                .name("Para Excluir")
                .icon("\ud83d\uddd1\ufe0f")
                .color("#FF0000")
                .build());

        categoryService.delete(created.getId());

        Optional<CategoryResponseDTO> found = categoryService.findById(created.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void shouldThrowWhenDeletingDefaultCategory() {
        CategoryResponseDTO defaultCat = categoryService.create(CategoryRequestDTO.builder()
                .name("Padr\u00e3o")
                .icon("\u2b50")
                .color("#FFD700")
                .build());

        // Manually set as default
        Category cat = categoryRepository.findById(defaultCat.getId()).orElseThrow();
        cat.setDefault(true);
        categoryRepository.save(cat);

        assertThatThrownBy(() -> categoryService.delete(defaultCat.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("N\u00e3o \u00e9 poss\u00edvel excluir uma categoria padr\u00e3o");
    }

    @Test
    void shouldInitializeDefaultCategories() {
        categoryService.initializeDefaultCategories();

        List<CategoryResponseDTO> all = categoryService.findAll();
        assertThat(all).hasSizeGreaterThanOrEqualTo(8);

        List<CategoryResponseDTO> defaults = categoryService.findDefaults();
        assertThat(defaults).hasSizeGreaterThanOrEqualTo(8);
        assertThat(defaults).allMatch(c -> c.isDefault());
    }
}