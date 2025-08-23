const Accordion = {
    init: function () {
        const self = this;

        $(".accordion")
            .find(".accordion-body")
            .each(function () {
                self.setAccordionBodyMaxHeight($(this).parent());
            });

        $(".accordion")
            .find(".accordion-header")
            .on("click", function (e) {
                self.handleClickCallback($(this))(e);
            });
    },

    handleClickCallback: function ($accordionHeader) {
        const self = this;

        return (e) => {
            const last = $accordionHeader
                .closest(".accordion-item")
                .toggleClass("collapsed");

            self.setAccordionBodyMaxHeight(last);
        };
    },

    setAccordionBodyMaxHeight: function ($accordionItem) {
        $accordionItem.children(".accordion-body").each(function () {
            if ($accordionItem.hasClass("collapsed"))
                $(this).css({
                    maxHeight: "0px",
                });
            else
                $(this).css({
                    maxHeight: $(this).prop("scrollHeight"),
                });
        });
    },

    refresh: function ($accordionHeader) {
        const self = this;

        $accordionHeader.off();

        $accordionHeader.on("click", function (e) {
            self.handleClickCallback($(this))(e);
        });
    },
};

const RadioButton = {
    init: function () {
        $(".button-switch")
            .find("button")
            .on("click", function () {
                const down = $(this).parent().find("button.down");

                $(this).toggleClass("down");
                down.toggleClass("down");
            });

        $(".menu-tab:not(:last-child, :first-child)").on("click", function () {
            $(".menu-tab.selected").toggleClass("selected");
            $(this).toggleClass("selected");
        });
    },
};

var Modal = {
    init: function () {
        $("body").on("modal:closed", function (e) {
            $(".modal").each(function () {
                Modal.close($(this).attr("id"));
            });
        });
    },

    open: function (id, url) {
        htmx.ajax("GET", url, {
            target: "#modal-root",
            swap: "afterbegin",
        }).then(() => {
            const $modal = $(`#${id}`);

            $modal.addClass("visible");

            setTimeout(() => {
                $modal.addClass("fade-in");
            }, 0);
        });
    },

    close: function (id) {
        const $modal = $(`#${id}`);

        $modal.removeClass("fade-in");

        setTimeout(() => {
            $modal.remove();
        }, 400);
    },
};

$(document).ready(() => {
    Accordion.init();
    RadioButton.init();
    Modal.init();

    $(".menu-tab:not(:last-child, :first-child)").on("click", function () {
        $(".menu-tab").removeClass("tab-selected");
        $(this).addClass("tab-selected");
    });
});
