package finance.guice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import finance.domain.MonthlyExpense;
import finance.repository.QueueBasedRepository;
import finance.repository.Repository;
import finance.service.SimpleFaker;

public class LocalRepository extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<Repository<Long, MonthlyExpense>>() {
        }).toInstance(new QueueBasedRepository<>(SimpleFaker.monthlyExpenses));
    }
}
