<script lang="ts">
    import { faChevronDown } from "@fortawesome/free-solid-svg-icons";
    import Icon from "@ui/components/Icon.svelte";
    import { type Snippet } from "svelte";
    import { _ } from "@ui/i18n";

    type Props = {
        id: string;
        children: Snippet;
        class?: string;
    };

    let { id, class: className, children }: Props = $props();

    let dangerOpen = $state(false);
</script>

<div class={className}>
    <button
        type="button"
        class="flex w-full items-center justify-between text-sm font-semibold"
        aria-expanded={dangerOpen}
        aria-controls={id}
        onclick={() => (dangerOpen = !dangerOpen)}>
        <span>{_("forms.danger.title")}</span>
        <Icon icon={faChevronDown} class="text-xs transition-transform {dangerOpen ? 'rotate-180' : ''}" />
    </button>

    {#if dangerOpen}
        <div {id} class="mt-2">
            {@render children()}
        </div>
    {/if}
</div>
