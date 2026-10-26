<script lang="ts">
    import { _ } from "@ui/i18n";
    import Accordion from "@ui/components/Accordion.svelte";
    import AccordionItem from "@ui/components/AccordionItem.svelte";
    import Badge from "@ui/components/Badge.svelte";
    import type { Category, Transaction } from "@ui/types";
    import type { DailyTransactions } from "./types";

    type Props = {
        categories: Map<number, Category>;
        transactions: Transaction[];
        onEdit: (transaction: Transaction) => void;
    };

    let { categories, transactions = $bindable<Transaction[]>(), onEdit }: Props = $props();
    let dailyTransactions = $state<DailyTransactions[]>([]);

    const findCategory = (categoryId?: number) => (categoryId ? categories.get(categoryId) : undefined);

    $effect(() => {
        dailyTransactions = transactions
            .groupingBy((transaction) => transaction.issued)
            .map((bucket) => ({
                issued: bucket.by.toString(),
                transactions: bucket.entries,
            }));
    });
</script>

<Accordion>
    {#each dailyTransactions as daily}
        <AccordionItem header={daily.issued}>
            {#snippet body()}
                <div class="p-2">
                    <table class="w-full">
                        <thead>
                            <tr>
                                <th class="border-border-card border-b-1 py-1 pl-2 text-left">
                                    {_("activity.panel.category")}
                                </th>
                                <th class="border-border-card border-b-1 py-1 pr-2 text-right">
                                    {_("activity.panel.amount")}
                                </th>
                            </tr>
                        </thead>

                        <tbody>
                            {#each daily.transactions as transaction}
                                {@const transactionCategory = findCategory(transaction.categoryId)}
                                <tr>
                                    <td class="border-border-card/50 border-b-1 py-2 pl-2 text-left">
                                        <button
                                            type="button"
                                            class="cursor-pointer rounded-full"
                                            aria-label={_("activity.action.editTransaction")}
                                            title={_("activity.action.editTransaction")}
                                            onclick={() => onEdit(transaction)}>
                                            <Badge
                                                color={transactionCategory?.color ?? "#a8b7d1"}
                                                icon={transactionCategory?.icon}>
                                                {transactionCategory?.name ?? "No Category"}
                                            </Badge>
                                        </button>
                                    </td>

                                    <td class="border-border-card/50 border-b-1 py-2 pr-2 text-right">
                                        ${transaction.amount}
                                    </td>
                                </tr>
                            {/each}
                        </tbody>

                        <tfoot>
                            <tr class="font-semibold">
                                <td class="pl-2">
                                    {_("activity.panel.summary")}
                                </td>
                                <td class="pr-2 text-right">$120</td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            {/snippet}
        </AccordionItem>
    {/each}
</Accordion>
