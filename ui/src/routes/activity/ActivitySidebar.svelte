<script lang="ts">
    import Badge from "@ui/components/Badge.svelte";
    import { formatMoney } from "@ui/currencies";
    import { type CategorySummary, Currency, type Category } from "@ui/types";

    type Props = {
        categories: Category[];
        summary: CategorySummary[];
    };

    let { categories, summary }: Props = $props();

    const summaries: Map<number | undefined, CategorySummary[]> = $derived(
        summary.reduce((s, it) => {
            s.set(it.categoryId, s.get(it.categoryId) ?? []);
            s.get(it.categoryId)?.push(it);

            return s;
        }, new Map<number | undefined, CategorySummary[]>()),
    );
</script>

<div class="flex flex-col gap-3">
    <div class="flex flex-col gap-2">
        {#each categories as category (category.id)}
            {@const s = summaries.get(category.id) ?? []}
            <div class="border-border-card bg-bg-card flex items-center justify-between gap-2 rounded-lg border-1 p-2">
                <Badge color={category.color} icon={category.icon}>{category.name}</Badge>

                {#each s as sum}
                    <div class="text-right text-sm">
                        <div class="font-semibold">{formatMoney(sum?.amount ?? 0, sum?.currency ?? Currency.USD)}</div>
                        <div class="opacity-60">0.0%</div>
                    </div>
                {/each}
            </div>
        {/each}
    </div>
</div>
