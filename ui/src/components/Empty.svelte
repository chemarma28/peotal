<script lang="ts">
    type Props = {
        header?: string;
        description?: string;
        loading?: boolean;
    };

    let {
        header,
        description,
        loading = $bindable<boolean>(false),
    }: Props = $props();
</script>

<div
    class="min-h-32 flex flex-col justify-center items-center gap-1 bg-bg-card border-1 border-border-card rounded-lg"
>
    {#if loading}
        <div
            class="w-8 h-8 rounded-full border-3 border-solid border-border-card border-t-transparent animate-spin"
        ></div>
    {:else}
        <div class="font-semibold">
            {header ?? "No entries found"}
        </div>

        <div class="text-sm max-w-md">
            {description ?? "Entries will appear here once they are added"}
        </div>
    {/if}
</div>
