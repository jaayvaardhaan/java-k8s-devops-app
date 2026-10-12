import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(
            new InetSocketAddress(8080), 0
        );

        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().toString();

                System.out.printf(
                    "[%s] %s %s from %s%n",
                    Instant.now(),
                    method,
                    path,
                    exchange.getRemoteAddress()
                );

                String response =
                    "<h1>Hello DevOps World! Version 2.0 is Live!</h1>";

                byte[] body = response.getBytes(StandardCharsets.UTF_8);

                exchange.getResponseHeaders().set(
                    "Content-Type", "text/html; charset=UTF-8"
                );

                exchange.sendResponseHeaders(200, body.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body);
                }
            }
        });

        System.out.println("Server started on port 8080...");
        server.start();
    }
}
