package finance.records;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CardFolder(LocalDate date, List<Card> cards, BigDecimal amount) {

}
