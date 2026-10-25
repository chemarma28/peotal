<script lang="ts">
    import { slide } from "svelte/transition";

    let { header, body } = $props();

    let hidden = $state(false);

    function toggle() {
        hidden = !hidden;
    }
</script>

<div class="bg-bg-card border-border-card rounded-lg border-1">
    <button
        class="font-inherit outline-border-focus flex w-full cursor-pointer items-center justify-start gap-1 rounded-lg p-2 text-lg font-semibold focus-visible:outline-2 focus-visible:-outline-offset-2"
        onclick={toggle}>
        <i class="fa fa-solid fa-angle-right transition-transform {!hidden ? 'rotate-90' : 'rotate-0'}"></i>
        <span>{header}</span>
    </button>

    {#if !hidden}
        <div class="c-accordion-collapsible" transition:slide>
            <div class="pr-2 pb-2 pl-2">
                {@render body()}
            </div>
        </div>
    {/if}
</div>
