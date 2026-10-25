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
    health: "fa-solid fa-heart-pulse",
    education: "fa-solid fa-graduation-cap",
    travel: "fa-solid fa-plane",
    pets: "fa-solid fa-paw",
    cafe: "fa-solid fa-mug-hot",
    shopping: "fa-solid fa-bag-shopping",
    utilities: "fa-solid fa-plug",
    rent: "fa-solid fa-house",
    savings: "fa-solid fa-piggy-bank",
    bonus: "fa-solid fa-award",
    fitness: "fa-solid fa-dumbbell",
    beauty: "fa-solid fa-scissors",
    clothes: "fa-solid fa-shirt",
    electronics: "fa-solid fa-laptop",
    insurance: "fa-solid fa-shield-halved",
    taxes: "fa-solid fa-file-invoice-dollar",
    childcare: "fa-solid fa-baby",
    charity: "fa-solid fa-hand-holding-heart",
    subscription: "fa-solid fa-repeat",
    circle: "fa-solid fa-circle",
} as const;

export type CategoryIcon = keyof typeof CATEGORY_ICONS;

export const DEFAULT_CATEGORY_ICON: CategoryIcon = "circle";

export function categoryIcon(key?: string | null): string {
    const found = key ? CATEGORY_ICONS[key as CategoryIcon] : undefined;

    return found ?? CATEGORY_ICONS[DEFAULT_CATEGORY_ICON];
}
