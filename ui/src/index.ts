import "./extentions";
import { mount } from "svelte";
import App from "./App.svelte";

document.body.innerHTML = "";

const app = mount(App, { target: document.body });

export default app;
