<script lang="ts">
    import InputField from "./InputField.svelte";

    type Props = {
        id: string;
        label: string;
        required?: boolean;
        value?: string;
    };

    let { id, label, required, value = $bindable<string>() }: Props = $props();

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

<InputField type="date" {id} {label} {required} bind:value={format.string}></InputField>
