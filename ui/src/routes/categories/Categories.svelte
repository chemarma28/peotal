<script lang="ts">
    import Badge from "@ui/components/Badge.svelte";
    import type { CategoryIcon } from "@ui/categories/icons";
    import Page from "@ui/layout/Page.svelte";
    import { TransactionType, type Category } from "@ui/types";

    type Props = {
        categories: Category[];
    };

    const starter = (id: number, name: string, color: string, icon: CategoryIcon): Category => ({
        id,
        name,
        color,
        icon,
        operation: TransactionType.EXPENSE,
        ordinal: id - 1,
    });

    let categories = $state<Category[]>([
        starter(1, "Groceries", "#7cb342", "groceries"),
        starter(2, "Transport", "#5c6bc0", "transport"),
        starter(3, "Restaurant", "#ef6c00", "restaurant"),
        starter(4, "Entertainment", "#ab47bc", "entertainment"),
        starter(5, "Internet", "#29b6f6", "internet"),
        starter(6, "Mobile", "#26a69a", "mobile"),
        starter(7, "Household", "#8d6e63", "household"),
    ]);

    let dragged = $state<Category>();
    let dropIndex = $state<number>();
</script>

<Page width="narrow">
    <div class="flex flex-col">
        {#each categories as category, i (category.id)}
            <div class="flex h-2 items-center">
                <div class="h-0.5 w-full rounded {dropIndex === i ? 'bg-border-focus' : 'bg-transparent'}"></div>
            </div>

            <div
                class="border-border-card bg-bg-card rounded-lg border-1 p-1 {category === dragged
                    ? 'opacity-50'
                    : ''}">
                <Badge color={category.color} icon={category.icon}>
                    {category.name}
                </Badge>
            </div>
        {/each}

        <div class="flex h-2 items-center">
            <div
                class="h-0.5 w-full rounded {dropIndex === categories.length
                    ? 'bg-[var(--color-border-focus)]'
                    : 'bg-transparent'}">
            </div>
        </div>
    </div>
</Page>
