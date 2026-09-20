export type Category = {
    id: number;
    name: string;
};

export enum TransactionType {
    INCOME = "INCOME",
    EXPENSE = "EXPENSE",
}

export enum Currency {
    BYN = "BYN",
    RUB = "RUB",
    USD = "USD",
    EUR = "EUR",
}

export type Transaction = {
    id?: number;
    categoryId?: number;
    description: string;
    issued: string;
    amount: number;
    operation: TransactionType;
    currency: Currency;
};
