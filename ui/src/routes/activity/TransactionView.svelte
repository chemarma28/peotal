<script lang="ts">
    import api from "@ui/api";
    import CurrencyField from "@ui/forms/CurrencyField.svelte";
    import DateField from "@ui/forms/DateField.svelte";
    import InputField from "@ui/forms/InputField.svelte";
    import SelectField from "@ui/forms/SelectField.svelte";
    import ToggleGroup from "@ui/forms/ToggleGroup.svelte";
    import type { SelectOption } from "@ui/forms/types";
    import type { Category, Transaction } from "@ui/types";
    import { Currency, TransactionType } from "@ui/types";

    type Props = {
        categories: Category[];
        onSave: (transactions: Transaction) => void;
    };

    let { onSave, categories }: Props = $props();

    let category = $state<Category>();

    const initial = (): Transaction => ({
        operation: TransactionType.INCOME,
        categoryId: category?.id,
        issued: new Date().toIsoDate(),
        amount: 0,
        currency: Currency.USD,
        description: "",
    });

    let dialogRef = $state<HTMLDialogElement>();
    let transaction: Transaction = $state<Transaction>(initial());

    export function show(it?: Transaction) {
        category = undefined;
        transaction = it ? { ...it } : initial();

        dialogRef?.showModal();
    }

    export function close() {
        dialogRef?.close();
    }

    async function save(e: SubmitEvent) {
        e.preventDefault();

        transaction.categoryId = category?.id ?? 1;
        const response = await api.post<Transaction>(
            "transaction",
            transaction,
        );
        onSave(response);
        close();
    }
</script>

<dialog
    bind:this={dialogRef}
    class="font-inherit text-inherit p-3"
>
    <form
        autocomplete="off"
        class="flex flex-col gap-4"
        onsubmit={save}
    >
        <header class="flex justify-between items-center">
            <h5 class="font-semibold">New Transaction</h5>

            <button
                type="button"
                class="text-lg"
                aria-label="Close dialog"
                onclick={close}
            >
                <i class="fa-solid fa-xmark"></i>
            </button>
        </header>

        <div class="flex flex-col gap-2">
            <ToggleGroup
                value={transaction.operation}
                values={Object.values(TransactionType).map(
                    (type) =>
                        ({
                            name: type,
                            record: type,
                        }) satisfies SelectOption<TransactionType>,
                )}
            ></ToggleGroup>

            <DateField
                id="date"
                label="Date"
                bind:value={transaction.issued}
            />

            <SelectField
                id="category"
                label="Category"
                required
                values={categories.map(
                    (category) =>
                        ({
                            name: category.name,
                            record: category,
                        }) satisfies SelectOption<Category>,
                )}
                bind:value={category}
                validator={(value) => (value ? "" : "Please select an option")}
            ></SelectField>

            <CurrencyField
                amountId="amount"
                currencyId="currency"
                label="Amount"
                bind:amount={transaction.amount}
                bind:currency={transaction.currency}
                required
                validator={(value) => {
                    if ((value as number) <= 0)
                        return "The value must be greater than 0";
                }}
            ></CurrencyField>

            <InputField
                id="description"
                label="Description"
                placeholder="Enter some notes"
                required
                bind:value={transaction.description}
            ></InputField>
        </div>

        <button
            class="primary w-full"
            type="submit"
        >
            Save
        </button>
    </form>
</dialog>
