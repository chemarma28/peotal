package application.mapper;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ValidationException;

import com.google.inject.name.Named;

import application.config.GuiceExtension;
import finance.domain.ExpenseCard;
import finance.guice.modules.Format;
import finance.service.dto.ExpenseCardLineItem;
import finance.service.dto.ExpenseCardRequestDto;
import finance.service.dto.ExpenseCardsList;
import finance.service.mapper.ExpenseCardMapper;

@ExtendWith(GuiceExtension.class)
public class ExpenseCardMapperTest {
    private ExpenseCardMapper mapper;

    private final ExpenseCard source = ExpenseCard.builder()
            .cardDate(LocalDate.of(2025, 9, 10))
            .description("Card")
            .amount(BigDecimal.valueOf(158.387))
            .build();

    @BeforeEach
    public void init(ExpenseCardMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void mapTest(@Named(Format.CURRENCY) NumberFormat numberFormat) {
        ExpenseCardLineItem dest = mapper.map(source, ExpenseCardLineItem.class);

        assert_valid_mapping(source, dest, numberFormat);
    }

    @Test
    public void listMappingTest(DateTimeFormatter formatter, @Named(Format.CURRENCY) NumberFormat numberFormat) {
        ExpenseCardsList cardsList = mapper.map(List.of(source));

        ExpenseCardLineItem dest = cardsList.cards.get(0);

        assert_valid_mapping(source, dest, numberFormat);

        Assertions.assertEquals(formatter.format(source.cardDate), cardsList.date);

        Assertions.assertEquals(1, dest.number);
    }

    private void assert_valid_mapping(ExpenseCard source, ExpenseCardLineItem dest, NumberFormat format) {
        String amountStr = format.format(source.amount);
        Assertions.assertEquals(amountStr, dest.amount);
        Assertions.assertEquals("Card", dest.description);
    }

    @Test
    public void requestMappingTest() {
        final String validAmount = "20.5";
        final String validDate = "10-09-2025";

        ExpenseCardRequestDto r1 = ExpenseCardRequestDto.builder()
                .amount(validAmount)
                .date(null)
                .description(null)
                .build();

        Exception e1 = Assertions.assertThrows(Exception.class, () -> {
            mapper.map(r1, ExpenseCard.class);
        });

        Assertions.assertTrue(e1.getCause().getClass().isAssignableFrom(ValidationException.class));
        Assertions.assertTrue(e1.getMessage().toLowerCase().contains("date"));

        ExpenseCardRequestDto r2 = ExpenseCardRequestDto.builder()
                .amount("not a number")
                .date(validDate)
                .description(null)
                .build();

        Exception e2 = Assertions.assertThrows(Exception.class, () -> {
            mapper.map(r2, ExpenseCard.class);
        });

        Assertions.assertTrue(e2.getCause().getClass().isAssignableFrom(ValidationException.class));
        Assertions.assertTrue(e2.getMessage().toLowerCase().contains("decimal"));

        ExpenseCardRequestDto r3 = ExpenseCardRequestDto.builder()
                .amount(validAmount)
                .date(validDate)
                .description(null)
                .build();

        ExpenseCard card = mapper.map(r3, ExpenseCard.class);

        Assertions.assertEquals(new BigDecimal(validAmount), card.amount);

        LocalDate cardDate = card.cardDate;

        Assertions.assertEquals(10, cardDate.getDayOfMonth());
        Assertions.assertEquals(9, cardDate.getMonthValue());
        Assertions.assertEquals(2025, cardDate.getYear());
    }
}
