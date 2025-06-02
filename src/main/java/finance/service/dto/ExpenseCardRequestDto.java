package finance.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExpenseCardRequestDto {
    public String amount;
    public String date;
    public String description;
}
