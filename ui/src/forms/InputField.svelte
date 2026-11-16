<script lang="ts">
    import type { HTMLInputAttributes, HTMLInputTypeAttribute } from "svelte/elements";
    import FormField from "./FormField.svelte";

    type Props = {
        id: string;
        type?: HTMLInputTypeAttribute;
        label: string;
        required?: boolean;
        value?: string;
    } & Omit<HTMLInputAttributes, "id" | "type" | "value" | "required">;

    let { id, label, type, value = $bindable<string>(), required, ...rest }: Props = $props();
</script>

<FormField {id} {label} {required}>
    <div class="form-control">
        <input {id} type={type ?? "text"} {required} bind:value {...rest} />
    </div>
</FormField>
