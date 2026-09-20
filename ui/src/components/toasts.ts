import { writable } from "svelte/store";

export enum ToastVariant {
    SUCCESS = "success",
    WARNING = "warning",
    ERROR = "error",
}

export type Toast = {
    id: number;
    variant: ToastVariant;
    title: string;
    message?: ToastMessage;
    duration: number;
    timer?: number;
};

export type ToastMessage = {
    payload: string;
    hidden?: boolean;
};

export const toasts = writable<Array<Toast>>([]);

export function showToast(
    variant: ToastVariant,
    title: string,
    options?: { message?: ToastMessage; duration?: number },
): Toast {
    const duration = options?.duration ?? 2500;

    const toast: Toast = {
        id: new Date().getTime(),
        variant: variant,
        title: title,
        message: options?.message,
        duration: duration,
    };

    toasts.update((toasts) => [...toasts, toast]);

    if (0 < duration)
        toast.timer = setTimeout(() => {
            hideToast(toast);
        }, duration);

    return toast;
}

export function hideToast(toast: Toast) {
    toasts.update((toasts) => toasts.filter((x) => x.id !== toast.id));
}
