<script lang="ts" generics="T">
    import type { SelectOption, SelectionKey } from "./types";

    type Props = {
        chosen?: SelectOption<T>;
        options: SelectOption<T>[];
        key?: SelectionKey<T>;
        styleClass?: string;
        label?: string;
        disabled?: boolean;
    };

    let {
        chosen = $bindable<SelectOption<T> | undefined>(),
        options,
        key = (o) => o.record,
        styleClass,
        label,
        disabled = false,
    }: Props = $props();

    const selected = (option: SelectOption<T>) => chosen != null && key(option) === key(chosen);
</script>

<div class="toggle-group {styleClass}" role="radiogroup" aria-label={label}>
    {#each options as option}
        <button
            type="button"
            role="radio"
            aria-checked={selected(option)}
            class={selected(option) ? "active" : ""}
            {disabled}
            onclick={() => {
                chosen = option;
            }}>
            <span>{option.name}</span>

            {#if option.badge != undefined}
                <span class="toggle-badge">{option.badge}</span>
            {/if}
        </button>
    {/each}
</div>
