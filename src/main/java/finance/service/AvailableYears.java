package finance.service;

import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.javalite.common.Convert;

public class AvailableYears {
    public List<Integer> retrieve() {
        List<Map<String, Object>> records = Base.findAll("SELECT DISTINCT YEAR(month_date) AS y FROM monthly_expense");

        for (Map<String, Object> record : records) {
            Convert.toLong(record.get("year"));
        }

        return records.stream().map(r -> Convert.toInteger(r.get("y"))).toList();
    }
}
