import { Transaction } from "@ui/types"

export type DailyTransactions = {
    issued: string,
    transactions: Transaction[];
}