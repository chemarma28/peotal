<script lang="ts">
    import { onMount } from "svelte";
    import ActivityPanel from "./ActivityPanel.svelte";
    import TransactionView from "./TransactionView.svelte";
    import api from "@ui/api";
    import type { Category, Transaction } from "@ui/types";
    import Empty from "@ui/components/Empty.svelte";
    import { showToast, ToastVariant } from "@ui/components/toasts";

    let transactionViewRef: TransactionView;

    let loading = $state(true);

    let transactions = $state<Transaction[]>([]);
    let categories = $state<Map<number, Category>>(new Map());

    async function loadTransactions(): Promise<Transaction[]> {
        const begin = new Date("2026-01-01");
        const end = new Date("2027-01-01");

        return await api.get<Transaction[]>(
            `transactions?begin=${begin.toIsoDate()}&end=${end.toIsoDate()}`,
        );
    }

    async function loadCategories(): Promise<Map<number, Category>> {
        let response = await api.get<Category[]>("categories");

        const categories = new Map<number, Category>();

        for (const category of response) {
            categories.set(category.id, category);
        }

        return categories;
    }

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

<div class="flex flex-col md:grid md:grid-cols-4">
    <div>
        <div>Category 1</div>
        <div>Category 2</div>
        <div>Category 3</div>
    </div>

    <div class="flex flex-col gap-2 md:col-span-3">
        <div class="flex justify-end">
            <button onclick={() => transactionViewRef.show()} class="primary">
                <i class="fa-solid fa-plus"></i>Add transaction</button>
        </div>

        {#if transactions.length && categories.size}
            <ActivityPanel {categories} {transactions} />
        {:else}
            <Empty {loading}></Empty>
        {/if}
    </div>
</div>

<TransactionView
    bind:this={transactionViewRef}
    onSave={onSaveTransaction}
    categories={[...categories.values()]}></TransactionView>
