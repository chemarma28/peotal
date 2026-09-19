<script lang="ts">
    import Header from "@ui/layout/Header.svelte";
    import Activity from "@ui/routes/activity/Activity.svelte";
    import Login from "@ui/routes/Login.svelte";
    import { Route, Router } from "svelte-routing";
    import Toasts from "./components/Toasts.svelte";

    export let url = "";
</script>

<Router {url}>
    <Route path="/login"><Login /></Route>

    <div>
        <header class="bg-zinc-200 p-2">
            <div class="mx-auto max-w-7xl">
                <Header></Header>
            </div>
        </header>

        <main class="p-2">
            <div class="mx-auto max-w-7xl">
                <Route path="/"><Activity /></Route>
            </div>
        </main>
    </div>

    <Toasts></Toasts>
</Router>
