<script lang="ts">
    type Props = {
        icons: Record<string, string>;
        value?: string;
        color?: string;
        label: string;
    };

    let { icons, value = $bindable<string>(), color, label }: Props = $props();

    const groupName = $props.id();
    const entries = $derived(Object.entries(icons));

    const accent = $derived(color ? `color-mix(in oklab, ${color} 60%, black)` : undefined);
    const selectedBackground = $derived(color ? `color-mix(in oklab, ${color} 30%, white)` : undefined);
</script>

<div class="grid grid-cols-8 gap-1 sm:grid-cols-10" role="radiogroup" aria-label={label}>
    {#each entries as [key, icon] (key)}
        <label
            class="has-[:focus-visible]:outline-border-focus relative inline-flex size-7 cursor-pointer items-center justify-center rounded-full border-2 border-transparent bg-zinc-100 text-xs transition-colors hover:bg-zinc-200 has-[:focus-visible]:outline-2 has-[:focus-visible]:outline-offset-2"
            style:border-color={value === key ? color : undefined}
            style:background-color={value === key ? selectedBackground : undefined}
            style:color={value === key ? accent : undefined}
            title={key}>
            <input
                class="sr-only"
                type="radio"
                name={groupName}
                value={key}
                checked={value === key}
                aria-label={key}
                onchange={() => (value = key)} />
            <i class={icon} aria-hidden="true"></i>
        </label>
    {/each}
</div>
