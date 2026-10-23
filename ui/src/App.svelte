<script lang="ts">
    import AppRoutes from "./AppRoutes.svelte";
    import { Router } from "svelte-routing";
    import Toasts from "./components/Toasts.svelte";

    export let url = "";
</script>

<Router {url}>
    <AppRoutes />

    <Toasts></Toasts>
</Router>
