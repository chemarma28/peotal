<script lang="ts">
    import { _ } from "@ui/i18n";
</script>

<div class="border-border-card bg-bg-card grid grid-cols-3 rounded-lg border-1">
    <div class="flex flex-col gap-1 p-3">
        <span class="text-sm opacity-60">{_("activity.summary.transactions")}</span>
        <span class="text-xl font-semibold">0</span>
    </div>

    <div class="border-border-card flex flex-col gap-1 border-l-1 p-3">
        <span class="text-sm opacity-60">{_("activity.summary.income")}</span>
        <span class="text-xl font-semibold">$0.00</span>
    </div>

    <div class="border-border-card flex flex-col gap-1 border-l-1 p-3">
        <span class="text-sm opacity-60">{_("activity.summary.expenses")}</span>
        <span class="text-xl font-semibold">$0.00</span>
    </div>
</div>
