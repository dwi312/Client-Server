package com.app.server;

import com.app.server.core.Request;
import com.app.server.core.Router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Entry point server: melayani banyak client dengan Thread Pool.
 */
public class ServerMain {
    private static final int PORT = 8080;
    private static final int MAX_THREADS = 50;

    public static void main(String[] args) {
        Router router = new Router();
        ExecutorService pool = Executors.newCachedThreadPool(); // scalable
        System.out.println("Server starting on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket client = serverSocket.accept();
                Request handler = new Request(client, router);
                pool.execute(handler);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }
}
