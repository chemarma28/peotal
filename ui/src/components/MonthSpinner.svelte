<script lang="ts">
    import { _ } from "@ui/i18n";

    type Props = {
        begin?: Date;
        stretch?: boolean;
    };

    let { begin = $bindable<Date>(new Date()), stretch }: Props = $props();

    let month = $derived<number>(begin.getMonth());
    let year = $derived<number>(begin.getFullYear());

    const months = [
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

    let format = $derived(`${_(months[month])} ${year}`);

    function shiftMonth(s: number) {
        begin = new Date(year, month + s, 1);
    }
</script>

<div class="flex {stretch ? 'w-full lg:w-[20rem]' : 'w-[20rem]'} items-center">
    <button class="secondary" aria-label={_("activity.period.previous")} onclick={() => shiftMonth(-1)}>
        <i class="fa-solid fa-chevron-left"></i>
    </button>

    <span class="flex-1 text-center font-semibold">{format}</span>

    <button class="secondary" aria-label={_("activity.period.next")} onclick={() => shiftMonth(1)}>
        <i class="fa-solid fa-chevron-right"></i>
    </button>
</div>
