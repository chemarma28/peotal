package finance.guice.modules;

import static java.util.Collections.emptyList;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.ServletModule;

import jakarta.servlet.http.HttpServlet;

public class GuiceServletDiscoveryModule extends ServletModule {
    private final Logger logger = LoggerFactory.getLogger(GuiceServletDiscoveryModule.class);

    @SuppressWarnings("rawtypes")
    private final Set<Class> servlets = new HashSet<>();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void setupServletsPaths(final String packageName) {
        final List<Class> classes = discover(packageName);

        for (Class cl : classes) {
            if (cl.isAnnotationPresent(HttpServletPath.class)) {
                final HttpServletPath annotation = (HttpServletPath) cl.getAnnotation(HttpServletPath.class);
                final String urlPattern = annotation.value();

                serve(urlPattern).with(cl);
            }
        }
    }

    @SuppressWarnings({ "rawtypes" })
    private List<Class> discover(final String packageName) {
        try {
            new ServletsDiscovery(packageName).walk();

            return new ArrayList<>(servlets);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return emptyList();
    }

    private final class ServletsDiscovery extends SimpleFileVisitor<Path> {
        private final static Path classpath = path(ServletsDiscovery.class.getClassLoader().getResource("."));
        private final Path start;

        private static Path path(final URL url) {
            if (null == url)
                return null;

            try {
                return Paths.get(url.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            return null;
        }

        private Path packageNameToPath(final String packageName) {
            final URL url = getClass().getClassLoader().getResource(packageName.replaceAll("[.]", "/"));

            return path(url);
        }

        ServletsDiscovery(final String packageName) {
            start = packageNameToPath(packageName);
        }

        void walk() throws IOException {
            Files.walkFileTree(start, this);
        }

        @SuppressWarnings("unchecked")
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Objects.requireNonNull(file);
            Objects.requireNonNull(attrs);

            if (attrs.isRegularFile() && file.toString().endsWith(".class")) {
                final String name = classpath.relativize(file)
                        .toString()
                        .replace(".class", "")
                        .replaceAll("/", ".")
                        .replaceAll("\\\\", ".");
                try {
                    @SuppressWarnings("rawtypes")
                    Class cl = Class.forName(name);

                    if (HttpServlet.class.isAssignableFrom(cl) && cl.isAnnotationPresent(HttpServletPath.class)) {
                        servlets.add(cl);

                        logger.debug("New servlet detected: {}", cl.getName());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            return FileVisitResult.CONTINUE;
        }
    }
}
