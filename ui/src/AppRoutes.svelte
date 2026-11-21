<script lang="ts">
    import { faChartPie, faReceipt, faTags } from "@fortawesome/free-solid-svg-icons";
    import AppShell from "@ui/layout/AppShell.svelte";
    import Activity from "@ui/routes/activity/Activity.svelte";
    import Categories from "@ui/routes/categories/Categories.svelte";
    import Login from "@ui/routes/Login.svelte";
    import { Route, useLocation } from "svelte-routing";
    import Secure from "./routes/Secure.svelte";
    import { onDestroy } from "svelte";

    const routes = [
        { to: "/", icon: faReceipt, label: "routes.activity" },
        { to: "/categories", icon: faTags, label: "routes.categories" },
        { to: "/budget", icon: faChartPie, label: "routes.budget" },
    ];

    let isLoginRoute = $state<boolean>(false);
    let location = useLocation();

    const unsubscribe = location.subscribe((r) => {
        isLoginRoute = r.pathname.endsWith("login");

        return () => (isLoginRoute = false);
    });

    onDestroy(() => {
        unsubscribe();
    });
</script>

{#if isLoginRoute}
    <Route path="/login"><Login /></Route>
{:else}
    <Secure>
        <AppShell {routes}>
            <Route path="/"><Activity /></Route>
            <Route path="/categories"><Categories /></Route>
        </AppShell>
    </Secure>
{/if}
