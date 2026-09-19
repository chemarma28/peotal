<script
    lang="ts"
    generics="T"
>
    import type { SelectOption } from "./types";
    import FormField from "./FormField.svelte";

    type Props = {
        id: string;
        label: string;
        required: boolean;
        value?: T;
        values: SelectOption<T>[];
        validator?: (value?: T) => string | undefined;
    };

    let {
        id,
        label,
        required,
        values,
        value = $bindable(),
        validator,
    }: Props = $props();

    let elementRef = $state<HTMLSelectElement>();

    $effect(() => {
        if (validator) elementRef?.setCustomValidity(validator(value) ?? "");
    });
</script>

<FormField
    {id}
    {label}
    {required}
>
    <div class="form-control">
        <select
            bind:this={elementRef}
            {id}
            bind:value
        >
            <option
                value={undefined}
                disabled
            >
                Select a category
            </option>

            {#each values as val}
                <option value={val.record}>{val.name}</option>
            {/each}
        </select>
    </div>
</FormField>
