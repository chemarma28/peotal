package finance.db;

import java.util.List;

import org.javalite.activejdbc.Base;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import finance.objects.MonthlyExpense;
import finance.records.MonthlyInfo;

public class MonthlyExpenseViabilityTest {

    Logger logger = LoggerFactory.getLogger(MonthlyExpenseViabilityTest.class);

    @BeforeAll
    public static void before() {
        Base.open("org.h2.Driver", "jdbc:h2:~/Workspace/dev", "dbuser", "letmein");
    }

    @Test
    public void test1() {
        System.out.printf("%05d", 12);
    }

    @Test
    public void test() {
        logger.info("Starting test");

        var q = List.of(
                "SELECT * FROM monthly_expense WHERE",
                "YEAR(monthly_expense.month_date) = ?1");

        List<MonthlyExpense> fetchResult = MonthlyExpense.findBySQL(String.join(" ", q), 2025);

        fetchResult.stream().map(
                m -> new MonthlyInfo(
                        m.getLocalDateTime("month_date").toLocalDate(),
                        m.getBigDecimal("balance"),
                        m.amount()))
                .forEach(System.out::println);
    }

    @AfterAll
    public static void after() {
        Base.close();
    }
}
