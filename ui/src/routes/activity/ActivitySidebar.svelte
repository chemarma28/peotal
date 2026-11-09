<script lang="ts">
    import Badge from "@ui/components/Badge.svelte";
    import Money from "@ui/components/Money.svelte";
    import { formatNumber } from "@ui/currencies";
    import { TransactionType, type Category, type CategorySummaries } from "@ui/types";

    type Props = {
        categories: Category[];
        summaries: CategorySummaries[];
    };

    let { categories, summaries }: Props = $props();

    const stats: Map<number, CategorySummaries> = $derived(
        summaries.reduce((s, it) => {
            s.set(it.categoryId, it);

            return s;
        }, new Map<number, CategorySummaries>()),
    );

    const sortedCategories = $derived(
        categories.toSorted((a, b) => {
            const weightA = a.id ? (stats.get(a.id)?.weight ?? 0) : 0;
            const weightB = b.id ? (stats.get(b.id)?.weight ?? 0) : 0;

            return weightB - weightA || a.ordinal - b.ordinal;
        }),
    );
</script>

<div class="flex flex-col gap-3">
    <div class="flex flex-col gap-2">
        {#each sortedCategories as category (category.id)}
            {#if category.id}
                {@const stat = stats.get(category.id)}
                <div
                    class="border-border-card bg-bg-card flex items-center justify-between gap-3 rounded-lg border-1 p-2">
                    <div class="min-w-0 overflow-hidden" title={category.name}>
                        <Badge color={category.color} icon={category.icon}>{category.name}</Badge>
                    </div>

                    <div class="shrink-0 text-sm">
                        <div class="flex items-center justify-end gap-2">
                            {#if stat?.sums.length}
                                <span
                                    class="size-2 shrink-0 rounded-full {category.operation === TransactionType.EXPENSE
                                        ? 'bg-expense'
                                        : 'bg-income'}"
                                    aria-hidden="true"></span>
                            {/if}

                            <div class="flex justify-end gap-4 font-semibold tabular-nums">
                                {#each stat?.sums as sum}
                                    <div class="whitespace-nowrap">
                                        <Money
                                            noColor
                                            operation={sum.operation}
                                            currency={sum.currency}
                                            amount={sum.amount} />
                                    </div>
                                {:else}
                                    <div class="whitespace-nowrap">{formatNumber(0)}</div>
                                {/each}
                            </div>
                        </div>

                        <div class="text-right tabular-nums opacity-60">
                            {formatNumber(100 * (stat?.weight ?? 0))}%
                        </div>
                    </div>
                </div>
            {/if}
        {/each}
    </div>
</div>
