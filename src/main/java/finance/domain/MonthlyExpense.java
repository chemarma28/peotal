package finance.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import finance.repository.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MonthlyExpense implements Entity<Long> {
    public Long id;
    public String name;
    public BigDecimal balance;
    public Contact keeper;
    public LocalDate monthDate;
    public List<ExpenseCard> expenseCards;

    @Override
    public Long id() {
        return id;
    }
}
