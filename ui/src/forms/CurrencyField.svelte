<script lang="ts">
    import type { HTMLInputAttributes } from "svelte/elements";
    import FormField from "./FormField.svelte";
    import { onMount } from "svelte";
    import type { Attachment } from "svelte/attachments";
    import AutoNumeric from "autonumeric";
    import { Currency } from "@ui/types";

    type Props = {
        label: string;
        required?: boolean;
        amountId: string;
        amount: number;
        currencyId: string;
        currency: Currency;
        validator?: (value?: number) => string | undefined;
    } & Omit<HTMLInputAttributes, "id">;

    let {
        label,
        required,
        amountId,
        amount = $bindable<number>(),
        currencyId,
        currency = $bindable<Currency>(),
        validator,
        ...props
    }: Props = $props();

    let numeric: AutoNumeric;
    let input = $state<HTMLInputElement>();
    let initial = amount;

    onMount(() => {
        input?.addEventListener("autoNumeric:rawValueModified", (e: CustomEvent) => {
            const num = numeric.getNumber();

            amount = num ? num : 0;
        });
    });

    $effect(() => {
        if (!numeric) return;

        const current = numeric.getNumber() ?? 0;

        if (current !== amount) {
            numeric.set(amount);
        }
    });

    const autonumeric: Attachment = (input) => {
        let options: AutoNumeric.CallbackOptions = {
            digitGroupSeparator: " ",
            decimalCharacter: ",",
            allowDecimalPadding: false,
            modifyValueOnUpDownArrow: false,
            showWarnings: false,
        };

        numeric = new AutoNumeric(input as HTMLElement, initial, options);

        return () => {
            numeric.remove();
        };
    };

    $effect(() => {
        if (validator) input?.setCustomValidity(validator(amount) ?? "");
    });
</script>

<FormField id={amountId} {label} {required}>
    <div class="form-control form-currency-control flex items-center">
        <input {@attach autonumeric} type="text" inputmode="decimal" id={amountId} bind:this={input} {...props} />

        <select id={currencyId} bind:value={currency} class="px-1">
            {#each Object.values(Currency) as currency}
                <option>{currency}</option>
            {/each}
        </select>
    </div>
</FormField>
