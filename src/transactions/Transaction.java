package transactions;

import java.math.BigDecimal;
import java.time.LocalDate;

import annotations.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Nullable
    public Long id;
    public String description;
    public LocalDate issued;
    public TransactionType operation;
    public Long categoryId;
    public BigDecimal amount;
    public Currency currency;
}
