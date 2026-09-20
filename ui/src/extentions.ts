export {};

declare global {
    interface Array<T> {
        first(): T;
        groupingBy(by: (el: T) => string | number): { by: string | number; entries: T[] }[];
    }

    interface Date {
        toIsoDate(): string;
    }
}

Array.prototype.first = function () {
    return this[0];
};

Array.prototype.groupingBy = function <T>(
    this: T[],
    by: (el: T) => string | number,
): { by: string | number; entries: T[] }[] {
    if (!this.length) return [];

    // Elements of array must be sorted
    const elements = [...this];

    let last = {
        by: by(elements.first()!),
        entries: [] as T[],
    };

    const result = [];

    for (let element of elements) {
        const k = by(element);

        if (k !== last.by) {
            result.push(last);

            last = {
                by: k,
                entries: [],
            };
        }

        last.entries.push(element);
    }

    result.push(last);

    return result;
};

Date.prototype.toIsoDate = function (): string {
    return this.toISOString().split("T").first();
};
