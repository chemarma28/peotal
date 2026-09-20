import langs from "@i18n/langs.json";
import { _, init } from "@codeborne/i18n-json";
import { TransactionType } from "./types";
export * from "@codeborne/i18n-json";

export async function initTranslations() {
    await init({ langs });
}

export function _transactionType(type: TransactionType): string {
    if (type === TransactionType.INCOME) return _("types.transactions.income");

    if (type === TransactionType.EXPENSE) return _("types.transactions.expense");

    return type;
}
