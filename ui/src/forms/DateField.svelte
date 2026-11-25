<script lang="ts">
    import type { HTMLInputAttributes } from "svelte/elements";
    import InputField from "./InputField.svelte";

    type Props = {
        id: string;
        label: string;
        value?: string;
        validator?: (value?: string) => string | undefined;
    } & Omit<HTMLInputAttributes, "id" | "label" | "value">;

    let { id, label, value = $bindable<string>(), validator }: Props = $props();

    const format = {
        get string(): string {
            if (!value) value = new Date().toIsoDate();

            return value;
        },

        set string(val: string) {
            value = val;
        },
    };
</script>

<InputField type="date" {id} {label} bind:value={format.string} {validator}></InputField>
