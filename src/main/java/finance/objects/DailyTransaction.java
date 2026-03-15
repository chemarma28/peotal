package finance.objects;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DailyTransaction(
        long id,
        String description,
        BigDecimal amount,
        LocalDateTime date,
        String category,
        TransactionType type) {
    public DailyTransaction(final FinanceCard card) {
        this(card.id(), card.description(), amount(card), card.date(), category(card), TransactionType.of(card));
    }

    private static BigDecimal amount(final FinanceCard card) {
        return incomeQ(card) ? card.amount() : card.amount().negate();
    }

    private static String category(final FinanceCard card) {
        if (incomeQ(card))
            return "Income";

        final ExpenseCard expense = (ExpenseCard) card;
        final FinanceCategory category = expense.category();

        if (null == category)
            return "Undefined";

        return category.name();
    }

    private static boolean incomeQ(final FinanceCard card) {
        if (card instanceof IncomeCard)
            return true;

        return false;
    }
}
