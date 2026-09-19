<script lang="ts">
    let { color, children } = $props();
</script>

<div
    class="inline-block text-center p-0.5 border-1 rounded-full w-[8rem]"
    style:background-color={`color-mix(in oklab, ${color} 50%, transparent)`}
    style:border-color={color}
>
    {@render children()}
</div>
