<script lang="ts">
    import { Link } from "svelte-routing";
</script>

<div class="flex flex-row justify-between items-center">
    <nav class="flex items-center gap-16">
        <h3 class="font-bold">Oikance</h3>

        <div class="flex items-end gap-4">
            <Link to="/">Activity</Link>
            <Link to="/budget">Budget</Link>
        </div>
    </nav>

    <div>
        <button
            class="secondary"
            aria-label="settings"
        >
            <i class="fa-solid fa-gear"></i>
            <span>Settings</span>
        </button>
    </div>
</div>
