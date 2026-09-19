<script lang="ts">
    import { slide } from "svelte/transition";

    let { header, body } = $props();

    let hidden = $state(false);

    function toggle() {
        hidden = !hidden;
    }
</script>

<div class="bg-bg-card border-1 rounded-lg border-border-card">
    <button
        class="flex justify-start items-center gap-1 font-inherit font-semibold text-lg p-2 cursor-pointer w-full"
        onclick={toggle}
    >
        <i
            class="fa fa-solid fa-angle-right transition-transform {!hidden
                ? 'rotate-90'
                : 'rotate-0'}"
        ></i>
        <span>{header}</span>
    </button>

    {#if !hidden}
        <div
            class="c-accordion-collapsible"
            transition:slide
        >
            <div class="pr-2 pb-2 pl-2">
                {@render body()}
            </div>
        </div>
    {/if}
</div>
