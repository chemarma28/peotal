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

    type TransactionDraft = Omit<Transaction, "categoryId"> & { categoryId?: number };

    const DEFAULT_OPERATION = TransactionType.EXPENSE;

    const operationOptions: SelectOption<TransactionType>[] = Object.values(TransactionType).map((record) => ({
        name: _transactionType(record),
        record,
    }));

    const categoryOptions = $derived<SelectOption<Category>[]>(
        categories.map((record) => ({ name: record.name, record })),
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
    let operation = $state<SelectOption<TransactionType> | undefined>(
        operationOptions.find((o) => o.record === DEFAULT_OPERATION),
    );
    let category = $state<SelectOption<Category> | undefined>();

    export function show(it?: Transaction) {
        transaction = it ? { ...it } : empty();
        operation = operationOptions.find((o) => o.record === transaction.operation);
        category = categoryOptions.find((o) => o.record.id === transaction.categoryId);

        dialogRef?.show();
    }

    export function close() {
        dialogRef?.close();
    }

    async function save() {
        const categoryId = category?.record.id;
        if (categoryId === undefined) return;

        const payload: Transaction = {
            ...transaction,
            operation: operation!.record,
            categoryId,
        };

        onSave(await api.post<Transaction>("transaction", payload));
        close();
    }
</script>

<Dialog bind:this={dialogRef} title={_("transaction.title.new")} onsubmit={save}>
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

    <CurrencyField
        amountId="amount"
        currencyId="currency"
        label={_("transaction.labels.currency")}
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
        required
        bind:value={transaction.description}>
    </InputField>

    {#snippet footer()}
        <button class="primary w-full" type="submit">{_("transaction.save")}</button>
    {/snippet}
</Dialog>
