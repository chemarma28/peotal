import { Currency } from "@ui/types";

export const CURRENCY_SYMBOLS: Record<Currency, string> = {
    [Currency.BYN]: "Br",
    [Currency.RUB]: "₽",
    [Currency.USD]: "$",
    [Currency.EUR]: "€",
};

const SYMBOL_AFTER_AMOUNT = new Set<Currency>([Currency.BYN]);

export function currencySymbol(currency: Currency): string {
    return CURRENCY_SYMBOLS[currency];
}

const AMOUNT_FORMAT = new Intl.NumberFormat("ru-RU", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
    useGrouping: true
});

export function formatMoney(amount: number, currency: Currency): string {
    const formatted = AMOUNT_FORMAT.format(amount);

    return SYMBOL_AFTER_AMOUNT.has(currency)
        ? `${formatted} ${currencySymbol(currency)}`
        : `${currencySymbol(currency)}${formatted}`;
}
