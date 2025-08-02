package finance.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class MonthItem {
    public LocalDate month;
    public BigDecimal amount;
    public BigDecimal income;

    public String htmxElementId() {
        return String.format("mi-%s", month.getMonth().hashCode());
    }
}
