package finance.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import finance.repository.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ExpenseCard implements Entity<Long> {
    public Long id;
    public String name;
    public String description;
    public BigDecimal amount;
    public LocalDate cardDate;
    public Contact cardKeeper;
    public MonthlyExpense monthlyExpense;

    @Override
    public Long id() {
        return id;
    }
}
