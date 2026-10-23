<script lang="ts">
    import type { CategoryIcon } from "@ui/categories/icons";
    import Badge from "@ui/components/Badge.svelte";
    import Empty from "@ui/components/Empty.svelte";
    import Page from "@ui/layout/Page.svelte";
    import { _ } from "@ui/i18n";
    import ToggleGroup from "@ui/forms/ToggleGroup.svelte";
    import type { SelectOption } from "@ui/forms/types";
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

    let filter = $state<SelectOption<TransactionType | undefined>>({ name: "", record: undefined });

    let expenseCategories = $derived(categories.filter((it) => it.operation === TransactionType.EXPENSE));
    let incomeCategories = $derived(categories.filter((it) => it.operation === TransactionType.INCOME));
    
    let visibleCategories = $derived(
        filter.record === TransactionType.EXPENSE
            ? expenseCategories
            : filter.record === TransactionType.INCOME
              ? incomeCategories
              : categories,
    );

    let filterOptions = $derived<SelectOption<TransactionType | undefined>[]>([
        { name: _("categories.filter.all"), record: undefined, badge: categories.length },
        {
            name: _("categories.filter.expenses"),
            record: TransactionType.EXPENSE,
            badge: expenseCategories.length,
        },
        {
            name: _("categories.filter.income"),
            record: TransactionType.INCOME,
            badge: incomeCategories.length,
        },
    ]);

    let sectionTitle = $derived(
        filter.record === TransactionType.EXPENSE
            ? _("categories.section.expenses")
            : filter.record === TransactionType.INCOME
              ? _("categories.section.income")
              : _("categories.section.all"),
    );

    let emptyHeader = $derived(
        filter.record === TransactionType.EXPENSE
            ? _("categories.empty.expenses.header")
            : filter.record === TransactionType.INCOME
              ? _("categories.empty.income.header")
              : _("categories.empty.all.header"),
    );

    let emptyDescription = $derived(
        filter.record === TransactionType.EXPENSE
            ? _("categories.empty.expenses.description")
            : filter.record === TransactionType.INCOME
              ? _("categories.empty.income.description")
              : _("categories.empty.all.description"),
    );

    let draggableIndex = $state<number>();
    let dropIndex = $state<number>();

    let dragArmed = false;
    let armedCard: HTMLElement | undefined;

    function enableDrop(e: DragEvent) {
        e.preventDefault();
    }

    function releaseDrag() {
        if (armedCard) {
            armedCard.draggable = false;
            armedCard = undefined;
        }

        dragArmed = false;
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
        window.addEventListener("mouseup", releaseDrag);
    });

    onDestroy(() => {
        window.removeEventListener("dragover", enableDrop);
        window.removeEventListener("drop", onDrop);
        window.removeEventListener("mouseup", releaseDrag);
    });

    const dropZone = (index: number) => (element: HTMLElement) => {
        function dragEnter(e: DragEvent) {
            if (!dragArmed) return;

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
        element.draggable = false;

        function mouseDown(e: MouseEvent) {
            const handle = (e.target as HTMLElement | null)?.closest("[data-drag-handle]");

            if (!handle || !element.contains(handle)) return;

            element.draggable = true;
            armedCard = element;
            dragArmed = true;
        }

        function dragStart(e: DragEvent) {
            if (!dragArmed) {
                e.preventDefault();
                return;
            }

            draggableIndex = index;
        }

        function dragEnd(e: DragEvent) {
            releaseDrag();

            draggableIndex = undefined;
            dropIndex = undefined;
        }

        element.addEventListener("mousedown", mouseDown);
        element.addEventListener("dragstart", dragStart);
        element.addEventListener("dragend", dragEnd);

        return () => {
            element.draggable = false;
            element.removeEventListener("mousedown", mouseDown);
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

<Page width="narrow" title={_("routes.categories")} description={_("categories.description")}>
    <section>
        <div class="mb-4">
            <ToggleGroup styleClass="bg-bg-card" bind:chosen={filter} options={filterOptions}></ToggleGroup>
        </div>

        <div class="mb-2 flex items-baseline justify-between gap-2">
            <h2 id="categories-heading" class="text-lg font-semibold">
                {sectionTitle}
            </h2>
            <span class="text-sm opacity-60">{visibleCategories.length}</span>
        </div>

        {#if visibleCategories.length}
            <ul class="m-0 flex list-none flex-col p-0" role="list" aria-labelledby="categories-heading">
                <li aria-hidden="true">
                    {@render placeholder(0)}
                </li>

                {#each visibleCategories as category, index (category.id)}
                    <li animate:flip={{ duration: 200 }}>
                        <div
                            {@attach draggable(index)}
                            {@attach dropZone(index + 1)}
                            class="border-border-card bg-bg-card rounded-lg border-1 p-1">
                            {#if !filter.record}
                                <i
                                    data-drag-handle
                                    aria-hidden="true"
                                    class="fa-solid fa-grip inline-block rotate-90 px-1 select-none {draggableIndex ===
                                    index
                                        ? 'cursor-grabbing'
                                        : 'cursor-grab'}"></i>
                            {/if}
                            <Badge color={category.color} icon={category.icon}>
                                {category.name}
                            </Badge>
                        </div>

                        {@render placeholder(index + 1)}
                    </li>
                {/each}
            </ul>
        {:else}
            <Empty header={emptyHeader} description={emptyDescription} />
        {/if}
    </section>
</Page>
