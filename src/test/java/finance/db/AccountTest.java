package finance.db;

import finance.objects.Account;
import finance.objects.ExpenseCard;
import finance.objects.IncomeCard;
import org.javalite.activejdbc.Base;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static java.math.BigDecimal.*;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    public static Account ACCOUNT() {
        final Account account = new Account("useraccount");

        return account;
    }

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
    public void test_new_account() {
        final Account account = ACCOUNT();
        assertTrue(account.save());
        assertEquals("useraccount", account.name());
        assertNotNull(account.id());
    }

    @Test
    public void test_reduce() {
        final Account account = ACCOUNT();
        account.save();

        final ExpenseCard card = account.reduce("EXP", LocalDateTime.now(), TEN);

        assertNotNull(card.id());
        assertEquals(TEN, card.amount());
        assertNotNull(card.date());
    }

    @Test
    public void test_top_up() {
        final Account account = ACCOUNT();
        account.save();

        final IncomeCard card = account.topUp("INC", LocalDateTime.now(), TEN);

        assertNotNull(card.id());
        assertEquals(TEN, card.amount());
        assertNotNull(card.date());
    }
}
