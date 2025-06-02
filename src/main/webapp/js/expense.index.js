import { client } from './hx-client.js'

const ACCORDION_HEADER = ".accordion-header";
const ACCORDION_BODY = ".accordion-body";

export const AccordionSupport = {
    elements: [],

    init: function () {
        const support = this;

        $(".accordion").each(function () {
            const accordion = $(this);

            support.elements.push(
                new Accordion(accordion)
            );
        });
    }
}

class Accordion {
    constructor(accordion) {
        const self = this;

        accordion.find(ACCORDION_HEADER).on("click", function () {
            const previous = accordion.find("section.active");
            const current = $(this).closest("section").toggleClass("active");

            if (!current.is(previous)) {
                previous.toggleClass("active");
                self.trigger(previous);
            }

            self.trigger(current);
        });
    }

    trigger(section) {
        const body = section.find(ACCORDION_BODY);

        body.css({
            maxHeight: section.hasClass("active") ? `${body.prop("scrollHeight")}px` : "0px"
        });
    }
}

export const ButtonGroupSupport = {
    elements: [],

    init: function () {
        const support = ButtonGroupSupport;

        $(".btn-group").each(function () {
            support.elements.push(
                new ButtonGroup($(this))
            )
        });
    }
}

class ButtonGroup {
    constructor(buttonGroup) {
        buttonGroup.on("click", function (e) {
            const clicked = e.target;
            const enabled = $(this).find(".enabled").get(0);

            if (enabled != clicked) {
                clicked.classList.toggle("enabled");
                enabled.classList.toggle("enabled");
            }
        });
    }
}

export const ModalSupport = {
    controls: [],

    init: function () {
        const support = this;

        $(".toggle-modal").each(function () {
            const element = $(this);

            support.controls.push(
                new OnClickTogglingModalElement(element)
            );
        });

        htmx.on("htmx:afterRequest", function (e) {
            toggle($(e.detail.elt).closest(".modal"));
        });

        htmx.on("htmx:targetError", function (e) {
            toggle($(e.detail.elt).closest(".modal"));
            e.preventDefault();
        });
    }
}

class OnClickTogglingModalElement {
    constructor(element) {
        const target = element.data("target");
        let modal = target ? $(target) : $(".modal");

        element.on("click", function (e) {
            modal.each(function () {
                if (e.target == element.get(0))
                    toggle($(this));
            })
        });
    }
}

function toggle(modal) {
    if (modal.hasClass("shown")) {
        modal.addClass("hidden");
        modal.removeClass("shown");
    } else {
        modal.removeClass("hidden");
        modal.addClass("shown");
    }
}


function init() {
    AccordionSupport.init();
    ButtonGroupSupport.init();
    ModalSupport.init();

    client.run();
}

$(document).ready(() => {
    init();

    htmx.on("htmx:afterSettle", function (e) {
        init();
    });

    $("aside tr").on("click", function (e) {
        $("aside table tr.selected").toggleClass("selected");
        $(this).toggleClass("selected");
    })
});
