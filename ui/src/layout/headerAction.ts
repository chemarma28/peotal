export type HeaderAction = {
    icon: string;
    label: string;
    onClick: () => void;
};

export type HeaderActionStore = {
    set: (action?: HeaderAction) => void;
};

export const HEADER_ACTION = Symbol("header-action");
