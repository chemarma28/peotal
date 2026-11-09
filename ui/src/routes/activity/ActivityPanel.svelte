<script lang="ts">
    import Accordion from "@ui/components/Accordion.svelte";
    import AccordionItem from "@ui/components/AccordionItem.svelte";
    import Badge from "@ui/components/Badge.svelte";
    import Money from "@ui/components/Money.svelte";
    import { currencySymbol } from "@ui/currencies";
    import { _ } from "@ui/i18n";
    import { TransactionType, type Category, type Currency, type Transaction } from "@ui/types";
    import type { DailyTotal, DailyTransactions } from "./types";

    type Props = {
        categories: Map<number, Category>;
        transactions: Transaction[];
        onEdit: (transaction: Transaction) => void;
    };

    let { categories, transactions, onEdit }: Props = $props();

    const findCategory = (categoryId?: number) => (categoryId ? categories.get(categoryId) : undefined);

    const dailyTransactions = $derived<DailyTransactions[]>(
        transactions
            .toSorted((a, b) => b.issued.localeCompare(a.issued))
            .groupingBy((it) => it.issued)
            .map((e) => {
                const issued = e.by as string;
                const lineItems = e.entries;
                const totalsByCurrency = new Map<Currency, DailyTotal>();

                for (const transaction of lineItems) {
                    const total = totalsByCurrency.get(transaction.currency) ?? {
                        currency: transaction.currency,
                        income: 0,
                        expense: 0,
                    };

                    if (transaction.operation === TransactionType.INCOME) {
                        total.income += transaction.amount;
                    } else {
                        total.expense += transaction.amount;
                    }

                    totalsByCurrency.set(transaction.currency, total);
                }

                const totals = [...totalsByCurrency.values()].toSorted((a, b) => a.currency.localeCompare(b.currency));

                return { issued, transactions: lineItems, totals };
            }),
    );
</script>

<Accordion>
    {#each dailyTransactions as daily}
        <AccordionItem header={daily.issued}>
            {#snippet body()}
                <div class="p-2">
                    <table class="w-full">
                        <thead>
                            <tr>
                                <th class="border-border-card border-b-1 py-1 pl-2 text-left text-xs uppercase">
                                    {_("activity.panel.category")}
                                </th>
                                <th class="border-border-card border-b-1 py-1 pr-2 text-right text-xs uppercase">
                                    {_("activity.panel.amount")}
                                </th>
                            </tr>
                        </thead>

                        <tbody>
                            {#each daily.transactions as transaction}
                                {@const transactionCategory = findCategory(transaction.categoryId)}
                                <tr class="h-14">
                                    <td class="border-border-card/50 border-b-1 pl-2 text-left">
                                        <div class="flex gap-2">
                                            <div class="flex items-center">
                                                <Badge
                                                    color={transactionCategory?.color ?? "#a8b7d1"}
                                                    icon={transactionCategory?.icon}>
                                                </Badge>
                                            </div>

                                            <div class="flex flex-col">
                                                <button
                                                    type="button"
                                                    class="text-left hover:cursor-pointer hover:underline"
                                                    onclick={() => onEdit(transaction)}>
                                                    {transactionCategory?.name ?? "No Category"}
                                                </button>

                                                <span class="max-w-[12rem] truncate text-left text-xs opacity-80">
                                                    {transaction.description}
                                                </span>
                                            </div>
                                        </div>
                                    </td>

                                    <td class="border-border-card/50 border-b-1 pr-2 text-right text-sm font-semibold">
                                        <Money
                                            operation={transaction.operation}
                                            currency={transaction.currency}
                                            amount={transaction.amount} />
                                    </td>
                                </tr>
                            {/each}
                        </tbody>

                        <tfoot>
                            <tr class="text-sm font-semibold">
                                <td class="pl-2 align-text-top">
                                    {_("activity.panel.summary")}
                                </td>
                                <td class="pr-2">
                                    {#each daily.totals as total}
                                        <div class="text-right">
                                            {#if total.income > 0}
                                                <Money
                                                    noColor
                                                    operation={TransactionType.INCOME}
                                                    currency={total.currency}
                                                    amount={total.income} />
                                            {/if}

                                            {#if total.expense > 0}
                                                <Money
                                                    noColor
                                                    operation={TransactionType.EXPENSE}
                                                    currency={total.currency}
                                                    amount={total.expense} />
                                            {/if}
                                        </div>
                                    {/each}
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            {/snippet}
        </AccordionItem>
    {/each}
</Accordion>
