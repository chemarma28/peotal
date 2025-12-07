export const client = {
    prefix: "X-Client-",

    run: function () {
        htmx.on("htmx:beforeSend", function (e) {
            const xhr = e.detail.xhr;

            const headers = {};

            for (let i = 0; i < sessionStorage.length; i++) {
                const key = sessionStorage.key(i);

                if (key.startsWith(client.prefix))
                    headers[key] = sessionStorage.getItem(key);
            }

            for (const [header, value] of Object.entries(headers))
                xhr.setRequestHeader(header, value);
        });

        htmx.on("htmx:afterOnLoad", function (e) {
            const xhr = e.detail.xhr;

            xhr.getAllResponseHeaders().split("\r\n").forEach(header => {
                let [name, value] = header.split(": ");

                if (name.startsWith(client.prefix))
                    sessionStorage.setItem(name, value)
            });
        })
    }
}

