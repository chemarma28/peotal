package transactions;

import java.math.BigDecimal;
import java.time.LocalDate;

import annotations.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Valid
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Nullable
    public Long id;

    @Size(max = 255)
    @Nullable
    public String description;

    @NotNull
    @PastOrPresent
    public LocalDate issued;

    @NotNull
    public TransactionType operation;

    @NotNull
    public Long categoryId;

    @NotNull
    @Positive
    public BigDecimal amount;

    @NotNull
    public Currency currency;
}
