import type { IconDefinition } from "@fortawesome/free-solid-svg-icons";
import { createContext } from "svelte";

export type HeaderAction = {
    icon: IconDefinition;
    label: string;
    onClick: () => void;
};

export const [headerActionHolder, setHeaderActionHolder] = createContext<{
    bind: (action: HeaderAction | undefined) => void;
}>();
