package finance.records;

import java.math.BigDecimal;

public record Card(Long id, String description, BigDecimal amount) {

}
