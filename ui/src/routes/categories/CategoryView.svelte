<script lang="ts">
    import api from "@ui/api";
    import { isPresetColor } from "@ui/categories/colors";
    import { CATEGORY_ICONS, DEFAULT_CATEGORY_ICON } from "@ui/categories/icons";
    import Badge from "@ui/components/Badge.svelte";
    import Dialog from "@ui/components/Dialog.svelte";
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
    };

    let { onSave }: Props = $props();

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
    let draft = $state<Draft>(empty());
    let operation = $state<SelectOption<TransactionType> | undefined>();

    let isNew = $derived(draft.id === undefined);
    let isCustomColor = $derived(!isPresetColor(draft.color));

    function empty(): Draft {
        return {
            name: "",
            color: "#7cb342",
            icon: DEFAULT_CATEGORY_ICON,
        };
    }

    export function show(source?: Category) {
        draft = source
            ? {
                  id: source.id,
                  name: source.name,
                  color: source.color,
                  icon: source.icon ?? DEFAULT_CATEGORY_ICON,
                  ordinal: source.ordinal,
              }
            : empty();

        operation = operationOptions.find((o) => o.record === (source?.operation ?? TransactionType.EXPENSE));

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
</script>

<Dialog bind:this={dialogRef} title={isNew ? _("categories.title.new") : _("categories.title.edit")} onsubmit={save}>
    <div class="relative flex w-full items-center justify-center rounded-lg border-1 border-zinc-300 bg-zinc-200 p-4">
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
        bind:chosen={operation}
        disabled={!isNew} />

    <ColorPicker label={_("categories.labels.color")} bind:value={draft.color} />

    <div class="flex flex-col gap-1">
        <span class="text-sm font-semibold">{_("categories.labels.icon")}</span>

        <IconPicker
            label={_("categories.labels.icon")}
            icons={CATEGORY_ICONS}
            bind:value={draft.icon}
            color={draft.color} />
    </div>

    {#snippet footer()}
        <button class="primary w-full" type="submit" disabled={saving}>
            {_("common.save")}
        </button>
    {/snippet}
</Dialog>
