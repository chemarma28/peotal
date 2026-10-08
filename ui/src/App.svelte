<script lang="ts">
    import AppShell from "@ui/layout/AppShell.svelte";
    import Activity from "@ui/routes/activity/Activity.svelte";
    import Categories from "./routes/categories/Categories.svelte";
    import Login from "@ui/routes/Login.svelte";
    import { Route, Router } from "svelte-routing";
    import Toasts from "./components/Toasts.svelte";

    export let url = "";
</script>

<Router {url}>
    <Route path="/login"><Login /></Route>

    <AppShell>
        <Route path="/"><Activity /></Route>
        <Route path="/categories"><Categories /></Route>
    </AppShell>

    <Toasts></Toasts>
</Router>
