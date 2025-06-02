package finance.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class MonthlyExpenseLineItem {
    public int monthValue;

    public int yearValue;

    public boolean requested;

    @Builder.Default
    public String month = "";

    @Builder.Default
    public String amount = "";

    @Builder.Default
    public String income = "";
}
