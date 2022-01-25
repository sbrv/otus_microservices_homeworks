package ru.sibirev.otus.microservice.simple_http_server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import java.util.logging.Logger;

public class SimpleHttpServer {
    private static final String LOG_TEMPLATE = "[%1$tF %1$tT] [%4$-7s] %5$s%n";
    private static final String LOG_MESSAGE_TEMPLATE = "[%s]:%-10s %s";
    private static final String TEXT_HTML_CONTEXT_TYPE = "text/html; charset=utf-8";
    private static final String JSON_CONTEXT_TYPE = "application/json; charset=utf-8";
    private static Logger logger;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", LOG_TEMPLATE);
        logger = Logger.getLogger(SimpleHttpServer.class.getName());
    }

    public static void main(String[] args) throws IOException {
        var server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/hello", httpExchange -> {
            log(httpExchange);
            String body = "SimpleHttpServer v.1 from " + InetAddress.getLocalHost();
            response(httpExchange, TEXT_HTML_CONTEXT_TYPE, body);
        });
        server.createContext("/health", httpExchange -> {
            log(httpExchange);
            var body = "{\"status\": \"OK\"}";
            response(httpExchange, JSON_CONTEXT_TYPE, body);
        });

        server.start();
        log("SimpleHttpServer started");
    }

    private static void log(HttpExchange httpExchange) throws UnknownHostException {
        var message = String.format(LOG_MESSAGE_TEMPLATE,
                httpExchange.getRequestMethod(), httpExchange.getRequestURI().toString(), InetAddress.getLocalHost());
        log(message);
    }

    private static void log(String message) {
        logger.info(message);
    }

    private static void response(HttpExchange httpExchange, String contentType, String body) throws IOException {
        try (OutputStream os = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().add("content-type", contentType);
            byte[] responseBytes = body.getBytes();
            httpExchange.sendResponseHeaders(200, responseBytes.length);
            os.write(responseBytes);
            os.flush();
        }
    }

}
