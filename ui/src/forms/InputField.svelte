<script lang="ts">
    import type { HTMLInputAttributes, HTMLInputTypeAttribute } from "svelte/elements";
    import FormField from "./FormField.svelte";

    type Props = {
        id: string;
        type?: HTMLInputTypeAttribute;
        label: string;
        required?: boolean;
        value?: string;
        validator?: (value?: string) => string | undefined;
    } & Omit<HTMLInputAttributes, "id" | "type" | "value" | "required">;

    let elementRef = $state<HTMLInputElement>();

    let { id, label, type, value = $bindable<string>(), required, validator, ...rest }: Props = $props();

    $effect(() => {
        elementRef?.setCustomValidity(validator?.(value) ?? "");
    });
</script>

<FormField {id} {label} {required}>
    <div class="form-control">
        <input bind:this={elementRef} {id} type={type ?? "text"} {required} bind:value {...rest} />
    </div>
</FormField>
