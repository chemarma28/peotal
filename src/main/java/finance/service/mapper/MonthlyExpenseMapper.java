package finance.service.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import finance.domain.ExpenseCard;
import finance.domain.MonthlyExpense;
import finance.guice.modules.Format;
import finance.service.BigDecimalSum;
import finance.service.converter.MonthSymbol;
import finance.service.converter.PrettyDecimal;
import finance.service.dto.MonthlyExpenseLineItem;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;

@Singleton
public class MonthlyExpenseMapper extends ModelMapper {
    private final NumberFormat numberFormat;

    @Inject
    public MonthlyExpenseMapper(DateFormatSymbols symbols, @Named(Format.DECIMAL) NumberFormat numberFormat) {
        this.numberFormat = numberFormat;

        typeMap(MonthlyExpense.class, MonthlyExpenseLineItem.class).addMappings(mapper -> {
            mapper.using(new MonthSymbol(symbols))
                    .map(MonthlyExpense::getMonthDate, MonthlyExpenseLineItem::setMonth);

            Converter<LocalDate, Integer> monthValue = c -> c.getSource().getMonthValue();

            mapper.using(monthValue).map(MonthlyExpense::getMonthDate, MonthlyExpenseLineItem::setMonthValue);

            Converter<LocalDate, Integer> yearValue = c -> c.getSource().getYear();

            mapper.using(yearValue).map(MonthlyExpense::getMonthDate, MonthlyExpenseLineItem::setYearValue);

            mapper.using(new PrettyDecimal(numberFormat))
                    .map(MonthlyExpense::getBalance, MonthlyExpenseLineItem::setIncome);

            Converter<List<ExpenseCard>, String> summarazingAmount = c -> numberFormat.format(
                    new BigDecimalSum<>(c.getSource()).resultByProperty(ExpenseCard::getAmount)
            );

            mapper.using(summarazingAmount).map(MonthlyExpense::getExpenseCards, MonthlyExpenseLineItem::setAmount);
        });
    }

    public MonthlyExpenseLineItem map(BigDecimal amount, BigDecimal income) {
        return MonthlyExpenseLineItem.builder()
                .amount(numberFormat.format(amount))
                .income(numberFormat.format(income))
                .build();
    }
}
