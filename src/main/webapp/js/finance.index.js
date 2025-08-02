import { client } from './hx-client.js'

export const AccordionSupport = {
    init: function () {
        const self = this;

        $(".accordion").find(".accordion-body").each(function () {
            self.setAccordionBodyMaxHeight($(this).parent());
        });

        $(".accordion").find(".accordion-header")
            .on("click", function () {
                const last = $(this).closest(".accordion-item").toggleClass("collapsed");

                self.setAccordionBodyMaxHeight(last);
            });
    },

    setAccordionBodyMaxHeight: function (accordionItem) {
        accordionItem.children(".accordion-body").each(function () {
            if (accordionItem.hasClass("collapsed"))
                $(this).css({
                    maxHeight: "0px"
                })
            else
                $(this).css({
                    maxHeight: $(this).prop("scrollHeight")
                })
        });
    }
}

const ButtonSwitchSupport = {
    init: function () {
        $(".button-switch").find("button").on("click", function () {
            const down = $(this).parent().find("button.down");

            $(this).toggleClass("down");
            down.toggleClass("down");
        });

        $(".menu-tab:not(:last-child, :first-child)").on("click", function () {
            $(".menu-tab.selected").toggleClass("selected");
            $(this).toggleClass("selected");
        });
    }
}

function init() {
    AccordionSupport.init();
    ButtonSwitchSupport.init();
}

$(document).ready(() => {
    client.run();
    init();

    document.body.addEventListener("clientInitAccordion", function (e) {
        AccordionSupport.init();
    });
});
