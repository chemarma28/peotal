package finance;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;

public class Watcher extends Thread {
    private final ServletContextHandler handler;
    private final WatchService watcher;

    public Watcher(final Path directory, final ServletContextHandler handler) throws IOException {
        this.handler = handler;
        this.watcher = FileSystems.getDefault().newWatchService();

        directory.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
    }

    @Override
    public void run() {
        while (true) {
            try {
                final WatchKey key = watcher.poll();

                if (null == key)
                    System.out.println("No events!");
                else
                    for (WatchEvent<?> e : key.pollEvents()) {
                        System.out.println(e.context());
                    }

                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void scan(final WatchService watcher) throws Exception {
        WatchKey key = watcher.take();

        for (WatchEvent<?> event : key.pollEvents()) {
            System.out.printf("File changed: %s\n", event.context());

            handler.stop();
            handler.start();
        }
    }
}
