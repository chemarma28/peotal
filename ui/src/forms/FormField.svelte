<script lang="ts">
    import type { Snippet } from "svelte";

    type Props = {
        id: string;
        label: string;
        required?: boolean;
        children?: Snippet;
    };

    let { id, label, required, children }: Props = $props();
</script>

<div class="flex flex-col gap-1">
    <div class="flex flex-row items-center">
        <label for={id} class="cursor-pointer text-sm font-semibold">
            {label}
            {#if required}
                <span class="text-xs text-red-700">*</span>
            {/if}
        </label>
    </div>

    <div>
        {@render children?.()}
    </div>
</div>
