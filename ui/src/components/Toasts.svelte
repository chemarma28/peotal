<script lang="ts">
    import { fly } from "svelte/transition";
    import { hideToast, toasts, ToastVariant } from "./toasts";
</script>

{#snippet title(title: string, icon: string, color: string)}
    <div class={`flex min-w-0 items-center gap-1 ${color}`}>
        <i class={icon}></i>
        <span class="truncate">{title}</span>
    </div>
{/snippet}

<div class="fixed top-0 left-[50%] z-50 flex -translate-x-[50%] flex-col gap-2 p-2" aria-live="assertive">
    {#each $toasts as toast}
        <div
            role="status"
            transition:fly
            class="bg-bg-card border-border-card w-3xs max-w-3xs rounded-lg border-1 shadow-lg">
            <div class="flex min-w-0 flex-col gap-2 p-2">
                <div class="flex min-w-0 items-center justify-between text-nowrap">
                    {#if toast.variant === ToastVariant.SUCCESS}
                        {@render title(toast.title, "fa-solid fa-circle-check", "text-green-700")}
                    {:else if toast.variant === ToastVariant.ERROR}
                        {@render title(toast.title, "fa-solid fa-circle-xmark", "text-red-700")}
                    {:else if toast.variant === ToastVariant.WARNING}
                        {@render title(toast.title, "fa-solid fa-triangle-exclamation", "text-amber-400")}
                    {/if}

                    <div class="shrink-0">
                        <button
                            type="button"
                            class="cursor-pointer text-sm"
                            aria-label="close toast"
                            onclick={() => {
                                hideToast(toast);
                            }}>
                            <i class="fa-solid fa-xmark"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    {/each}
</div>
