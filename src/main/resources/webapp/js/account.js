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

function overlay() {
    const overlay = document.createElement("div");
}

function dialog() {
    document.getElementById("my-modal").showModal();
}

function closeDialog() {
    document.getElementById("my-modal").close();
}
