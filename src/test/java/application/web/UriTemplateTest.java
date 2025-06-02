package application.web;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import finance.guice.routing.UriTemplate;

public class UriTemplateTest {
    private final String pattern1 = "/year/{year}/month/{month}/expense/cards";
    private UriTemplate uriTemplate1 = new UriTemplate(pattern1);

    private final String pattern2 = "/annual/{annual}";
    private final UriTemplate uriTemplate2 = new UriTemplate(pattern2);

    @Test
    public void testMatching() {
        Map<String, String> params = uriTemplate1.match("/year/2025/month/7/expense/cards");

        Assertions.assertTrue(params.containsKey("year"));
        Assertions.assertEquals("2025", params.get("year"));

        Assertions.assertTrue(params.containsKey("month"));
        Assertions.assertEquals(params.get("month"), "7");

        Map<String, String> p = uriTemplate2.match("/annual/2010");
        Assertions.assertTrue(p.containsKey("annual"));
        Assertions.assertEquals("2010", p.get("annual"));
    }

    @Test
    public void testMatches() {
        Assertions.assertTrue(uriTemplate1.matches("/year/2025/month/7/expense/cards"));

        Assertions.assertFalse(uriTemplate1.matches("/year/2025/month/some/7/expense/cards"));

        Assertions.assertTrue(uriTemplate2.matches("/annual/102"));

        Assertions.assertFalse(uriTemplate2.matches("/annual"));

        Assertions.assertFalse(uriTemplate2.matches("/annual/"));

        Assertions.assertFalse(uriTemplate2.matches("/annual/2025/"));

        Assertions.assertFalse(uriTemplate2.matches("/annual/2025/month"));
    }
}
