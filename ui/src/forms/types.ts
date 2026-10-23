export type SelectOption<T> = {
    name: string;
    record: T;
    badge?: string | number;
};

export type SelectionKey<T> = (option: SelectOption<T>) => unknown;
