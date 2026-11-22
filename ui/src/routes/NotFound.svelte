<script lang="ts">
    import { auth } from "@ui/auth";
    import { _ } from "@ui/i18n";
    import { Link } from "svelte-routing";
</script>

<main class="flex min-h-dvh items-center justify-center bg-zinc-100 p-4">
    <div
        class="border-border-card bg-bg-card flex w-full max-w-md flex-col gap-3 rounded-xl border-1 p-6 text-center shadow-sm">
        <span class=" text-lg font-bold">{_("notFound.title")}</span>

        <div class="text-sm opacity-70">
            <p>{$auth ? _("notFound.description") : _("notFound.authDescription")}</p>
            <p>{$auth ? _("notFound.instruction") : _("notFound.authInstruction")}</p>
        </div>

        <div>
            {#if $auth}
                <Link class="hover:underline" to="/">{_("notFound.home")}</Link>
            {:else}
                <Link class="hover:underline" to="/login">{_("notFound.signIn")}</Link>
            {/if}
        </div>
    </div>
</main>
