<script lang="ts">
    import AppShell from "@ui/layout/AppShell.svelte";
    import Activity from "@ui/routes/activity/Activity.svelte";
    import Categories from "@ui/routes/categories/Categories.svelte";
    import Login from "@ui/routes/Login.svelte";
    import { Route, useLocation } from "svelte-routing";

    const location = useLocation();
</script>

<Route path="/login"><Login /></Route>

{#if $location.pathname.replace(/\/+$/, "") !== "/login"}
    <AppShell>
        <Route path="/"><Activity /></Route>
        <Route path="/categories"><Categories /></Route>
    </AppShell>
{/if}
