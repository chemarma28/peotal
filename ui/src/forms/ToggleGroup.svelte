<script lang="ts" generics="T">
    import type { SelectOption, SelectionKey } from "./types";

    type Props = {
        chosen?: SelectOption<T>;
        options: SelectOption<T>[];
        key?: SelectionKey<T>;
    };

    let { chosen = $bindable<SelectOption<T> | undefined>(), options, key = (o) => o.record }: Props = $props();

    const selected = (option: SelectOption<T>) => chosen != null && key(option) === key(chosen);
</script>

<div class="toggle-group" role="radiogroup">
    {#each options as option}
        <button
            type="button"
            role="radio"
            aria-checked={selected(option)}
            class={selected(option) ? "active" : ""}
            onclick={() => {
                chosen = option;
            }}>{option.name}</button>
    {/each}
</div>
