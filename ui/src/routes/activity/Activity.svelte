<script lang="ts">
    import { onMount, getContext } from "svelte";
    import { faPlus } from "@fortawesome/free-solid-svg-icons";
    import ActivityPanel from "./ActivityPanel.svelte";
    import ActivitySidebar from "./ActivitySidebar.svelte";
    import TransactionView from "./TransactionView.svelte";
    import api from "@ui/api";
    import Icon from "@ui/components/Icon.svelte";
    import { type Category, type CategorySummaries, type Transaction, TransactionType } from "@ui/types";
    import Empty from "@ui/components/Empty.svelte";
    import { showToast, ToastVariant } from "@ui/components/toasts";
    import { HEADER_ACTION, type HeaderActionStore } from "@ui/layout/headerAction";
    import { _ } from "@ui/i18n";
    import Page from "@ui/layout/Page.svelte";
    import MonthPicker from "@ui/components/MonthPicker.svelte";
    import ToggleGroup from "@ui/forms/ToggleGroup.svelte";
    import { type SelectOption } from "@ui/forms/types";

    let transactionViewRef: TransactionView;

    let categoriesLoading = $state(true);
    let periodLoading = $state(true);
    const loading = $derived(categoriesLoading || periodLoading);

    let transactions = $state<Transaction[]>([]);
    let categories = $state<Map<number, Category>>(new Map());
    let summaries = $state<CategorySummaries[]>([]);

    const now = new Date();
    let begin = $state(new Date(now.getFullYear(), now.getMonth(), 1));
    const end = $derived(new Date(begin.getFullYear(), begin.getMonth() + 1, 0));

    let periodRequest = 0;

    const headerAction = getContext<HeaderActionStore>(HEADER_ACTION);

    $effect(() => {
        headerAction.set({
            icon: faPlus,
            label: _("activity.button.add.transaction"),
            onClick: () => transactionViewRef.show(),
        });

        return () => headerAction.set(undefined);
    });

    async function loadTransactions(from: Date, to: Date): Promise<Transaction[]> {
        return await api.get<Transaction[]>(`transactions?begin=${from.toIsoDate()}&end=${to.toIsoDate()}`);
    }

    async function loadCategories(): Promise<Map<number, Category>> {
        let response = await api.get<Category[]>("categories");

        const categories = new Map<number, Category>();

        for (const category of response) {
            if (category.id === undefined) continue;

            categories.set(category.id, category);
        }

        return categories;
    }

    async function loadCategorySummaries(from: Date, to: Date): Promise<CategorySummaries[]> {
        return await api.get<CategorySummaries[]>(`categories/summary?begin=${from.toIsoDate()}&end=${to.toIsoDate()}`);
    }

    async function loadPeriod(from: Date, to: Date) {
        const request = ++periodRequest;
        periodLoading = true;

        try {
            const [loadedTransactions, loadedSummary] = await Promise.all([
                loadTransactions(from, to),
                loadCategorySummaries(from, to),
            ]);

            if (request !== periodRequest) return;

            transactions = loadedTransactions;
            summaries = loadedSummary;
        } finally {
            if (request === periodRequest) {
                periodLoading = false;
            }
        }
    }

    $effect(() => {
        void loadPeriod(begin, end);
    });

    const filterOptions: SelectOption<TransactionType | undefined>[] = [
        { name: _("types.transactions.expense"), record: TransactionType.EXPENSE },
        { name: _("types.transactions.income"), record: TransactionType.INCOME },
        { name: _("activity.filter.all"), record: undefined },
    ];

    let filter = $state<SelectOption<TransactionType | undefined>>(filterOptions[2]);
    let visibleCategories = $derived<Category[]>(
        [...categories.values()].filter((it) => !filter.record || it.operation === filter.record),
    );
    let visibleTransactions = $derived(transactions.filter((it) => !filter.record || it.operation === filter.record));

    async function onSaveTransaction(transaction: Transaction) {
        showToast(ToastVariant.SUCCESS, _("activity.toast.transactionSaved"));
        await loadPeriod(begin, end);
    }

    onMount(async () => {
        try {
            categories = await loadCategories();
        } finally {
            categoriesLoading = false;
        }
    });
</script>

{#snippet pageActions()}
    <div class="hidden items-center gap-2 lg:flex">
        <MonthPicker bind:begin></MonthPicker>
    </div>
{/snippet}

<Page title={_("activity.title")} description={_("activity.description")} actions={pageActions}>
    <div class="mb-4 lg:hidden">
        <MonthPicker bind:begin stretch></MonthPicker>
    </div>

    <div class="grid grid-cols-1 gap-12 lg:grid-cols-[22rem_minmax(0,1fr)]">
        <aside class="lg:self-start">
            <div class="min-h-12">
                <ToggleGroup styleClass="bg-bg-card" bind:chosen={filter} options={filterOptions}></ToggleGroup>
            </div>

            {#if visibleCategories.length}
                <ActivitySidebar categories={visibleCategories} {summaries} />
            {:else}
                <Empty
                    {loading}
                    header={_("activity.empty.categories.header")}
                    description={_("activity.empty.categories.description")} />
            {/if}
        </aside>

        <section>
            <div class="mx-auto w-full max-w-5xl">
                <div class="hidden min-h-12 lg:flex lg:justify-end">
                    <div>
                        <button onclick={() => transactionViewRef.show()} class="primary inline-flex items-center gap-1">
                            <Icon icon={faPlus} />{_("activity.button.add.transaction")}
                        </button>
                    </div>
                </div>

                {#if !visibleCategories.length}
                    <Empty
                        {loading}
                        header={_("activity.empty.categories.header")}
                        description={_("activity.empty.categories.description")} />
                {:else if visibleTransactions.length}
                    <ActivityPanel
                        {categories}
                        transactions={visibleTransactions}
                        onEdit={(transaction) => transactionViewRef.show(transaction)} />
                {:else}
                    <Empty
                        {loading}
                        header={_("activity.empty.transactions.header")}
                        description={_("activity.empty.transactions.description")} />
                {/if}
            </div>
        </section>
    </div>
</Page>

<TransactionView bind:this={transactionViewRef} onSave={onSaveTransaction} categories={[...categories.values()]}
></TransactionView>
