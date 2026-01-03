package finance.service;

import java.util.ArrayList;
import java.util.List;

import finance.objects.MonthlyExpense;
import finance.records.MonthlyInfo;

public class MonthlyInfoFetch {
    public List<MonthlyInfo> retrieve(int year) {
        List<MonthlyExpense> r = MonthlyExpense.where("YEAR(month_date) = ?1", year);

        List<MonthlyInfo> infos = r.stream().map(m -> new MonthlyInfo(
                m.getLocalDateTime("month_date").toLocalDate(),
                m.getBigDecimal("balance"),
                m.amount()))
                .toList();

        List<MonthlyInfo> result = new ArrayList<>(infos);

        result.add(new MonthlyInfo(
                null,
                new BigDecimalSum<>(infos).resultByProperty(MonthlyInfo::income),
                new BigDecimalSum<>(infos).resultByProperty(MonthlyInfo::amount)));

        return result;
    }
}
