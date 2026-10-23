export const CATEGORY_ICONS = {
    groceries: "fa-solid fa-cart-shopping",
    transport: "fa-solid fa-car",
    restaurant: "fa-solid fa-utensils",
    entertainment: "fa-solid fa-film",
    internet: "fa-solid fa-wifi",
    mobile: "fa-solid fa-mobile-screen-button",
    household: "fa-solid fa-bolt",
    services: "fa-solid fa-screwdriver-wrench",
    salary: "fa-solid fa-briefcase",
    gift: "fa-solid fa-gift",
    circle: "fa-solid fa-circle",
} as const;

export type CategoryIcon = keyof typeof CATEGORY_ICONS;

export const DEFAULT_CATEGORY_ICON: CategoryIcon = "circle";

export function categoryIcon(key?: string | null): string {
    const found = key ? CATEGORY_ICONS[key as CategoryIcon] : undefined;

    return found ?? CATEGORY_ICONS[DEFAULT_CATEGORY_ICON];
}
