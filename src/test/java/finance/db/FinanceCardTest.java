package finance.db;

import finance.objects.Account;
import finance.objects.ExpenseCard;
import finance.objects.IncomeCard;
import org.javalite.activejdbc.Base;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static finance.db.AccountTest.ACCOUNT;
import static org.junit.jupiter.api.Assertions.*;

public class FinanceCardTest {
    @BeforeEach
    public void before() {
        Base.open(Credentials.driver, Credentials.url, Credentials.user, Credentials.pswd);
        Base.openTransaction();
    }

    @AfterEach
    public void after() {
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void test_new_expense_card() {
        final Account account = ACCOUNT();
        assertTrue(account.save());

        final ExpenseCard card = new ExpenseCard(account, LocalDateTime.now(), BigDecimal.TEN, "EXP");

        assertTrue(card.save());
        assertNotNull(card.id());
        assertEquals(BigDecimal.TEN, card.amount());
        assertEquals("EXP", card.description());
        assertNotNull(card.date());

        final Account parent = card.parent(Account.class);
        assertNotNull(parent.id());
    }

    @Test
    public void test_new_income_card() {
        final Account account = ACCOUNT();
        assertTrue(account.save());

        final IncomeCard card = new IncomeCard(account, LocalDateTime.now(), BigDecimal.TEN, "INC");

        assertTrue(card.save());
        assertNotNull(card.id());
        assertEquals(BigDecimal.TEN, card.amount());
        assertEquals("INC", card.description());
        assertNotNull(card.date());

        final Account parent = card.parent(Account.class);
        assertNotNull(parent.id());
    }
}
