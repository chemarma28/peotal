<script lang="ts">
    import type { CategoryIcon } from "@ui/categories/icons";
    import Badge from "@ui/components/Badge.svelte";
    import Page from "@ui/layout/Page.svelte";
    import { TransactionType, type Category } from "@ui/types";
    import { onDestroy, onMount } from "svelte";
    import { flip } from "svelte/animate";

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

    let draggableIndex = $state<number>();
    let dropIndex = $state<number>();

    function enableDrop(e: DragEvent) {
        e.preventDefault();
    }

    function onDrop(e: DragEvent) {
        if (dropIndex == undefined || draggableIndex == undefined) return;

        const shallow = [...categories];
        const from = draggableIndex;
        const to = dropIndex;
        const position = from < to ? to - 1 : to;

        const [moved] = shallow.splice(from, 1);
        shallow.splice(position, 0, moved);

        categories = [...shallow];
    }

    onMount(() => {
        window.addEventListener("dragover", enableDrop);
        window.addEventListener("drop", onDrop);
    });

    onDestroy(() => {
        window.removeEventListener("dragover", enableDrop);
        window.removeEventListener("drop", onDrop);
    });

    const dropZone = (index: number) => (element: HTMLElement) => {
        function dragEnter(e: DragEvent) {
            dropIndex = index;
        }

        element.addEventListener("dragenter", dragEnter);
        element.addEventListener("dragover", enableDrop);

        return () => {
            element.removeEventListener("dragenter", dragEnter);
            element.removeEventListener("dragover", enableDrop);
        };
    };

    const draggable = (index: number) => (element: HTMLElement) => {
        element.draggable = true;

        function dragStart(e: DragEvent) {
            draggableIndex = index;
        }

        function dragEnd(e: DragEvent) {
            draggableIndex = undefined;
            dropIndex = undefined;
        }

        element.addEventListener("dragstart", dragStart);
        element.addEventListener("dragend", dragEnd);

        return () => {
            element.removeEventListener("dragstart", dragStart);
            element.removeEventListener("dragend", dragEnd);
        };
    };
</script>

{#snippet placeholder(index: number)}
    <div class="flex h-2 w-full items-center" {@attach dropZone(index)}>
        <div
            class="border-border-focus h-0.5 w-full rounded-lg border-1 {index === dropIndex
                ? 'opacity-100'
                : 'opacity-0'}">
        </div>
    </div>
{/snippet}

<Page width="narrow">
    <div class="flex flex-col">
        {@render placeholder(0)}

        {#each categories as category, index (category.id)}
            <div animate:flip={{ duration: 200 }}>
                <div
                    {@attach draggable(index)}
                    {@attach dropZone(index + 1)}
                    class="border-border-card bg-bg-card rounded-lg border-1 p-1">
                    <i class="fa-solid fa-grip rotate-90 cursor-move"></i>
                    <Badge color={category.color} icon={category.icon}>
                        {category.name}
                    </Badge>
                </div>

                {@render placeholder(index + 1)}
            </div>
        {/each}
    </div>
</Page>
