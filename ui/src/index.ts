import "./extentions";
import { mount } from "svelte";
import App from "./App.svelte";
import { initTranslations } from "./i18n";

await initTranslations();

const app = mount(App, { target: document.getElementById("index")! });

export default app;
