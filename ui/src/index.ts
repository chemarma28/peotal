import "./extentions";
import { mount } from "svelte";
import App from "./App.svelte";
import { initTranslations } from "./i18n";

await initTranslations();

document.body.innerHTML = "";

const app = mount(App, { target: document.body });

export default app;
