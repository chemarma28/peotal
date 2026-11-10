<script lang="ts">
    import type { Snippet } from "svelte";
    import { setContext } from "svelte";
    import { Link } from "svelte-routing";
    import { faChartPie, faReceipt, faTags, type IconDefinition } from "@fortawesome/free-solid-svg-icons";
    import Icon from "@ui/components/Icon.svelte";
    import { HEADER_ACTION, type HeaderAction, type HeaderActionStore } from "@ui/layout/headerAction";
    import { _ } from "@ui/i18n";

    type Props = {
        children: Snippet;
    };

    let { children }: Props = $props();

    const routes = [
        { to: "/", icon: faReceipt, label: "routes.activity" },
        { to: "/categories", icon: faTags, label: "routes.categories" },
        { to: "/budget", icon: faChartPie, label: "routes.budget" },
    ];

    let headerAction = $state<HeaderAction>();

    setContext<HeaderActionStore>(HEADER_ACTION, {
        set: (action?: HeaderAction) => {
            headerAction = action;
        },
    });
</script>

{#snippet sidebarLink(to: string, icon: IconDefinition, label: string)}
    <Link
        {to}
        getProps={({ isCurrent }) => ({
            class: `flex items-center gap-3 rounded-lg px-3 py-2 text-sm ${isCurrent ? "bg-bg-toggle-active font-semibold" : "hover:bg-zinc-100"}`,
        })}>
        <Icon {icon} />
        {_(label)}
    </Link>
{/snippet}

{#snippet tabLink(to: string, icon: IconDefinition, label: string)}
    <Link
        {to}
        getProps={({ isCurrent }) => ({
            class: `flex flex-1 flex-col items-center gap-1 py-2 text-xs ${isCurrent ? "font-semibold" : "opacity-60"}`,
        })}>
        <Icon {icon} />
        {_(label)}
    </Link>
{/snippet}

<div class="min-h-dvh bg-zinc-100">
    <aside
        class="border-border-card bg-bg-card hidden lg:fixed lg:inset-y-0 lg:left-0 lg:flex lg:w-60 lg:flex-col lg:border-r lg:p-4">
        <h3 class="font-bold">Peotal</h3>

        <nav class="mt-6 flex flex-col gap-1">
            {#each routes as r}
                {@render sidebarLink(r.to, r.icon, r.label)}
            {/each}
        </nav>
    </aside>

    <div class="lg:pl-60">
        <header class="sticky top-0 z-20 flex items-center justify-between gap-2 bg-zinc-200 p-2 lg:hidden">
            <h3 class="font-bold">Peotal</h3>

            {#if headerAction}
                <button
                    class="primary inline-flex items-center gap-1"
                    aria-label={headerAction.label}
                    onclick={headerAction.onClick}>
                    <Icon icon={headerAction.icon} />{headerAction.label}
                </button>
            {/if}
        </header>

        <main class="p-4 pb-16">
            {@render children()}
        </main>
    </div>

    <nav
        class="border-border-card bg-bg-card fixed inset-x-0 bottom-0 z-20 flex border-t pb-[env(safe-area-inset-bottom)] lg:hidden">
        {#each routes as r}
            {@render tabLink(r.to, r.icon, r.label)}
        {/each}
    </nav>
</div>
