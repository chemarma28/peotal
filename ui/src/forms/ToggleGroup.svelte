<script lang="ts" generics="T">
    import type { SelectOption } from "./types";

    type Props = {
        value?: T;
        values: SelectOption<T>[];
    };

    let chosen = $state<SelectOption<T>>();

    let { value = $bindable<T>(), values }: Props = $props();
</script>

<div class="toggle-group">
    {#each values as toggle}
        <button
            type="button"
            class={`${chosen?.name === toggle.name || toggle.record === value ? "active" : ""}`}
            onclick={() => {
                chosen = toggle;
                value = toggle.record;
            }}>{toggle.name}</button>
    {/each}
</div>
