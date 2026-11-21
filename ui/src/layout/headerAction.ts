import type { IconDefinition } from "@fortawesome/free-solid-svg-icons";

export type HeaderAction = {
    icon: IconDefinition;
    label: string;
    onClick: () => void;
};
