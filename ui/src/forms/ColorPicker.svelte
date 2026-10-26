<script lang="ts">
    import { PRESET_COLORS, isPresetColor } from "@ui/categories/colors";
    import { _ } from "@ui/i18n";

    type Props = {
        value?: string;
        label: string;
    };

    let { value = $bindable("#7cb342"), label }: Props = $props();

    let colorInput = $state<HTMLInputElement>();

    let isCustom = $derived(!isPresetColor(value));

    function openColorPicker() {
        colorInput?.click();
    }
</script>

<div class="flex flex-col gap-1">
    <span class="text-sm font-semibold">{label}</span>

    <div class="flex flex-wrap items-center gap-1.5" role="group" aria-label={label}>
        {#each PRESET_COLORS as color}
            <button
                type="button"
                class="focus-visible:outline-border-focus h-6 w-6 shrink-0 rounded-md border-2 transition focus-visible:outline-2 focus-visible:outline-offset-2 {value ===
                color
                    ? 'border-border-focus ring-2 ring-border-focus/30'
                    : 'border-transparent hover:border-border-card'}"
                style:background-color={color}
                aria-label={color}
                aria-pressed={value === color}
                title={color}
                onclick={() => (value = color)}>
            </button>
        {/each}

        <button
            type="button"
            class="focus-visible:outline-border-focus text-border-focus flex h-6 w-6 shrink-0 items-center justify-center rounded-md border-2 border-dashed transition focus-visible:outline-2 focus-visible:outline-offset-2 {isCustom
                ? 'border-border-focus border-solid ring-2 ring-border-focus/30'
                : 'border-border-card hover:border-border-focus'}"
            aria-label={_("categories.color.custom")}
            title={_("categories.color.custom")}
            onclick={openColorPicker}>
            <i class="fa-solid fa-palette text-[10px]" aria-hidden="true"></i>
        </button>

        <input
            bind:this={colorInput}
            bind:value
            type="color"
            class="sr-only"
            tabindex="-1"
            aria-hidden="true" />
    </div>
</div>
