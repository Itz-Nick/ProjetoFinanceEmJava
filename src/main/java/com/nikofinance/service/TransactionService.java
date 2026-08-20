package com.nikofinance.service;

import com.nikofinance.dto.CategorySummaryDTO;
import com.nikofinance.dto.DashboardResponseDTO;
import com.nikofinance.dto.TransactionRequestDTO;
import com.nikofinance.dto.TransactionResponseDTO;
import com.nikofinance.model.Category;
import com.nikofinance.model.Transaction;
import com.nikofinance.repository.CategoryRepository;
import com.nikofinance.repository.TransactionRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionResponseDTO create(TransactionRequestDTO request) {
        Transaction transaction = mapToEntity(request);
        Transaction saved = transactionRepository.save(transaction);
        return mapToResponse(saved);
    }

    public List<TransactionResponseDTO> findAll() {
        return transactionRepository.findAllByOrderByTransactionDateDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByTransactionDateBetweenOrderByTransactionDateDesc(startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> findByTypeAndDateRange(Transaction.TransactionType type, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByTypeAndTransactionDateBetweenOrderByTransactionDateDesc(type, startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Optional<TransactionResponseDTO> findById(Long id) {
        return transactionRepository.findById(id)
                .map(this::mapToResponse);
    }

    public TransactionResponseDTO update(Long id, TransactionRequestDTO request) {
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada com ID: " + id));

        updateEntityFromRequest(existing, request);
        Transaction saved = transactionRepository.save(existing);
        return mapToResponse(saved);
    }

    public void delete(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new EntityNotFoundException("Transação não encontrada com ID: " + id);
        }
        transactionRepository.deleteById(id);
    }

    public DashboardResponseDTO getDashboardSummary() {
        BigDecimal totalIncome = transactionRepository.sumAmountByType(Transaction.TransactionType.INCOME).orElse(BigDecimal.ZERO);
        BigDecimal totalExpense = transactionRepository.sumAmountByType(Transaction.TransactionType.EXPENSE).orElse(BigDecimal.ZERO);
        BigDecimal balance = totalIncome.subtract(totalExpense);

        long totalCount = transactionRepository.count();
        long incomeCount = transactionRepository.countByType(Transaction.TransactionType.INCOME);
        long expenseCount = transactionRepository.countByType(Transaction.TransactionType.EXPENSE);

        return DashboardResponseDTO.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .transactionCount(totalCount)
                .incomeCount(incomeCount)
                .expenseCount(expenseCount)
                .build();
    }

    public List<CategorySummaryDTO> getCategorySummary(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = transactionRepository.getCategorySummary(startDate, endDate);
        return results.stream()
                .map(row -> CategorySummaryDTO.builder()
                        .categoryId(((Number) row[0]).longValue())
                        .categoryName((String) row[1])
                        .categoryIcon((String) row[2])
                        .categoryColor((String) row[3])
                        .totalAmount((BigDecimal) row[4])
                        .transactionCount(((Number) row[5]).longValue())
                        .build())
                .collect(Collectors.toList());
    }

    private Transaction mapToEntity(TransactionRequestDTO request) {
        Transaction transaction = Transaction.builder()
                .type(request.getType())
                .amount(request.getAmount())
                .description(request.getDescription())
                .transactionDate(request.getTransactionDate())
                .notes(request.getNotes())
                .build();

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + request.getCategoryId()));
            transaction.setCategory(category);
        }

        return transaction;
    }

    private void updateEntityFromRequest(Transaction transaction, TransactionRequestDTO request) {
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setNotes(request.getNotes());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + request.getCategoryId()));
            transaction.setCategory(category);
        } else {
            transaction.setCategory(null);
        }
    }

    private TransactionResponseDTO mapToResponse(Transaction transaction) {
        TransactionResponseDTO response = TransactionResponseDTO.builder()
                .id(transaction.getId())
                .type(transaction.getType().name())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .notes(transaction.getNotes())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();

        if (transaction.getCategory() != null) {
            response.setCategoryId(transaction.getCategory().getId());
            response.setCategoryName(transaction.getCategory().getName());
            response.setCategoryIcon(transaction.getCategory().getIcon());
            response.setCategoryColor(transaction.getCategory().getColor());
        }

        return response;
    }
}