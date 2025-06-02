package finance.service.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import finance.domain.ExpenseCard;
import finance.guice.modules.Format;
import finance.service.BigDecimalSum;
import finance.service.converter.PrettyDecimal;
import finance.service.converter.ValidDecimal;
import finance.service.converter.ValidLocalDate;
import finance.service.dto.ExpenseCardLineItem;
import finance.service.dto.ExpenseCardRequestDto;
import finance.service.dto.ExpenseCardsList;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Singleton
public class ExpenseCardMapper extends ModelMapper {
    private final DateTimeFormatter dateTimeFormatter;
    private final NumberFormat fcurrency;

    @Inject
    public ExpenseCardMapper(DateTimeFormatter dateTimeFormatter, @Named(Format.CURRENCY) NumberFormat currencyFormat) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.fcurrency = currencyFormat;

        typeMap(ExpenseCard.class, ExpenseCardLineItem.class).addMappings(mapper -> {
            mapper.using(new PrettyDecimal(currencyFormat)).map(ExpenseCard::getAmount, ExpenseCardLineItem::setAmount);
            mapper.map(ExpenseCard::getDescription, ExpenseCardLineItem::setDescription);
        });

        typeMap(ExpenseCardRequestDto.class, ExpenseCard.class).addMappings(mapper -> {
            mapper.using(new ValidDecimal()).map(ExpenseCardRequestDto::getAmount, ExpenseCard::setAmount);
            mapper.using(new ValidLocalDate()).map(ExpenseCardRequestDto::getDate, ExpenseCard::setCardDate);
            mapper.map(ExpenseCardRequestDto::getDescription, ExpenseCard::setDescription);
        });
    }

    public ExpenseCardsList map(List<ExpenseCard> cards) {
        if (cards != null && !cards.isEmpty()) {
            ExpenseCard first = cards.get(0);

            List<ExpenseCardLineItem> lineItems = this.map(cards, new TypeToken<List<ExpenseCardLineItem>>() {
            }.getType());

            for (int i = 1; i <= lineItems.size(); i++)
                lineItems.get(i - 1).number = i;

            return ExpenseCardsList.builder()
                    .id("card-list-" + first.cardDate.hashCode())
                    .date(dateTimeFormatter.format(first.cardDate))
                    .cards(lineItems)
                    .total(fcurrency.format(new BigDecimalSum<>(cards).resultByProperty(card -> card.amount)))
                    .build();
        }

        return ExpenseCardsList.builder()
                .date("")
                .cards(Collections.emptyList())
                .build();
    }
}
