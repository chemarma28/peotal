package categories;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySummaries {
    public Long categoryId;
    public List<CategorySummary> sums;
    public BigDecimal weight;
}
