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
        this.accordion = accordion;

        accordion.find(ACCORDION_HEADER).on("click", function () {
            const section = $(this).parent().toggleClass("active");

            const body = section.find(ACCORDION_BODY);

            body.css({
                maxHeight: section.hasClass("active") ? `${body.prop("scrollHeight")}px` : "0px"
            })
        });
    }
}
