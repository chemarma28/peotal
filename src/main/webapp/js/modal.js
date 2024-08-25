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
