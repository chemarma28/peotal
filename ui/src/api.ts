export const headers = {
    "Content-Type": "application/json; charset=UTF-8",
    "Accept": "application/json",
} as HeadersInit;

type Body = object | string | FormData | File;

class Api {
    prefix = "api";

    request(path: string, init?: RequestInit | { body?: Body, headers?: HeadersInit }): Promise<Response> {
        if (path.startsWith('/')) throw new Error("Invalid path: " + path)

        path = this.prefix + "/" + path;

        const body = init?.body;

        return fetch(path, {
            ...init,
            body: body instanceof File || body instanceof FormData || typeof body == "string" ? body : body && JSON.stringify(body),
            headers: init?.headers ?? headers
        }).catch(this.handleFetchFailure) as Promise<Response>;
    }

    async requestJson(path: string, init?: RequestInit | { body?: Body, headers?: HeadersInit }) {
        return this.request(path, init).then(this.extractJsonHandlingErrors);
    }

    private async extractJsonHandlingErrors(response: Response): Promise<any> {
        let data: any;

        try {
            data = response.status == 204 ? await response.text() : await response.json();
        } catch (e) {
            console.error("Not a JSON", e);
            throw { message: "errors.notJson" };
        }

        if (response.status < 200 || response.status >= 400) {
            data.message = data.message || data.reason;

            throw data;
        }

        return data;
    }

    private handleFetchFailure(error: any) {
        console.error(error);

        throw error;
    }

    get<T>(path: string): Promise<T> {
        return this.requestJson(path);
    }

    post<T>(path: string, body?: Body, headers?: HeadersInit): Promise<T> {
        return this.requestJson(path, { method: "POST", body, headers });
    }

    put<T>(path: string, body?: Body, headers?: HeadersInit): Promise<T> {
        return this.requestJson(path, { method: "PUT", body, headers });
    }

    delete(path: string) {
        return this.requestJson(path, { method: "DELETE" });
    }

    patch(path: string, body: object) {
        return this.requestJson(path, { method: "PATCH", body });
    }
}

export default new Api;