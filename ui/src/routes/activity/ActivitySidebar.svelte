<script lang="ts">
    import Badge from "@ui/components/Badge.svelte";
    import { formatMoney } from "@ui/currencies";
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
                        <div class="flex gap-2">
                            {#each stat?.sums as sum}
                                <div class="font-semibold">
                                    {formatMoney(sum?.amount ?? 0, sum?.currency ?? Currency.USD)}
                                </div>
                            {:else}
                                <div class="font-semibold">{formatMoney(0, Currency.USD)}</div>
                            {/each}
                        </div>

                        <div class="text-right opacity-60">0.0%</div>
                    </div>
                </div>
            {/if}
        {/each}
    </div>
</div>
