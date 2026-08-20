package com.nikofinance.service;

import com.nikofinance.dto.CategoryRequestDTO;
import com.nikofinance.dto.CategoryResponseDTO;
import com.nikofinance.model.Category;
import com.nikofinance.repository.CategoryRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDTO create(CategoryRequestDTO request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Categoria com nome '" + request.getName() + "' já existe");
        }

        Category category = Category.builder()
                .name(request.getName())
                .icon(request.getIcon())
                .color(request.getColor())
                .isDefault(false)
                .build();

        Category saved = categoryRepository.save(category);
        return mapToResponse(saved);
    }

    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAllByOrderByNameAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDTO> findDefaults() {
        return categoryRepository.findByIsDefaultTrueOrderByNameAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Optional<CategoryResponseDTO> findById(Long id) {
        return categoryRepository.findById(id)
                .map(this::mapToResponse);
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO request) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + id));

        if (!existing.getName().equals(request.getName()) && categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Categoria com nome '" + request.getName() + "' já existe");
        }

        existing.setName(request.getName());
        existing.setIcon(request.getIcon());
        existing.setColor(request.getColor());

        Category saved = categoryRepository.save(existing);
        return mapToResponse(saved);
    }

    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + id));

        if (category.isDefault()) {
            throw new IllegalArgumentException("Não é possível excluir uma categoria padrão");
        }

        categoryRepository.deleteById(id);
    }

    public void initializeDefaultCategories() {
        String[] defaultCategories = {
            "Alimentação", "Transporte", "Educação", "Lazer",
            "Assinaturas", "Casa", "Saúde", "Outros"
        };

        String[] icons = {
            "🍔", "🚗", "📚", "🎮", "📱", "🏠", "❤️", "📦"
        };

        String[] colors = {
            "#EF4444", "#3B82F6", "#8B5CF6", "#F59E0B",
            "#EC4899", "#10B981", "#F97316", "#6B7280"
        };

        for (int i = 0; i < defaultCategories.length; i++) {
            String name = defaultCategories[i];
            if (!categoryRepository.existsByName(name)) {
                Category category = Category.builder()
                        .name(name)
                        .icon(icons[i])
                        .color(colors[i])
                        .isDefault(true)
                        .build();
                categoryRepository.save(category);
            }
        }
    }

    private CategoryResponseDTO mapToResponse(Category category) {
        long transactionCount = category.getTransactions() != null ? category.getTransactions().size() : 0;

        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .icon(category.getIcon())
                .color(category.getColor())
                .isDefault(category.isDefault())
                .transactionCount(transactionCount)
                .build();
    }
}