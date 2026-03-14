window.addEventListener("load", ready);

function ready(e) {
    accordionReady();
}

function accordionReady() {
    const aitems = document.querySelectorAll(".accordion-item");

    for (let aitem of aitems) {
        const h = aitem.querySelector(".accordion-header");
        const m = aitem.querySelector(".accordion-collapsable");

        m.style.maxHeight = isCollapsed(aitem) ? `0px` : `${m.scrollHeight}px`;

        h.addEventListener("selectstart", (e) => e.preventDefault());
        h.addEventListener("mousedown", (e) => e.preventDefault());

        h.addEventListener("click", (e) => {
            if (isCollapsed(aitem)) {
                aitem.classList.remove("collapsed");
                uncollapse(aitem);
            } else {
                collapse(aitem);
                aitem.classList.add("collapsed");
            }
        });
    }
}

function isCollapsed(aitem) {
    return aitem.classList.contains("collapsed");
}

function collapse(aitem) {
    aitem.querySelector(".accordion-collapsable").style.maxHeight = "0px";
}

function uncollapse(aitem) {
    const main = aitem.querySelector(".accordion-collapsable");

    main.style.maxHeight = `${main.scrollHeight}px`;
}

function showMakeExpenseDialog() {
    const $dialog = dialog(
        "make-expense-dialog",
        "New expense card",
        function ($body) {
            const dialog = this.get(0);

            $.get("/expense.html").done((response) => {
                $body.html(response);

                dialog.showModal();
            });
        },
    );

    $dialog.footer.append(
        $(`<button>Make expense card</button>`).click((e) => {
            const form = $dialog.self.find("form");

            $.ajax({
                url: "/expense.html",
                type: "POST",
                dataType: "html",
                data: form.serialize(),
            }).done((response) => {
                $dialog.body.html(response);
            });
        }),
    );
}

function dialog(id, title, initCallback) {
    const $dialog = $(`<dialog id=${id}></dialog>`);
    const dialog = $dialog.get(0);

    const $exit = $("<div></div>").append(
        $("<button></button>")
            .append($("<span class='cross-icon-32'></span>"))
            .click((e) => {
                dialog.close();

                setTimeout(() => {
                    $dialog.remove();
                });
            }),
    );

    const $header = $(
        `<div class='dialog-header'>
            <div>${title}</div>
        </div>`,
    ).append($exit);

    const $body = $("<div class='dialog-body'>Future Dialog Body</div>");

    const $footer = $("<div class='dialog-footer'></div>");

    $("body").prepend($dialog.append($header).append($body).append($footer));

    initCallback.call($dialog, $body);

    return {
        self: $dialog,
        header: $header,
        body: $body,
        footer: $footer,
    };
}
