<script lang="ts">
    import type { Snippet } from "svelte";

    type Width = "narrow" | "default" | "wide";

    type Props = {
        width?: Width;
        title?: string;
        description?: string;
        actions?: Snippet;
        children: Snippet;
    };

    let { width = "default", title, description, actions, children }: Props = $props();

    const WIDTHS: Record<Width, string> = {
        narrow: "max-w-3xl",
        default: "max-w-6xl",
        wide: "max-w-7xl",
    };
</script>

<div class="mx-auto w-full {WIDTHS[width]}">
    {#if title || description || actions}
        <header class="mb-4 flex flex-col gap-3 border-b border-border-card pb-3 sm:flex-row sm:items-start sm:justify-between">
            <div class="min-w-0">
                {#if title}
                    <h1 class="text-2xl font-semibold tracking-tight">{title}</h1>
                {/if}

                {#if description}
                    <p class="mt-1 text-sm opacity-70">{description}</p>
                {/if}
            </div>

            {#if actions}
                <div class="flex shrink-0 items-center gap-2">
                    {@render actions()}
                </div>
            {/if}
        </header>
    {/if}

    {@render children()}
</div>
