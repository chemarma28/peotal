import { defineConfig } from "vite";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { svelte } from "@sveltejs/vite-plugin-svelte";
import tailwindcss from "@tailwindcss/vite";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const uiDir = path.resolve(__dirname, "ui");

export default defineConfig({
    root: uiDir,

    resolve: {
        tsconfigPaths: true
    },

    plugins: [
        svelte(),
        tailwindcss()
    ],

    server: {
        proxy: {
            "/api": "http://localhost:9097"
        }
    }
})
