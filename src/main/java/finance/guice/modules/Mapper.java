package finance.guice.modules;

import com.google.inject.AbstractModule;

import finance.service.mapper.ExpenseCardMapper;
import finance.service.mapper.MonthlyExpenseMapper;

public class Mapper extends AbstractModule {
    @Override
    protected void configure() {
        bind(ExpenseCardMapper.class);
        bind(MonthlyExpenseMapper.class);
    }
}
