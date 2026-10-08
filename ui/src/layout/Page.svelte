<script lang="ts">
    import type { Snippet } from "svelte";

    type Width = "narrow" | "default" | "wide";

    type Props = {
        width?: Width;
        children: Snippet;
    };

    let { width = "default", children }: Props = $props();

    const WIDTHS: Record<Width, string> = {
        narrow: "max-w-3xl",
        default: "max-w-6xl",
        wide: "max-w-7xl",
    };
</script>

<div class="mx-auto w-full {WIDTHS[width]}">
    {@render children()}
</div>
