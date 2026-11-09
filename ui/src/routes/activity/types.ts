import { Currency, Transaction } from "@ui/types";

export type DailyTotal = {
    currency: Currency;
    income: number;
    expense: number;
};

export type DailyTransactions = {
    issued: string;
    transactions: Transaction[];
    totals: DailyTotal[];
};
