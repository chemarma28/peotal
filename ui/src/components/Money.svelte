<script lang="ts">
    import { formatMoney, formatNumber } from "@ui/currencies";
    import { Currency, TransactionType } from "@ui/types";

    type Props = {
        operation: TransactionType;
        currency: Currency;
        amount: number;
        noColor?: boolean;
        showSymbol?: boolean;
    };

    let { operation, currency, amount, noColor, showSymbol = true }: Props = $props();

    const sign = $derived(amount === 0 ? "" : operation === TransactionType.EXPENSE ? "-" : "+");
    const formatted = $derived(showSymbol ? formatMoney(amount, currency) : formatNumber(amount));
</script>

<span class={noColor ? "" : operation === TransactionType.EXPENSE ? "text-expense" : "text-income"}>
    {sign}{formatted}
</span>
