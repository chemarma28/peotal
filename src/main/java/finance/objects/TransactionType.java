package finance.objects;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum TransactionType {
    INCOME("income"),
    EXPENSE("expense");

    private final String displayName;

    private static Map<String, TransactionType> displayNameToType;

    TransactionType(final String displayName) {
        this.displayName = displayName;
        put(displayName, this);
    }

    public String displayName() {
        return displayName;
    }

    public static TransactionType of(final FinanceCard card) {
        return card instanceof ExpenseCard ? EXPENSE : INCOME;
    }

    private static void put(String displayName, TransactionType type) {
        if (null == displayNameToType)
            displayNameToType = new ConcurrentHashMap<>();

        displayNameToType.put(displayName, type);
    }

    public static TransactionType parse(String param) {
        return displayNameToType.get(param);
    }
}
