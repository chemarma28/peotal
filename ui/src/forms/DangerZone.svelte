<script lang="ts">
    import { faChevronDown } from "@fortawesome/free-solid-svg-icons";
    import Icon from "@ui/components/Icon.svelte";
    import { type Snippet } from "svelte";
    import { _ } from "@ui/i18n";

    type Props = {
        id: string;
        children: Snippet;
        class?: string;
        open?: boolean;
    };

    let { id, class: className, children, open = $bindable(false) }: Props = $props();
</script>

<div class={className}>
    <button
        type="button"
        class="flex w-full items-center justify-between text-sm font-semibold"
        aria-expanded={open}
        aria-controls={id}
        onclick={() => (open = !open)}>
        <span>{_("forms.danger.title")}</span>
        <Icon icon={faChevronDown} class="text-xs transition-transform {open ? 'rotate-180' : ''}" />
    </button>

    {#if open}
        <div {id} class="mt-2">
            {@render children()}
        </div>
    {/if}
</div>
