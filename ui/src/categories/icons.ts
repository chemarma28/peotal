import {
    faAward,
    faBaby,
    faBagShopping,
    faBolt,
    faBriefcase,
    faCar,
    faCartShopping,
    faCircle,
    faDumbbell,
    faFileInvoiceDollar,
    faFilm,
    faGift,
    faGraduationCap,
    faHandHoldingHeart,
    faHeartPulse,
    faHouse,
    faLaptop,
    faMobileScreenButton,
    faMugHot,
    faPaw,
    faPiggyBank,
    faPlane,
    faPlug,
    faRepeat,
    faScissors,
    faScrewdriverWrench,
    faShieldHalved,
    faShirt,
    faUtensils,
    faWifi,
} from "@fortawesome/free-solid-svg-icons";

export const CategoryIcons = {
    groceries: faCartShopping,
    transport: faCar,
    restaurant: faUtensils,
    entertainment: faFilm,
    internet: faWifi,
    mobile: faMobileScreenButton,
    household: faBolt,
    services: faScrewdriverWrench,
    salary: faBriefcase,
    gift: faGift,
    health: faHeartPulse,
    education: faGraduationCap,
    travel: faPlane,
    pets: faPaw,
    cafe: faMugHot,
    shopping: faBagShopping,
    utilities: faPlug,
    rent: faHouse,
    savings: faPiggyBank,
    bonus: faAward,
    fitness: faDumbbell,
    beauty: faScissors,
    clothes: faShirt,
    electronics: faLaptop,
    insurance: faShieldHalved,
    taxes: faFileInvoiceDollar,
    childcare: faBaby,
    charity: faHandHoldingHeart,
    subscription: faRepeat,
    circle: faCircle,
} as const;

export type CategoryIconName = keyof typeof CategoryIcons;
export type CategoryIcon = CategoryIconName;

export const DEFAULT_CATEGORY_ICON: CategoryIcon = "circle";

export function categoryIconDefinition(key?: string | null) {
    const found = key ? CategoryIcons[key as CategoryIconName] : undefined;

    return found ?? CategoryIcons.circle;
}
