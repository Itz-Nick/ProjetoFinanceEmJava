package com.nikofinance;

import com.nikofinance.model.Category;
import com.nikofinance.model.Transaction;
import com.nikofinance.repository.CategoryRepository;
import com.nikofinance.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldSaveAndFindTransaction() {
        Category category = Category.builder()
                .name("Teste")
                .icon("🧪")
                .color("#FF0000")
                .isDefault(false)
                .build();
        entityManager.persistAndFlush(category);

        Transaction transaction = Transaction.builder()
                .type(Transaction.TransactionType.INCOME)
                .amount(new BigDecimal("100.50"))
                .description("Salário")
                .transactionDate(LocalDate.now())
                .category(category)
                .build();

        Transaction saved = transactionRepository.save(transaction);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAmount()).isEqualByComparingTo("100.50");
        assertThat(saved.getType()).isEqualTo(Transaction.TransactionType.INCOME);
        assertThat(saved.getCategory().getId()).isEqualTo(category.getId());
    }

    @Test
    void shouldFindTransactionsByDateRange() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate tomorrow = today.plusDays(1);

        Transaction t1 = Transaction.builder()
                .type(Transaction.TransactionType.INCOME)
                .amount(new BigDecimal("100.00"))
                .description("Hoje")
                .transactionDate(today)
                .build();

        Transaction t2 = Transaction.builder()
                .type(Transaction.TransactionType.EXPENSE)
                .amount(new BigDecimal("50.00"))
                .description("Ontem")
                .transactionDate(yesterday)
                .build();

        Transaction t3 = Transaction.builder()
                .type(Transaction.TransactionType.INCOME)
                .amount(new BigDecimal("200.00"))
                .description("Amanhã")
                .transactionDate(tomorrow)
                .build();

        entityManager.persistAndFlush(t1);
        entityManager.persistAndFlush(t2);
        entityManager.persistAndFlush(t3);

        List<Transaction> result = transactionRepository.findByTransactionDateBetweenOrderByTransactionDateDesc(yesterday, today);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTransactionDate()).isEqualTo(today);
        assertThat(result.get(1).getTransactionDate()).isEqualTo(yesterday);
    }

    @Test
    void shouldSumAmountByType() {
        Transaction t1 = Transaction.builder()
                .type(Transaction.TransactionType.INCOME)
                .amount(new BigDecimal("100.00"))
                .description("Receita 1")
                .transactionDate(LocalDate.now())
                .build();

        Transaction t2 = Transaction.builder()
                .type(Transaction.TransactionType.INCOME)
                .amount(new BigDecimal("200.00"))
                .description("Receita 2")
                .transactionDate(LocalDate.now())
                .build();

        Transaction t3 = Transaction.builder()
                .type(Transaction.TransactionType.EXPENSE)
                .amount(new BigDecimal("50.00"))
                .description("Despesa 1")
                .transactionDate(LocalDate.now())
                .build();

        entityManager.persistAndFlush(t1);
        entityManager.persistAndFlush(t2);
        entityManager.persistAndFlush(t3);

        Optional<BigDecimal> incomeSum = transactionRepository.sumAmountByType(Transaction.TransactionType.INCOME);
        Optional<BigDecimal> expenseSum = transactionRepository.sumAmountByType(Transaction.TransactionType.EXPENSE);

        assertThat(incomeSum).isPresent();
        assertThat(incomeSum.get()).isEqualByComparingTo("300.00");

        assertThat(expenseSum).isPresent();
        assertThat(expenseSum.get()).isEqualByComparingTo("50.00");
    }

    @Test
    void shouldCountByType() {
        Transaction t1 = Transaction.builder()
                .type(Transaction.TransactionType.INCOME)
                .amount(new BigDecimal("100.00"))
                .description("Receita")
                .transactionDate(LocalDate.now())
                .build();

        Transaction t2 = Transaction.builder()
                .type(Transaction.TransactionType.EXPENSE)
                .amount(new BigDecimal("50.00"))
                .description("Despesa")
                .transactionDate(LocalDate.now())
                .build();

        entityManager.persistAndFlush(t1);
        entityManager.persistAndFlush(t2);

        long incomeCount = transactionRepository.countByType(Transaction.TransactionType.INCOME);
        long expenseCount = transactionRepository.countByType(Transaction.TransactionType.EXPENSE);

        assertThat(incomeCount).isEqualTo(1);
        assertThat(expenseCount).isEqualTo(1);
    }
}