<script lang="ts">
    import Badge from "@ui/components/Badge.svelte";
    import { formatMoney, formatNumber } from "@ui/currencies";
    import { Currency, type Category, type CategorySummaries } from "@ui/types";

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
</script>

<div class="flex flex-col gap-3">
    <div class="flex flex-col gap-2">
        {#each categories as category (category.id)}
            {#if category.id}
                {@const stat = stats.get(category.id)}
                <div
                    class="border-border-card bg-bg-card flex items-center justify-between gap-2 rounded-lg border-1 p-2">
                    <Badge color={category.color} icon={category.icon}>{category.name}</Badge>

                    <div class="text-sm">
                        <div class="flex justify-end gap-4 font-semibold">
                            {#each stat?.sums as sum}
                                <div>
                                    {formatMoney(sum?.amount ?? 0, sum?.currency ?? Currency.USD)}
                                </div>
                            {:else}
                                <div>{formatMoney(0, Currency.USD)}</div>
                            {/each}
                        </div>

                        <div class="text-right opacity-60">{formatNumber(100 * (stat?.weight ?? 0))}%</div>
                    </div>
                </div>
            {/if}
        {/each}
    </div>
</div>
