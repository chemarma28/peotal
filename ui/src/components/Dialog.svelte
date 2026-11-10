<script lang="ts">
    import type { Snippet } from "svelte";
    import { faXmark } from "@fortawesome/free-solid-svg-icons";
    import Icon from "@ui/components/Icon.svelte";
    import { _ } from "@ui/i18n";

    type Props = {
        title: string;
        onsubmit?: (event: SubmitEvent) => void;
        onclose?: () => void;
        closable?: boolean;
        dismissible?: boolean;
        children?: Snippet;
        footer?: Snippet;
    };

    let { title, onsubmit, onclose, closable = true, dismissible = true, children, footer }: Props = $props();

    const titleId = $props.id();

    let dialogRef = $state<HTMLDialogElement>();
    let pressedOnBackdrop = $state(false);

    export function show() {
        if (!dialogRef || dialogRef.open) return;

        dialogRef.showModal();

        if (!dialogRef.querySelector("[autofocus]")) {
            dialogRef.querySelector<HTMLElement>("input, select, textarea")?.focus({ preventScroll: true });
        }
    }

    export function close() {
        dialogRef?.close();
    }

    function submit(event: SubmitEvent) {
        event.preventDefault();
        onsubmit?.(event);
    }

    function cancel(event: Event) {
        if (!dismissible) event.preventDefault();
    }

    function pointerDown(event: MouseEvent) {
        pressedOnBackdrop = event.target === dialogRef;
    }

    function click(event: MouseEvent) {
        if (dismissible && pressedOnBackdrop && event.target === dialogRef) close();

        pressedOnBackdrop = false;
    }
</script>

<dialog
    bind:this={dialogRef}
    class="font-inherit p-3 text-inherit"
    aria-labelledby={titleId}
    oncancel={cancel}
    onmousedown={pointerDown}
    onclick={click}
    {onclose}>
    <form autocomplete="off" class="flex flex-col gap-4" onsubmit={submit}>
        <header class="flex items-center justify-between">
            <h5 id={titleId} class="font-semibold">{title}</h5>

            {#if closable}
                <button
                    type="button"
                    class="icon"
                    aria-label={_("common.close")}
                    title={_("common.close")}
                    onclick={close}>
                    <Icon icon={faXmark} />
                </button>
            {/if}
        </header>

        <div class="flex flex-col gap-2">
            {@render children?.()}
        </div>

        {#if footer}
            <div class="flex justify-end gap-2">
                {@render footer()}
            </div>
        {/if}
    </form>
</dialog>
