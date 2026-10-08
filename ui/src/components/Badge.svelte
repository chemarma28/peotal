<script lang="ts">
    import type { Snippet } from "svelte";
    import { categoryIcon } from "@ui/categories/icons";

    type Props = {
        color: string;
        icon?: string;
        children: Snippet;
    };

    let { color, icon, children }: Props = $props();

    const accent = $derived(`color-mix(in oklab, ${color} 60%, black)`);
</script>

<span
    class="inline-flex items-center gap-1 rounded-full border-1 px-2 py-0.5 whitespace-nowrap"
    style:background-color={`color-mix(in oklab, ${color} 35%, white)`}
    style:border-color={color}
    style:color={accent}>
    <i class={categoryIcon(icon)} aria-hidden="true"></i>
    {@render children()}
</span>
