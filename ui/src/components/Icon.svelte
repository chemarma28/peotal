<script lang="ts">
    import type { IconDefinition } from "@fortawesome/free-solid-svg-icons";
    import type { SVGAttributes } from "svelte/elements";

    type Props = {
        icon: IconDefinition;
    } & SVGAttributes<SVGSVGElement>;

    let { icon, class: className, ...rest }: Props = $props();

    const paths = $derived(Array.isArray(icon.icon[4]) ? icon.icon[4] : [icon.icon[4]]);
</script>

<svg
    class="inline-block {className}"
    width="1em"
    height="1em"
    viewBox={`0 0 ${icon.icon[0]} ${icon.icon[1]}`}
    fill="currentColor"
    aria-hidden="true"
    focusable="false"
    {...rest}>
    {#each paths as d}
        <path {d} />
    {/each}
</svg>
