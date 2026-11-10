<script lang="ts">
    import api from "@ui/api";
    import { faGrip, faPenToSquare, faPlus } from "@fortawesome/free-solid-svg-icons";
    import Badge from "@ui/components/Badge.svelte";
    import Empty from "@ui/components/Empty.svelte";
    import Icon from "@ui/components/Icon.svelte";
    import { showToast, ToastVariant } from "@ui/components/toasts";
    import { DEFAULT_CATEGORIES } from "@ui/categories/defaults";
    import { _ } from "@ui/i18n";
    import ToggleGroup from "@ui/forms/ToggleGroup.svelte";
    import type { SelectOption } from "@ui/forms/types";
    import Page from "@ui/layout/Page.svelte";
    import { TransactionType, type Category } from "@ui/types";
    import { getContext, onDestroy, onMount } from "svelte";
    import { flip } from "svelte/animate";
    import CategoryView from "./CategoryView.svelte";
    import { HEADER_ACTION, type HeaderActionStore } from "@ui/layout/headerAction";

    let categoryViewRef: CategoryView;

    let categories = $state<Category[]>([]);
    let loading = $state(true);
    let loadError = $state(false);
    let creatingDefaults = $state(false);

    const NO_FILTER = { name: "", record: undefined };
    let filter = $state<SelectOption<TransactionType | undefined>>(NO_FILTER);

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

    const headerAction = getContext<HeaderActionStore>(HEADER_ACTION);

    $effect(() => {
        headerAction.set({
            icon: faPlus,
            label: _("categories.button.add.category"),
            onClick: () => categoryViewRef.show(undefined, filter.record ?? TransactionType.EXPENSE),
        });

        return () => headerAction.set(undefined);
    });

    async function loadCategories() {
        loading = true;
        loadError = false;

        try {
            categories = await api.get<Category[]>("categories");
        } catch (e) {
            loadError = true;
        } finally {
            loading = false;
        }
    }

    async function createDefaults() {
        creatingDefaults = true;

        try {
            categories = await api.post<Category[]>("category/batch", { items: DEFAULT_CATEGORIES });
            filter = { name: "", record: undefined };
            showToast(ToastVariant.SUCCESS, _("categories.toast.defaultsCreated"));
        } catch (e) {
            showToast(ToastVariant.ERROR, _("categories.toast.defaultsFailed"));
            await loadCategories();
        } finally {
            creatingDefaults = false;
        }
    }

    function onCategorySaved(saved: Category) {
        categories = categories.some((it) => it.id === saved.id)
            ? categories.map((it) => (it.id === saved.id ? saved : it))
            : [...categories, saved];
        filter = { name: "", record: saved.operation };
    }

    function onCategoryDeleted(deleted: Category) {
        categories = categories.filter((it) => it.id !== deleted.id);

        if (categories.length === 0) {
            filter = NO_FILTER;
        }
    }

    async function persistOrder() {
        const items = categories.filter((it) => it.id !== undefined).map((it, ordinal) => ({ id: it.id, ordinal }));

        try {
            categories = await api.put<Category[]>("category/order", { items });
        } catch (e) {
            showToast(ToastVariant.ERROR, _("categories.toast.orderFailed"));
            await loadCategories();
        }
    }

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
        if (filter.record || dropIndex == undefined || draggableIndex == undefined) return;

        const shallow = [...categories];
        const from = draggableIndex;
        const to = dropIndex;
        const position = from < to ? to - 1 : to;

        const [moved] = shallow.splice(from, 1);
        shallow.splice(position, 0, moved);

        categories = [...shallow];
        void persistOrder();
    }

    onMount(() => {
        loadCategories();
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
    {#if loading}
        <Empty loading />
    {:else if loadError}
        <Empty
            header={_("categories.empty.loadError.header")}
            description={_("categories.empty.loadError.description")}
            actionLabel={_("categories.action.retry")}
            onAction={loadCategories} />
    {:else}
        <section>
            <div class="mb-4">
                <ToggleGroup styleClass="bg-bg-card" bind:chosen={filter} options={filterOptions}></ToggleGroup>
            </div>

            <div class="mb-2 flex items-center justify-between gap-2">
                <h2 id="categories-heading" class="text-lg font-semibold">
                    {filter.record === TransactionType.EXPENSE
                        ? _("categories.section.expenses")
                        : filter.record === TransactionType.INCOME
                          ? _("categories.section.income")
                          : _("categories.section.all")}
                </h2>

                <div class="hidden lg:block">
                    <button
                        onclick={() => categoryViewRef.show(undefined, filter.record ?? TransactionType.EXPENSE)}
                        class="primary inline-flex items-center gap-1">
                        <Icon icon={faPlus} />{_("categories.button.add.category")}
                    </button>
                </div>
            </div>

            {#if categories.length === 0}
                <Empty
                    header={emptyHeader}
                    description={emptyDescription}
                    actionLabel={_("categories.action.createDefaults")}
                    onAction={createDefaults}
                    actionDisabled={creatingDefaults} />
            {:else if visibleCategories.length}
                <ul class="m-0 flex list-none flex-col p-0" role="list" aria-labelledby="categories-heading">
                    <li aria-hidden="true">
                        {@render placeholder(0)}
                    </li>

                    {#each visibleCategories as category, index (category.id)}
                        <li animate:flip={{ duration: 200 }}>
                            <div
                                {@attach draggable(index)}
                                {@attach dropZone(index + 1)}
                                class="border-border-card bg-bg-card flex items-center justify-between rounded-lg border-1 p-1">
                                <div class="flex items-center">
                                    {#if !filter.record}
                                        <Icon
                                            data-drag-handle
                                            icon={faGrip}
                                            class="mx-1 size-4 shrink-0 rotate-90 select-none {draggableIndex === index
                                                ? 'cursor-grabbing'
                                                : 'cursor-grab'}" />
                                    {/if}
                                    <Badge color={category.color} icon={category.icon}>
                                        {category.name}
                                    </Badge>
                                </div>

                                <div>
                                    <button
                                        type="button"
                                        onclick={() => categoryViewRef?.show(category)}
                                        class="icon"
                                        aria-label={_("categories.action.edit")}
                                        title={_("categories.action.edit")}>
                                        <Icon icon={faPenToSquare} />
                                    </button>
                                </div>
                            </div>

                            {@render placeholder(index + 1)}
                        </li>
                    {/each}
                </ul>
            {:else}
                <Empty header={emptyHeader} description={emptyDescription} />
            {/if}
        </section>
    {/if}
</Page>

<CategoryView bind:this={categoryViewRef} onSave={onCategorySaved} onDelete={onCategoryDeleted}></CategoryView>
