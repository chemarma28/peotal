<script lang="ts">
    import { _, _transactionType } from "@ui/i18n";
    import api from "@ui/api";
    import CurrencyField from "@ui/forms/CurrencyField.svelte";
    import DateField from "@ui/forms/DateField.svelte";
    import InputField from "@ui/forms/InputField.svelte";
    import SelectField from "@ui/forms/SelectField.svelte";
    import ToggleGroup from "@ui/forms/ToggleGroup.svelte";
    import type { SelectOption } from "@ui/forms/types";
    import Dialog from "@ui/components/Dialog.svelte";
    import type { Category, Transaction } from "@ui/types";
    import { Currency, TransactionType } from "@ui/types";

    type Props = {
        categories: Category[];
        onSave: (transaction: Transaction) => void;
    };

    let { onSave, categories }: Props = $props();

    type TransactionDraft = Omit<Transaction, "categoryId" | "description"> & {
        categoryId?: number;
        description: string;
    };

    const DEFAULT_OPERATION = TransactionType.EXPENSE;

    const operationOptions: SelectOption<TransactionType>[] = Object.values(TransactionType).map((record) => ({
        name: _transactionType(record),
        record,
    }));

    let operation = $state<SelectOption<TransactionType> | undefined>(
        operationOptions.find((o) => o.record === DEFAULT_OPERATION),
    );

    const categoryOptions = $derived<SelectOption<Category>[]>(
        categories
            .filter((record) => !operation || record.operation === operation.record)
            .map((record) => ({ name: record.name, record })),
    );

    const empty = (): TransactionDraft => ({
        operation: DEFAULT_OPERATION,
        issued: new Date().toIsoDate(),
        amount: 0,
        currency: Currency.USD,
        description: "",
    });

    let dialogRef = $state<Dialog>();

    let transaction = $state<TransactionDraft>(empty());
    let category = $state<SelectOption<Category> | undefined>();

    let isNew = $derived(transaction.id === undefined);
    let hasCategories = $derived(categoryOptions.length > 0);

    $effect(() => {
        if (category && operation && category.record.operation !== operation.record) {
            category = undefined;
        }
    });

    export function show(it?: Transaction) {
        transaction = it ? { ...it, description: it.description ?? "" } : empty();
        operation = operationOptions.find((o) => o.record === transaction.operation);

        const selected = categories.find((o) => o.id === transaction.categoryId);
        category = selected ? { name: selected.name, record: selected } : undefined;

        dialogRef?.show();
    }

    export function close() {
        dialogRef?.close();
    }

    async function save() {
        const categoryId = category?.record.id;
        if (categoryId === undefined || !operation) return;

        const payload: Transaction = {
            ...transaction,
            operation: operation.record,
            categoryId,
            description: transaction.description?.trim() || undefined,
        };

        const saved = isNew
            ? await api.post<Transaction>("transaction", payload)
            : await api.put<Transaction>("transaction", payload);

        onSave(saved);
        close();
    }
</script>

<Dialog bind:this={dialogRef} title={isNew ? _("transaction.title.new") : _("transaction.title.edit")} onsubmit={save}>
    <ToggleGroup bind:chosen={operation} options={operationOptions}></ToggleGroup>

    <DateField id="date" label={_("transaction.labels.issued")} bind:value={transaction.issued} />

    <SelectField
        id="category"
        label={_("transaction.labels.category")}
        required
        placeholder={_("transaction.placeholders.category")}
        options={categoryOptions}
        key={(o) => o.record.id}
        bind:chosen={category}
        validator={(value) => (value ? "" : _("transaction.errors.category"))}>
    </SelectField>

    {#if !hasCategories}
        <p class="text-sm text-red-700">{_("transaction.errors.noCategory")}</p>
    {/if}

    <CurrencyField
        amountId="amount"
        currencyId="currency"
        label={_("transaction.labels.currency")}
        autofocus
        required
        bind:amount={transaction.amount}
        bind:currency={transaction.currency}
        validator={(value) => {
            if ((value as number) <= 0) return _("transaction.errors.currency");
        }}>
    </CurrencyField>

    <InputField
        id="description"
        label={_("transaction.labels.description")}
        placeholder={_("transaction.placeholders.description")}
        bind:value={transaction.description}>
    </InputField>

    {#snippet footer()}
        <button class="primary w-full" type="submit" disabled={!hasCategories}>
            {_("transaction.save")}
        </button>
    {/snippet}
</Dialog>
