<script lang="ts">
    type Props = {
        header?: string;
        description?: string;
        loading?: boolean;
        actionLabel?: string;
        onAction?: () => void;
        actionDisabled?: boolean;
    };

    let {
        header,
        description,
        loading = $bindable<boolean>(false),
        actionLabel,
        onAction,
        actionDisabled = false,
    }: Props = $props();
</script>

<div
    class="bg-bg-card border-border-card flex min-h-32 flex-col items-center justify-center gap-1 rounded-lg border-1 p-4">
    {#if loading}
        <div class="border-border-card h-8 w-8 animate-spin rounded-full border-3 border-solid border-t-transparent">
        </div>
    {:else}
        <div class="font-semibold">
            {header ?? "No entries found"}
        </div>

        <div class="text-center text-sm">
            {description ?? "Entries will appear here once they are added"}
        </div>

        {#if actionLabel && onAction}
            <button class="primary mt-2" disabled={actionDisabled} onclick={onAction}>
                {actionLabel}
            </button>
        {/if}
    {/if}
</div>
