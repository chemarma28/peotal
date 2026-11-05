package categories;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import transactions.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySummary {
    public Long categoryId;
    public Currency currency;
    public BigDecimal amount;
}
