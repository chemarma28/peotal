# Peotal

Peotal is a lightweight, self-hosted income and expense tracker. It is built
primarily for personal use, but intentionally kept simple enough for anyone to
run on their own machine.

## Status

Peotal is currently in MVP development (version `0.1.0`). Authentication,
production packaging, and some planned features are not ready yet.

## Requirements

- JDK 21
- Node.js 24+
- npm

`JAVA_HOME` must point to JDK 21.

## Run in development

Use two terminals.

### Backend

```bash
export JAVA_HOME="/c/Program Files/Java/jdk-21"
./gradlew run
```

The backend is available at `http://localhost:9097`.

### Frontend

```bash
npm install
npm run dev
```

The frontend is available at `http://localhost:5173`.

The frontend dev server proxies `/api` to the backend. The H2 database is stored
in `~/.h2/dev`.

## Build checks

```bash
./gradlew test
npm run check
npm run build
```

`npm run build` currently produces `ui/dist`, but the backend does not serve the
built UI yet. Use the Vite dev server for local development.

## License

MIT
