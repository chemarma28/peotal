<script lang="ts">
    import api from "@ui/api";
    import { auth } from "@ui/auth";
    import { showToast, ToastVariant } from "@ui/components/toasts";
    import InputField from "@ui/forms/InputField.svelte";
    import { _ } from "@ui/i18n";
    import { type Account, type Credentials } from "@ui/types";
    import { navigate } from "svelte-routing";

    let creds = $state<Credentials>({ username: "", password: "" });
    let showEnsureMessage = $state(false);

    $effect(() => {
        if ($auth) navigate("/", { replace: true });
    });

    async function submit(e: SubmitEvent) {
        e.preventDefault();

        try {
            const account = await api.post<Account>("auth/login", creds);
            auth.set(account);
        } catch (error) {
            const status = (error as { status?: number } | undefined)?.status;
            const message = (error as { message?: string } | undefined)?.message;

            if (status && status === 401) showEnsureMessage = true;
            else showToast(ToastVariant.ERROR, message ?? _("auth.login.error"));
        }
    }
</script>

<main class="flex min-h-dvh items-center justify-center bg-zinc-100 p-4">
    <div class="border-border-card bg-bg-card flex w-full max-w-sm flex-col gap-5 rounded-xl border-1 p-6 shadow-sm">
        <span class="text-lg font-bold">Peotal</span>

        <div class="flex flex-col gap-1">
            <h1 class="text-2xl font-semibold tracking-tight">{_("auth.login.title")}</h1>
            <p class="text-sm opacity-70">{_("auth.login.description")}</p>
        </div>

        <form id="login" class="flex flex-col gap-2" onsubmit={submit}>
            <InputField bind:value={creds.username} id="username" label="Username" autofocus required></InputField>
            <InputField bind:value={creds.password} type="password" id="password" label="Password" required>
            </InputField>

            {#if showEnsureMessage}
                <p class="text-sm text-red-700">{_("auth.login.check")}</p>
            {/if}
        </form>

        <button class="primary" type="submit" form="login">Submit</button>
    </div>
</main>
