export const client = {
    prefix: "Client-Context-",

    run: function () {
        htmx.on("htmx:beforeSend", function (e) {
            const xhr = e.detail.xhr;

            const q = new URLSearchParams(window.location.search);

            const params = {};
            const headers = {};

            for (const [key, value] of q.entries())
                params[key] = value;

            for (let i = 0; i < sessionStorage.length; i++) {
                const key = sessionStorage.key(i);

                if (key.startsWith(prefix))
                    headers[key] = sessionStorage.getItem(key);
            }

            if (params["set"] === "true") {
                for (let k of params)
                    headers[prefix + k] = params[k];
            }

            for (let header of headers)
                xhr.setRequestHeader(header, headers[header]);
        });

        htmx.on("htmx:afterOnLoad", function (e) {
            const xhr = e.detail.xhr;

            xhr.getAllResponseHeaders().split("\r\n").forEach(header => {
                let [name, value] = header.split(": ");

                if (name.startsWith(prefix))
                    sessionStorage.setItem(name, value)
            });
        })
    }
}

