package finance.records;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MonthlyInfo(LocalDate monthDate, BigDecimal income, BigDecimal amount) {

}
