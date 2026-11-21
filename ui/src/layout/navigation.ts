import type { IconDefinition } from "@fortawesome/free-solid-svg-icons";
import { createContext } from "svelte";

export type NavigationItem = {
    to: string;
    icon: IconDefinition;
    label: string;
};

export const [getNavigationItems, setNavigationItems] = createContext<NavigationItem[]>();
