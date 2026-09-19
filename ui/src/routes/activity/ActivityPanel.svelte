<script lang="ts">
    import Accordion from "@ui/components/Accordion.svelte";
    import AccordionItem from "@ui/components/AccordionItem.svelte";
    import Rounded from "@ui/components/Rounded.svelte";
    import type { Category, Transaction } from "@ui/types";
    import type { DailyTransactions } from "./types";

    type Props = {
        categories: Map<number, Category>;
        transactions: Transaction[];
    };

    let { categories, transactions = $bindable<Transaction[]>() }: Props =
        $props();
    let dailyTransactions = $state<DailyTransactions[]>([]);

    const category = (categoryId?: number) => {
        categoryId && categories.has(categoryId)
            ? categories.get(categoryId)!.name
            : "No Category";
    };

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
                                <th
                                    class="py-1 pl-2 border-b-1 border-border-card text-left"
                                >
                                    Description
                                </th>
                                <th
                                    class="py-1 w-12 border-b-1 border-border-card"
                                >
                                    Category
                                </th>
                                <th
                                    class="py-1 pr-2 border-b-1 border-border-card text-right"
                                >
                                    Amount
                                </th>
                            </tr>
                        </thead>

                        <tbody>
                            {#each daily.transactions as transaction}
                                <tr class="">
                                    <td
                                        class="border-b-1 pl-2 py-2 border-border-card/50"
                                    >
                                        {transaction.description}
                                    </td>

                                    <td
                                        class="border-b-1 py-2 border-border-card/50 text-center"
                                    >
                                        <Rounded color="lightblue">
                                            {category(transaction.categoryId)}
                                        </Rounded>
                                    </td>

                                    <td
                                        class="border-b-1 py-2 pr-2 border-border-card/50 text-right"
                                    >
                                        ${transaction.amount}
                                    </td>
                                </tr>
                            {/each}
                        </tbody>

                        <tfoot>
                            <tr class="font-semibold">
                                <td class="pl-2">Summary</td>
                                <td></td>
                                <td class="pr-2 text-right">$120</td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            {/snippet}
        </AccordionItem>
    {/each}
</Accordion>
