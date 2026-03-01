window.addEventListener("load", ready);

function ready(e) {
    accordionReady();
}

function accordionReady() {
    const aitems = document.querySelectorAll(".accordion-item");

    for (let aitem of aitems) {
        const h = aitem.querySelector(".accordion-header");
        const m = aitem.querySelector(".accordion-main");

        m.style.maxHeight = isCollapsed(aitem) ? `0px` : `${m.scrollHeight}px`;

        h.addEventListener("click", (e) => toggleBegin(aitem));
        m.addEventListener("transitionend", (e) => toggleEnd(aitem));
    }
}

function toggleBegin(aitem) {
    if (isCollapsed(aitem)) {
        aitem.classList.remove("collapsed");
        uncollapse(aitem);
    } else {
        collapse(aitem);

        // How to execute this when transition ends?
        aitem.classList.add("collapsed");
    }
}

function toggleEnd(aitem) {
    // if (isCollapsed(aitem))
    //     aitem.classList.add("collapsed");
}

function isCollapsed(aitem) {
    return aitem.classList.contains("collapsed");
}

function collapse(aitem) {
    aitem.querySelector(".accordion-main").style.maxHeight = "0px";
}

function uncollapse(aitem) {
    const main = aitem.querySelector(".accordion-main");

    main.style.maxHeight = `${main.scrollHeight}px`;
}
