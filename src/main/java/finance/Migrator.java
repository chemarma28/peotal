package finance;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Migrator {
    private static final DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final String changelogs;
    private final String root;

    public Migrator(final String changelogs, final String root) {
        this.changelogs = changelogs;
        this.root = root;
    }

    public void run(final String logsDir) throws IOException {
        final ProcessBuilder pb = new ProcessBuilder();
        pb.command("liquibase", "update", format("--changelog-file=%s", root));

        pb.directory(Paths.get(changelogs).toFile());

        report(pb, logsDir);

        pb.start();
    }

    public void report(final ProcessBuilder pb, final String logsDir) throws IOException {
        final Path logs = Paths.get(logsDir);

        if (!Files.exists(logs))
            Files.createDirectory(logs);

        final LocalDateTime now = LocalDateTime.now();
        final String log = format("run-%s-report.txt", f.format(now));
        final String error = format("run-%s-log.txt", f.format(now));

        pb.redirectOutput(logs.resolve(log).toFile());
        pb.redirectError(logs.resolve(error).toFile());
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        new Migrator("db/changelog", "changelog-root.xml").run("liquibase");
    }
}
