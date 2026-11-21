<script lang="ts">
    import { auth } from "@ui/auth";
    import { tick } from "svelte";
    import { get } from "svelte/store";
    import { navigate } from "svelte-routing";

    let { children } = $props();

    $effect(() => {
        if ($auth) return;

        tick().then(() => {
            if (!get(auth)) navigate("/login", { replace: true });
        });
    });
</script>

{#if $auth}
    {@render children()}
{/if}
