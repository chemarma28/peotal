<script lang="ts" generics="T">
    import type { SelectOption, SelectionKey } from "./types";
    import FormField from "./FormField.svelte";
    import { _ } from "@ui/i18n";

    type Props = {
        id: string;
        label: string;
        required?: boolean;
        chosen?: SelectOption<T>;
        options: SelectOption<T>[];
        placeholder?: string;
        key?: SelectionKey<T>;
        validator?: (value?: SelectOption<T>) => string | undefined;
    };

    let {
        id,
        label,
        required,
        chosen = $bindable<SelectOption<T> | undefined>(),
        options,
        placeholder,
        key = (o) => o.record,
        validator,
    }: Props = $props();

    let elementRef = $state<HTMLSelectElement>();

    const index = $derived(chosen ? options.findIndex((o) => key(o) === key(chosen!)) : -1);

    function pick(event: Event) {
        const index = Number((event.currentTarget as HTMLSelectElement).value);

        chosen = index >= 0 ? options[index] : undefined;
    }

    $effect(() => {
        elementRef?.setCustomValidity(validator?.(chosen) ?? "");
    });
</script>

<FormField {id} {label} {required}>
    <div class="form-control">
        <select bind:this={elementRef} {id} {required} value={index < 0 ? "" : String(index)} onchange={pick}>
            <option value="" disabled>{placeholder ?? _("forms.select.undefined")}</option>

            {#each options as option, i}
                <option value={String(i)}>{option.name}</option>
            {/each}
        </select>
    </div>
</FormField>
