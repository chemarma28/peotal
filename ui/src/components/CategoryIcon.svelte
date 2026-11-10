<script lang="ts">
    import Icon from "@ui/components/Icon.svelte";
    import { categoryIconDefinition } from "@ui/categories/icons";

    type Props = {
        color: string;
        icon?: string;
        class?: string;
    };

    let { color, icon, class: className }: Props = $props();

    const accent = $derived(`color-mix(in oklab, ${color} 60%, black)`);
</script>

<span
    class="inline-flex h-7 w-7 items-center justify-center rounded-full border-1 text-xs {className}"
    style:background-color={`color-mix(in oklab, ${color} 35%, white)`}
    style:border-color={color}
    style:color={accent}>
    <Icon icon={categoryIconDefinition(icon)} />
</span>
