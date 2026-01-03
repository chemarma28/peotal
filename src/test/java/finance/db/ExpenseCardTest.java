package finance.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.javalite.activejdbc.Base;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import finance.objects.Contact;
import finance.objects.ExpenseCard;
import finance.objects.MonthlyExpense;

public class ExpenseCardTest {
    private final String driver = "org.h2.Driver";
    private final String url = "jdbc:h2:~/Workspace/test";
    private final String user = "dbuser";
    private final String pswd = "letmein";

    @BeforeEach
    public void before() {
        Base.open(driver, url, user, pswd);
        Base.openTransaction();
    }

    @Test
    public void new_expense_card() {
        final Contact contact = new Contact();
        contact.set("first_name", "test");
        contact.set("last_name", "test");
        contact.set("email", "test");
        contact.set("password", "test");
        assertTrue(contact.saveIt());

        final Long contact_id = contact.getLong("id");
        
        assertNotNull(contact_id);

        final ExpenseCard card = new ExpenseCard(contact_id, BigDecimal.ONE, LocalDate.of(2024, 1, 1), "test");

        assertTrue(card.saveIt());

        assertEquals("EXP-000000", card.getString("name"));
        assertEquals("test", card.getString("description"));

        final Long monthlyExpenseId = card.getLong("monthly_expense_id");

        assertNotNull(monthlyExpenseId);
        assertTrue(MonthlyExpense.exists(monthlyExpenseId));
        assertEquals(1, MonthlyExpense.count());
    }

    @AfterEach
    public void after() {
        Base.rollbackTransaction();
        Base.close();
    }
}
