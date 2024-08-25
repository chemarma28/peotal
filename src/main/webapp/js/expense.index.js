import { AccordionSupport } from "./accordion.js";
import { ButtonGroupSupport } from "./button-group.js";
import { ModalSupport } from "./modal.js";

$(document).ready(() => {
    AccordionSupport.init();
    ButtonGroupSupport.init();
    ModalSupport.init();
});
