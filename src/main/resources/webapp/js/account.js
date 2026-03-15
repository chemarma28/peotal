window.addEventListener("load", ready);

function ready(e) {
    accordionSetup();
}

function accordionSetup() {
    $(".accordion-item").each(function () {
        const $header = $(this).find(".accordion-header");
        const $body = $(this).find(".accordion-collapsable");

        const maxHeight = () => {
            if ($(this).hasClass("collapsed")) return "0px";

            return `${$body.prop("scrollHeight")}px`;
        };

        $body.css("maxHeight", maxHeight);

        $header.on("selectstart", (e) => e.preventDefault());
        $header.on("mousedown", (e) => e.preventDefault());

        $header.on("click", (e) => {
            $(this).toggleClass("collapsed");
            $body.css("maxHeight", maxHeight);
        });
    });
}

function showMakeIncomeDialog() {
    const $dialog = dialog(
        "dialog",
        "New income card",
        requestDialogBody("/secure/income.html"),
    );

    $dialog.footer.append(
        $(`<button>Make income card</button>`).click((e) => {
            const form = $dialog.self.find("form");

            $.ajax({
                url: "/secure/income.html",
                type: "POST",
                dataType: "html",
                data: form.serialize(),
                success: handleRedirect($dialog),
                error: handleConflict($dialog),
            });
        }),
    );
}

function showMakeExpenseDialog() {
    const $dialog = dialog(
        "dialog",
        "New income card",
        requestDialogBody("/secure/expense.html"),
    );

    $dialog.footer.append(
        $(`<button>Make expense card</button>`).click((e) => {
            const form = $dialog.self.find("form");

            $.ajax({
                url: "/secure/expense.html",
                type: "POST",
                dataType: "html",
                data: form.serialize(),
                statusCode: {
                    201: handleRedirect($dialog),
                    409: handleConflict($dialog),
                    500: handleInternalErrors($dialog),
                },
            });
        }),
    );
}

function requestDialogBody(url) {
    return ($dialog) => {
        $.get(url).done((response) => {
            $dialog.body.html(response);

            $dialog.element.showModal();
        });
    };
}

function handleRedirect($dialog) {
    return (resp, status, xhr) => {
        $dialog.element.close();

        redirect(resp, status, xhr);
    };
}

function redirect(resp, status, xhr) {
    const location = xhr.getResponseHeader("Location");

    location
        ? window.location.assign(location)
        : console.error("Expecting Location HTTP Header specified.");
}

function handleConflict($dialog) {
    return (xhr, status, error) => {
        $dialog.body.html(xhr.responseText);
    };
}

function handleInternalErrors($dialog) {
    return (xhr, status, error) => {
        $dialog.body.html(xhr.responseText);
    };
}

function dialog(id, title, initCallback) {
    const $dialog = $(`<dialog id=${id}></dialog>`);
    const dialog = $dialog.get(0);

    const $exit = $("<div></div>").append(
        $("<button></button>")
            .append($("<span class='fa fa-lg fa-xmark'></span>"))
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

    const $container = $dialog.append($header).append($body).append($footer);

    if ($("body").has("dialog").length)
        $("body").find("dialog").replaceWith($container);
    else $("body").prepend($container);

    const result = {
        self: $dialog,
        element: dialog,
        header: $header,
        body: $body,
        footer: $footer,
    };

    initCallback.call(undefined, result);

    return result;
}

function deleteCard(e) {
    const row = $(e.target).closest("tr");
    const type = row.data("type");
    const id = row.data("id");

    $.ajax({
        url: `/secure/card?id=${id}&type=${type}`,
        type: "DELETE",
        success: redirect,
    });
}
