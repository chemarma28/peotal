package finance.records;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Card(Long id, String description, BigDecimal amount, LocalDate cardDate) {

}
