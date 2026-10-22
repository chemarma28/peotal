<script lang="ts">
    import { _ } from "@ui/i18n";

    type Props = {
        begin?: Date;
        stretch?: boolean;
    };

    let { begin = $bindable<Date>(new Date()), stretch }: Props = $props();

    const MONTH_KEYS = [
        "months.january",
        "months.february",
        "months.march",
        "months.april",
        "months.may",
        "months.june",
        "months.july",
        "months.august",
        "months.september",
        "months.october",
        "months.november",
        "months.december",
    ];

    const MIN_YEAR = 1900;
    const MAX_YEAR = 2100;

    let month = $derived(begin.getMonth());
    let year = $derived(begin.getFullYear());

    let monthOptions = $derived(
        MONTH_KEYS.map((key, value) => ({
            value,
            label: _(key),
        })),
    );

    function pickMonth(event: Event) {
        const value = Number((event.currentTarget as HTMLSelectElement).value);

        begin = new Date(year, value, 1);
    }

    function pickYear(event: Event) {
        const raw = (event.currentTarget as HTMLInputElement).value;

        if (raw.trim() === "") {
            return;
        }

        const value = Number(raw);

        if (!Number.isInteger(value) || value < MIN_YEAR || value > MAX_YEAR) {
            return;
        }

        begin = new Date(value, month, 1);
    }

    function resetYear(event: Event) {
        (event.currentTarget as HTMLInputElement).value = String(year);
    }
</script>

<div
    class="flex items-center {stretch ? 'w-full lg:w-auto' : 'w-auto'}"
    role="group"
    aria-label={_("activity.period.label")}>
    <div
        class="border-border-card bg-bg-card focus-within:outline-border-focus flex h-8 min-w-0 flex-1 items-center rounded-lg border-1 focus-within:outline-2 lg:flex-none">
        <div class="relative flex h-full min-w-0 flex-1 lg:min-w-[7.5rem]">
            <select
                class="h-full w-full cursor-pointer appearance-none bg-transparent py-1 pr-6 pl-2 text-sm font-semibold focus:outline-none"
                value={month}
                aria-label={_("activity.period.month")}
                onchange={pickMonth}>
                {#each monthOptions as option}
                    <option value={option.value}>{option.label}</option>
                {/each}
            </select>

            <i
                class="fa-solid fa-chevron-down pointer-events-none absolute top-1/2 right-2 -translate-y-1/2 text-[10px] opacity-50">
            </i>
        </div>

        <span class="bg-border-card h-4 w-px shrink-0"></span>

        <div class="flex h-full shrink-0 items-center">
            <input
                class="h-full w-[4.5rem] bg-transparent py-1 pr-1 pl-2 text-sm font-semibold tabular-nums focus:outline-none"
                type="number"
                min={MIN_YEAR}
                max={MAX_YEAR}
                step="1"
                value={year}
                aria-label={_("activity.period.year")}
                title={_("activity.period.year")}
                autocomplete="off"
                oninput={pickYear}
                onblur={resetYear} />
        </div>
    </div>
</div>

<style>
    input[type="number"]::-webkit-inner-spin-button,
    input[type="number"]::-webkit-outer-spin-button {
        opacity: 0.6;
    }
</style>
