package com.poc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.yaml.snakeyaml.Yaml;

public class HelloServlet extends HttpServlet {

    private final String greeting = loadGreeting();

    private static String loadGreeting() {
        try (InputStream in = HelloServlet.class.getResourceAsStream("/app.yml")) {
            Map<String, Object> cfg = new Yaml().load(in);
            return String.valueOf(cfg.getOrDefault("greeting", "Hello World"));
        } catch (IOException e) {
            return "Hello World";
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("/login");
            return;
        }
        String user = Html.escape((String) session.getAttribute("user"));
        resp.setContentType("text/html;charset=UTF-8");
        String body = "<h1 id=\"title\" style=\"color:" + Page.COLOR + ";\">" + Html.escape(greeting) + "</h1>"
          + "<p>Signed in as " + user + "</p>"
          + "<a href=\"/login\" id=\"logout\" style=\"color:" + Page.COLOR + ";\">Sign out</a>";
        resp.getWriter().write(Page.wrap("Hello", body));
    }
}
