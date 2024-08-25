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
