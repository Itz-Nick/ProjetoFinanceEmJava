package com.nikofinance;

import com.nikofinance.dto.TransactionRequestDTO;
import com.nikofinance.model.Category;
import com.nikofinance.model.Transaction;
import com.nikofinance.repository.CategoryRepository;
import com.nikofinance.repository.TransactionRepository;
import com.nikofinance.service.TransactionService;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        categoryRepository.deleteAll();

        testCategory = Category.builder()
                .name("Teste")
                .icon("🧪")
                .color("#FF0000")
                .isDefault(false)
                .build();
        testCategory = categoryRepository.save(testCategory);
    }

    @Test
    void shouldCreateIncomeTransaction() {
        TransactionRequestDTO request = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.INCOME)
                .amount(new BigDecimal("1000.00"))
                .description("Sal\u00e1rio")
                .transactionDate(LocalDate.now())
                .categoryId(testCategory.getId())
                .build();

        TransactionResponseDTO created = transactionService.create(request);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getType()).isEqualTo("INCOME");
        assertThat(created.getAmount()).isEqualByComparingTo("1000.00");
        assertThat(created.getDescription()).isEqualTo("Sal\u00e1rio");
        assertThat(created.getCategoryId()).isEqualTo(testCategory.getId());
    }

    @Test
    void shouldCreateExpenseTransaction() {
        TransactionRequestDTO request = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.EXPENSE)
                .amount(new BigDecimal("50.00"))
                .description("Supermercado")
                .transactionDate(LocalDate.now())
                .categoryId(testCategory.getId())
                .build();

        TransactionResponseDTO created = transactionService.create(request);

        assertThat(created.getType()).isEqualTo("EXPENSE");
        assertThat(created.getAmount()).isEqualByComparingTo("50.00");
    }

    @Test
    void shouldCreateTransactionWithoutCategory() {
        TransactionRequestDTO request = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.INCOME)
                .amount(new BigDecimal("100.00"))
                .description("Freelance")
                .transactionDate(LocalDate.now())
                .build();

        TransactionResponseDTO created = transactionService.create(request);

        assertThat(created.getCategoryId()).isNull();
        assertThat(created.getCategoryName()).isNull();
    }

    @Test
    void shouldFindTransactionById() {
        TransactionRequestDTO request = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.INCOME)
                .amount(new BigDecimal("200.00"))
                .description("Teste")
                .transactionDate(LocalDate.now())
                .build();

        TransactionResponseDTO created = transactionService.create(request);
        Optional<TransactionResponseDTO> found = transactionService.findById(created.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(created.getId());
    }

    @Test
    void shouldReturnEmptyWhenTransactionNotFound() {
        Optional<TransactionResponseDTO> found = transactionService.findById(999L);
        assertThat(found).isEmpty();
    }

    @Test
    void shouldUpdateTransaction() {
        TransactionRequestDTO request = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.INCOME)
                .amount(new BigDecimal("100.00"))
                .description("Original")
                .transactionDate(LocalDate.now())
                .build();

        TransactionResponseDTO created = transactionService.create(request);

        TransactionRequestDTO updateRequest = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.EXPENSE)
                .amount(new BigDecimal("75.00"))
                .description("Atualizado")
                .transactionDate(LocalDate.now())
                .categoryId(testCategory.getId())
                .build();

        TransactionResponseDTO updated = transactionService.update(created.getId(), updateRequest);

        assertThat(updated.getType()).isEqualTo("EXPENSE");
        assertThat(updated.getAmount()).isEqualByComparingTo("75.00");
        assertThat(updated.getDescription()).isEqualTo("Atualizado");
        assertThat(updated.getCategoryId()).isEqualTo(testCategory.getId());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentTransaction() {
        TransactionRequestDTO request = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.INCOME)
                .amount(new BigDecimal("100.00"))
                .description("Teste")
                .transactionDate(LocalDate.now())
                .build();

        assertThatThrownBy(() -> transactionService.update(999L, request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldDeleteTransaction() {
        TransactionRequestDTO request = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.INCOME)
                .amount(new BigDecimal("100.00"))
                .description("Para excluir")
                .transactionDate(LocalDate.now())
                .build();

        TransactionResponseDTO created = transactionService.create(request);
        transactionService.delete(created.getId());

        Optional<TransactionResponseDTO> found = transactionService.findById(created.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void shouldThrowWhenDeletingNonExistentTransaction() {
        assertThatThrownBy(() -> transactionService.delete(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldCalculateDashboardSummary() {
        TransactionRequestDTO income1 = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.INCOME)
                .amount(new BigDecimal("1000.00"))
                .description("Sal\u00e1rio")
                .transactionDate(LocalDate.now())
                .build();

        TransactionRequestDTO income2 = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.INCOME)
                .amount(new BigDecimal("500.00"))
                .description("Freelance")
                .transactionDate(LocalDate.now())
                .build();

        TransactionRequestDTO expense1 = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.EXPENSE)
                .amount(new BigDecimal("300.00"))
                .description("Aluguel")
                .transactionDate(LocalDate.now())
                .build();

        TransactionRequestDTO expense2 = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.EXPENSE)
                .amount(new BigDecimal("150.00"))
                .description("Mercado")
                .transactionDate(LocalDate.now())
                .build();

        transactionService.create(income1);
        transactionService.create(income2);
        transactionService.create(expense1);
        transactionService.create(expense2);

        DashboardResponseDTO dashboard = transactionService.getDashboardSummary();

        assertThat(dashboard.getTotalIncome()).isEqualByComparingTo("1500.00");
        assertThat(dashboard.getTotalExpense()).isEqualByComparingTo("450.00");
        assertThat(dashboard.getBalance()).isEqualByComparingTo("1050.00");
        assertThat(dashboard.getTransactionCount()).isEqualTo(4);
        assertThat(dashboard.getIncomeCount()).isEqualTo(2);
        assertThat(dashboard.getExpenseCount()).isEqualTo(2);
    }

    @Test
    void shouldFilterTransactionsByDateRange() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate tomorrow = today.plusDays(1);

        TransactionRequestDTO t1 = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.INCOME)
                .amount(new BigDecimal("100.00"))
                .description("Hoje")
                .transactionDate(today)
                .build();

        TransactionRequestDTO t2 = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.EXPENSE)
                .amount(new BigDecimal("50.00"))
                .description("Ontem")
                .transactionDate(yesterday)
                .build();

        TransactionRequestDTO t3 = TransactionRequestDTO.builder()
                .type(TransactionRequestDTO.TransactionType.INCOME)
                .amount(new BigDecimal("200.00"))
                .description("Amanh\u00e3")
                .transactionDate(tomorrow)
                .build();

        transactionService.create(t1);
        transactionService.create(t2);
        transactionService.create(t3);

        List<TransactionResponseDTO> result = transactionService.findByDateRange(yesterday, today);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTransactionDate()).isEqualTo(today);
        assertThat(result.get(1).getTransactionDate()).isEqualTo(yesterday);
    }
}