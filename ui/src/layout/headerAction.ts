import type { IconDefinition } from "@fortawesome/free-solid-svg-icons";

export type HeaderAction = {
    icon: IconDefinition;
    label: string;
    onClick: () => void;
};

export type HeaderActionStore = {
    set: (action?: HeaderAction) => void;
};

export const HEADER_ACTION = Symbol("header-action");
