import { TransactionType, type Category } from "@ui/types";
import type { CategoryIcon } from "./icons";

export type DefaultCategory = Omit<Category, "id" | "ordinal" | "icon"> & {
    icon: CategoryIcon;
};

export const DEFAULT_CATEGORIES: DefaultCategory[] = [
    {
        name: "Groceries",
        operation: TransactionType.EXPENSE,
        color: "#7cb342",
        icon: "groceries",
    },
    {
        name: "Transport",
        operation: TransactionType.EXPENSE,
        color: "#5c6bc0",
        icon: "transport",
    },
    {
        name: "Restaurant",
        operation: TransactionType.EXPENSE,
        color: "#ef6c00",
        icon: "restaurant",
    },
    {
        name: "Internet/Mobile",
        operation: TransactionType.EXPENSE,
        color: "#29b6f6",
        icon: "internet",
    },
    {
        name: "Household",
        operation: TransactionType.EXPENSE,
        color: "#8d6e63",
        icon: "household",
    },
    {
        name: "Services",
        operation: TransactionType.EXPENSE,
        color: "#607d8b",
        icon: "services",
    },
    {
        name: "Salary",
        operation: TransactionType.INCOME,
        color: "#2e7d32",
        icon: "salary",
    },
    {
        name: "Gifts",
        operation: TransactionType.INCOME,
        color: "#c2185b",
        icon: "gift",
    },
];
