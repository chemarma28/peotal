package finance.service.dto;

import java.util.List;

import lombok.Builder;

@Builder
public class ExpenseCardsList {
    public String id;
    public String date;
    public List<ExpenseCardLineItem> cards;
    public String total;
}
