import api from "@ui/api";
import { Account } from "@ui/types";
import { writable } from "svelte/store";

export const auth = writable<Account | undefined>();

export async function loadAccount(): Promise<Account | undefined> {
    try {
        const account = await api.get<Account>("auth/me");
        auth.set(account);
        return account;
    } catch {
        auth.set(undefined);
        return undefined;
    }
}
