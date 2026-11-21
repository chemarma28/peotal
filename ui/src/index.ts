import "./extentions";
import { mount } from "svelte";
import App from "./App.svelte";
import { loadAccount } from "./auth";
import { initTranslations } from "./i18n";

await initTranslations();
await loadAccount();

const app = mount(App, { target: document.getElementById("index")! });

export default app;
