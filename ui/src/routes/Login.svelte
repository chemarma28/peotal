<script lang="ts">
    import { _ } from "@ui/i18n";
</script>

<main class="flex min-h-dvh items-center justify-center bg-zinc-100 p-4">
    <div class="border-border-card bg-bg-card flex w-full max-w-sm flex-col gap-5 rounded-xl border-1 p-6 shadow-sm">
        <span class="text-lg font-bold">Peotal</span>

        <div class="flex flex-col gap-1">
            <h1 class="text-2xl font-semibold tracking-tight">{_("auth.login.title")}</h1>
            <p class="text-sm opacity-70">{_("auth.login.description")}</p>
        </div>
    </div>
</main>
