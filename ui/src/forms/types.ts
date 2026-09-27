export type SelectOption<T> = {
    name: string;
    record: T;
};

export type SelectionKey<T> = (option: SelectOption<T>) => unknown;
