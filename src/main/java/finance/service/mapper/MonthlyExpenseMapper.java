package finance.service.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import com.google.inject.Singleton;

import finance.domain.ExpenseCard;
import finance.domain.MonthlyExpense;
import finance.service.BigDecimalSum;
import finance.service.dto.MonthItem;

@Singleton
public class MonthlyExpenseMapper extends ModelMapper {
    private final Converter<List<ExpenseCard>, BigDecimal> sumAmount = c -> new BigDecimalSum<>(c.getSource())
            .resultByProperty(ExpenseCard::getAmount);

    public MonthlyExpenseMapper() {
        typeMap(MonthlyExpense.class, MonthItem.class).addMappings(mapper -> {
            mapper.map(MonthlyExpense::getMonthDate, MonthItem::setMonth);

            mapper.map(MonthlyExpense::getBalance, MonthItem::setIncome);

            mapper.using(sumAmount).map(MonthlyExpense::getExpenseCards, MonthItem::setAmount);
        });

        this.validate();
    }
}
