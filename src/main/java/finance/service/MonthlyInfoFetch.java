package finance.service;

import java.util.List;

import finance.objects.MonthlyExpense;
import finance.records.MonthlyInfo;

public class MonthlyInfoFetch {
    public List<MonthlyInfo> retrieve(int year) {
        List<MonthlyExpense> r = MonthlyExpense.where("YEAR(month_date) = ?1", year);

        List<MonthlyInfo> result = r.stream().map(m -> new MonthlyInfo(
                m.getLocalDateTime("month_date").toLocalDate(),
                m.getBigDecimal("balance"),
                m.amount()))
                .toList();

        return result;
    }
}
