<script lang="ts">
    import { onMount, getContext } from "svelte";
    import ActivityPanel from "./ActivityPanel.svelte";
    import ActivitySidebar from "./ActivitySidebar.svelte";
    import ActivitySummary from "./ActivitySummary.svelte";
    import TransactionView from "./TransactionView.svelte";
    import api from "@ui/api";
    import { type Category, type Transaction, TransactionType } from "@ui/types";
    import Empty from "@ui/components/Empty.svelte";
    import { showToast, ToastVariant } from "@ui/components/toasts";
    import { HEADER_ACTION, type HeaderActionStore } from "@ui/layout/headerAction";
    import { _ } from "@ui/i18n";
    import MonthSpinner from "@ui/components/MonthSpinner.svelte";
    import ToggleGroup from "@ui/forms/ToggleGroup.svelte";
    import { type SelectOption } from "@ui/forms/types";

    let transactionViewRef: TransactionView;

    let loading = $state(true);

    let transactions = $state<Transaction[]>([]);
    let categories = $state<Map<number, Category>>(new Map());

    const now = new Date();
    let begin = $state(new Date(now.getFullYear(), now.getMonth(), 1));

    const headerAction = getContext<HeaderActionStore>(HEADER_ACTION);

    $effect(() => {
        headerAction.set({
            icon: "fa-solid fa-plus",
            label: _("activity.button.add.transaction"),
            onClick: () => transactionViewRef.show(),
        });

        return () => headerAction.set(undefined);
    });

    async function loadTransactions(): Promise<Transaction[]> {
        const begin = new Date("2026-01-01");
        const end = new Date("2027-01-01");

        return await api.get<Transaction[]>(`transactions?begin=${begin.toIsoDate()}&end=${end.toIsoDate()}`);
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

    const filterOptions: SelectOption<TransactionType | undefined>[] = [
        { name: _("types.transactions.expense"), record: TransactionType.EXPENSE },
        { name: _("types.transactions.income"), record: TransactionType.INCOME },
        { name: _("activity.filter.all"), record: undefined },
    ];

    let filter = $state<SelectOption<TransactionType | undefined>>(filterOptions[2]);

    async function onSaveTransaction(transaction: Transaction) {
        showToast(ToastVariant.SUCCESS, "Transaction was saved");
        transactions = await loadTransactions();
    }

    onMount(async () => {
        try {
            categories = await loadCategories();
            transactions = await loadTransactions();
        } finally {
            loading = false;
        }
    });
</script>

<div class="mb-4 lg:hidden">
    <MonthSpinner bind:begin stretch></MonthSpinner>
</div>

<div class="grid grid-cols-1 gap-8 lg:grid-cols-[18rem_minmax(0,1fr)]">
    <aside class="lg:sticky lg:top-4 lg:self-start">
        <div class="mb-2">
            <ToggleGroup styleClass="bg-bg-card" bind:chosen={filter} options={filterOptions}></ToggleGroup>
        </div>

        {#if transactions.length && categories.size}
            <ActivitySidebar categories={[...categories.values()]} />
        {:else}
            <Empty {loading}></Empty>
        {/if}
    </aside>

    <section>
        <div class="mx-auto flex w-full max-w-5xl flex-col gap-4">
            <div class="flex items-center justify-between gap-2">
                <div class="hidden lg:block">
                    <MonthSpinner bind:begin></MonthSpinner>
                </div>

                <div class="hidden lg:block">
                    <button onclick={() => transactionViewRef.show()} class="primary">
                        <i class="fa-solid fa-plus"></i>{_("activity.button.add.transaction")}</button>
                </div>
            </div>

            <ActivitySummary />

            {#if transactions.length && categories.size}
                <ActivityPanel {categories} {transactions} />
            {:else}
                <Empty {loading}></Empty>
            {/if}
        </div>
    </section>
</div>

<TransactionView bind:this={transactionViewRef} onSave={onSaveTransaction} categories={[...categories.values()]}
></TransactionView>
