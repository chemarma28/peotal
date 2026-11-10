<script lang="ts">
    import { fly } from "svelte/transition";
    import {
        faCircleCheck,
        faCircleXmark,
        faTriangleExclamation,
        faXmark,
        type IconDefinition,
    } from "@fortawesome/free-solid-svg-icons";
    import Icon from "@ui/components/Icon.svelte";
    import { _ } from "@ui/i18n";
    import { hideToast, toasts, ToastVariant } from "./toasts";
</script>

{#snippet title(title: string, icon: IconDefinition, color: string)}
    <div class={`flex min-w-0 items-center gap-1 ${color}`}>
        <Icon {icon} />
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
                        {@render title(toast.title, faCircleCheck, "text-green-700")}
                    {:else if toast.variant === ToastVariant.ERROR}
                        {@render title(toast.title, faCircleXmark, "text-red-700")}
                    {:else if toast.variant === ToastVariant.WARNING}
                        {@render title(toast.title, faTriangleExclamation, "text-amber-400")}
                    {/if}

                    <div class="shrink-0">
                        <button
                            type="button"
                            class="icon"
                            aria-label={_("common.close")}
                            title={_("common.close")}
                            onclick={() => {
                                hideToast(toast);
                            }}>
                            <Icon icon={faXmark} />
                        </button>
                    </div>
                </div>
            </div>
        </div>
    {/each}
</div>
