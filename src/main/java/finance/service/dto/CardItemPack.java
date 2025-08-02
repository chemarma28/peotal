package finance.service.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardItemPack {
    public LocalDate cardDate;
    public List<CardItem> cards;
    public CardItem total;
}
