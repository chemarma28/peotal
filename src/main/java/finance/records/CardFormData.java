package finance.records;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardFormData(LocalDate cardDate, BigDecimal amount, String description) {

}
