<script lang="ts">
    import { faChartPie, faReceipt, faTags } from "@fortawesome/free-solid-svg-icons";
    import { setNavigationItems } from "@ui/layout/navigation";
    import Activity from "@ui/routes/activity/Activity.svelte";
    import Categories from "@ui/routes/categories/Categories.svelte";
    import Login from "@ui/routes/Login.svelte";
    import { Route } from "svelte-routing";
    import { auth } from "./auth";
    import NotFound from "./routes/NotFound.svelte";

    const routes = [
        { to: "/", icon: faReceipt, label: "routes.activity" },
        { to: "/categories", icon: faTags, label: "routes.categories" },
        { to: "/budget", icon: faChartPie, label: "routes.budget" },
    ];

    setNavigationItems(routes);
</script>

{#if !$auth}
    <Route path="/login"><Login /></Route>
{:else}
    <Route path="/"><Activity /></Route>
    <Route path="/categories"><Categories /></Route>
    <Route path="/login"><Login /></Route>
{/if}

<Route><NotFound /></Route>
