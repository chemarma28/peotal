<script lang="ts">
    import { _, __ } from "@ui/i18n";
    import { Link } from "svelte-routing";
</script>

<div class="flex flex-row items-center justify-between">
    <nav class="flex items-center gap-16">
        <h3 class="font-bold">Oikance</h3>

        <div class="flex items-end gap-4">
            <Link to="/">{_("routes.activity")}</Link>
            <Link to="/budget">{_("routes.budget")}</Link>
        </div>
    </nav>

    <div>
        <button class="secondary" aria-label="settings">
            <i class="fa-solid fa-gear"></i>
            <span>Settings</span>
        </button>
    </div>
</div>
