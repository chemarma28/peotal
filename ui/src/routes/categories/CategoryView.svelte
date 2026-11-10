<script lang="ts">
    import api from "@ui/api";
    import { isPresetColor } from "@ui/categories/colors";
    import { CategoryIcons, DEFAULT_CATEGORY_ICON } from "@ui/categories/icons";
    import { faChevronDown } from "@fortawesome/free-solid-svg-icons";
    import Badge from "@ui/components/Badge.svelte";
    import Dialog from "@ui/components/Dialog.svelte";
    import Icon from "@ui/components/Icon.svelte";
    import { showToast, ToastVariant } from "@ui/components/toasts";
    import ColorPicker from "@ui/forms/ColorPicker.svelte";
    import IconPicker from "@ui/forms/IconPicker.svelte";
    import InputField from "@ui/forms/InputField.svelte";
    import SelectField from "@ui/forms/SelectField.svelte";
    import type { SelectOption } from "@ui/forms/types";
    import { _ } from "@ui/i18n";
    import { TransactionType, type Category } from "@ui/types";

    type Props = {
        onSave: (category: Category) => void;
        onDelete?: (category: Category) => void;
    };

    let { onSave, onDelete }: Props = $props();

    type Draft = {
        id?: number;
        name: string;
        color: string;
        icon: string;
        ordinal?: number;
    };

    const operationOptions: SelectOption<TransactionType>[] = [
        { name: _("types.transactions.expense"), record: TransactionType.EXPENSE },
        { name: _("types.transactions.income"), record: TransactionType.INCOME },
    ];

    let dialogRef = $state<Dialog>();
    let saving = $state(false);
    let confirmingDelete = $state(false);
    let dangerOpen = $state(false);
    let draft = $state<Draft>(empty());
    let operation = $state<SelectOption<TransactionType> | undefined>();

    let isNew = $derived(draft.id === undefined);
    let isCustomColor = $derived(!isPresetColor(draft.color));
    let dialogTitle = $derived(
        confirmingDelete
            ? _("categories.delete.title")
            : isNew
              ? _("categories.title.new")
              : _("categories.title.edit"),
    );

    function empty(): Draft {
        return {
            name: "",
            color: "#7cb342",
            icon: DEFAULT_CATEGORY_ICON,
        };
    }

    export function show(source?: Category, defaultOperation: TransactionType = TransactionType.EXPENSE) {
        draft = source
            ? {
                  id: source.id,
                  name: source.name,
                  color: source.color,
                  icon: source.icon ?? DEFAULT_CATEGORY_ICON,
                  ordinal: source.ordinal,
              }
            : empty();

        operation = operationOptions.find((o) => o.record === (source?.operation ?? defaultOperation));
        confirmingDelete = false;
        dangerOpen = false;
        saving = false;

        dialogRef?.show();
    }

    export function close() {
        dialogRef?.close();
    }

    async function save() {
        if (!operation) return;

        saving = true;

        try {
            const payload = { ...draft, operation: operation.record };
            const saved = isNew
                ? await api.post<Category>("category", payload)
                : await api.put<Category>("category", payload);

            onSave(saved);
            showToast(ToastVariant.SUCCESS, _("categories.toast.saved"));
            close();
        } catch (error) {
            const message = (error as { message?: string } | undefined)?.message;

            showToast(ToastVariant.ERROR, message ?? _("categories.toast.failed"));
        } finally {
            saving = false;
        }
    }

    async function remove() {
        if (draft.id === undefined) return;

        saving = true;

        try {
            await api.delete(`category/${draft.id}`);

            onDelete?.({
                id: draft.id,
                name: draft.name,
                operation: operation?.record ?? TransactionType.EXPENSE,
                color: draft.color,
                icon: draft.icon,
                ordinal: draft.ordinal ?? 0,
            });

            showToast(ToastVariant.SUCCESS, _("categories.toast.deleted"));
            close();
        } catch (error) {
            showToast(ToastVariant.ERROR, _("categories.toast.deleteFailed"));
        } finally {
            saving = false;
        }
    }

    function submit() {
        if (confirmingDelete) {
            void remove();
        } else {
            void save();
        }
    }
</script>

<Dialog bind:this={dialogRef} title={dialogTitle} onsubmit={submit}>
    {#if confirmingDelete}
        <p class="text-sm">
            {_("categories.delete.description")}
        </p>

        <div class="flex w-full items-center justify-center rounded-lg border-1 border-zinc-300 bg-zinc-200 p-4">
            <Badge color={draft.color} icon={draft.icon}>
                {draft.name.trim() || _("categories.preview.name")}
            </Badge>
        </div>
    {:else}
        <div
            class="relative flex w-full items-center justify-center rounded-lg border-1 border-zinc-300 bg-zinc-200 p-4">
            {#if isCustomColor}
                <span
                    class="border-border-card bg-bg-card absolute top-2 right-2 z-10 rounded-md border-1 px-1.5 py-0.5 text-[10px] font-semibold tracking-wide uppercase">
                    {_("categories.color.custom")}
                </span>
            {/if}

            <Badge color={draft.color} icon={draft.icon}>
                {draft.name.trim() || _("categories.preview.name")}
            </Badge>
        </div>

        <InputField
            id="name"
            label={_("categories.labels.name")}
            placeholder={_("categories.placeholders.name")}
            required
            autofocus
            maxlength={64}
            bind:value={draft.name} />

        <SelectField
            id="type"
            label={_("categories.labels.type")}
            required
            options={operationOptions}
            bind:chosen={operation} />

        <ColorPicker label={_("categories.labels.color")} bind:value={draft.color} />

        <div class="flex flex-col gap-1">
            <span class="text-sm font-semibold">{_("categories.labels.icon")}</span>

            <IconPicker
                label={_("categories.labels.icon")}
                icons={CategoryIcons}
                bind:value={draft.icon}
                color={draft.color} />
        </div>

        {#if !isNew}
            <div class="mt-2">
                <button
                    type="button"
                    class="flex w-full items-center justify-between text-sm font-semibold"
                    aria-expanded={dangerOpen}
                    aria-controls="category-danger-zone"
                    onclick={() => (dangerOpen = !dangerOpen)}>
                    <span>{_("categories.danger.title")}</span>
                    <Icon
                        icon={faChevronDown}
                        class="text-xs transition-transform {dangerOpen ? 'rotate-180' : ''}" />
                </button>

                {#if dangerOpen}
                    <div id="category-danger-zone" class="mt-2">
                        <button type="button" class="danger w-full" onclick={() => (confirmingDelete = true)}>
                            {_("categories.action.delete")}
                        </button>
                    </div>
                {/if}
            </div>
        {/if}
    {/if}

    {#snippet footer()}
        {#if confirmingDelete}
            <button type="submit" class="danger w-full" disabled={saving}>
                {_("common.continue")}
            </button>
        {:else}
            <button class="primary w-full" type="submit" disabled={saving}>
                {_("common.save")}
            </button>
        {/if}
    {/snippet}
</Dialog>
