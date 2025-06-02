package application.mapper;

import com.google.inject.name.Named;

import application.config.GuiceExtension;
import finance.domain.ExpenseCard;
import finance.domain.MonthlyExpense;
import finance.guice.modules.Format;
import finance.service.dto.MonthlyExpenseLineItem;
import finance.service.mapper.MonthlyExpenseMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;

@ExtendWith(GuiceExtension.class)
public class MonthlyExpenseMapperTest {
    private NumberFormat numberFormat;
    private MonthlyExpenseMapper mapper;

    @BeforeEach
    public void init(DateFormatSymbols symbols, @Named(Format.DECIMAL) NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
        mapper = new MonthlyExpenseMapper(symbols, numberFormat);
    }

    @Test
    public void mapTest() {
        MonthlyExpense source = MonthlyExpense.builder()
                .monthDate(LocalDate.of(2025, 7, 4))
                .expenseCards(
                        List.of(
                                ExpenseCard.builder()
                                        .amount(BigDecimal.valueOf(10))
                                        .build(),
                                ExpenseCard.builder()
                                        .amount(BigDecimal.valueOf(10))
                                        .build()
                        )
                )
                .build();

        MonthlyExpenseLineItem dest = mapper.map(source, MonthlyExpenseLineItem.class);

        Assertions.assertEquals(numberFormat.format(BigDecimal.valueOf(20)), dest.amount);

        Assertions.assertEquals(2025, dest.yearValue);

        Assertions.assertEquals(7, dest.monthValue);
    }
}
