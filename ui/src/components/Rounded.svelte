<script lang="ts">
    let { color, children } = $props();
</script>

<div
    class="inline-block w-[8rem] rounded-full border-1 p-0.5 text-center"
    style:background-color={`color-mix(in oklab, ${color} 50%, transparent)`}
    style:border-color={color}>
    {@render children()}
</div>
