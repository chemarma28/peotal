package transactions;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Transaction {

    public Long id;
    public String description;
    public LocalDate issued;
    public TransactionType operation;
    public Long categoryId;
    public BigDecimal amount;

    public Transaction(String description, LocalDate issued, TransactionType opeartion, Long categoryId, BigDecimal amount) {
        this.description = description;
        this.issued = issued;
        this.operation = opeartion;
        this.categoryId = categoryId;
        this.amount = amount;
    }
}
