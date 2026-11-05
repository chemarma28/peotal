import { Currency, Transaction } from "@ui/types";

export type DailyTransactions = {
    issued: string;
    transactions: Transaction[];
    sums: {
        currency: Currency;
        sum: number;
    }[];
};
