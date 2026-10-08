<script lang="ts">
    import Badge from "@ui/components/Badge.svelte";
    import { type Category } from "@ui/types";

    type Props = {
        categories: Category[];
    };

    let { categories }: Props = $props();
</script>

<div class="flex flex-col gap-3">
    <div class="flex flex-col gap-2">
        {#each categories as category (category.id)}
            <div class="border-border-card bg-bg-card flex items-center justify-between gap-2 rounded-lg border-1 p-2">
                <Badge color={category.color} icon={category.icon}>{category.name}</Badge>

                <div class="text-right text-sm">
                    <div class="font-semibold">$0.00</div>
                    <div class="opacity-60">0.0%</div>
                </div>
            </div>
        {/each}
    </div>
</div>
