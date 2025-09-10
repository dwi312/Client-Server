package com.app.server.core;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

/**
 * Handle satu koneksi client dalam Thread terpisah.
 *
 * Protocol (simple):
 * - Client kirim: satu baris REQUEST (e.g. "POST /mahasiswa")
 * - Jika POST/PUT: client kirim satu baris JSON body berikutnya
 * - Server membalas satu baris JSON Response (serialized Response object)
 *
 * Catatan: komunikasi berbasis baris (BufferedReader/Writer).
 */
public class Request implements Runnable {
    private final Socket clientSocket;
    private final Router router;
    private final Gson gson = new Gson();

    public Request(Socket clientSocket, Router router) {
        this.clientSocket = clientSocket;
        this.router = router;
    }

    @Override
    public void run() {
        String clientInfo = clientSocket.getRemoteSocketAddress().toString();
        System.out.println("Connected: " + clientInfo);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            String requestLine;
            // keep conversation until client closes connection
            while ((requestLine = in.readLine()) != null) {
                System.out.println("Request from " + clientInfo + ": " + requestLine);
                String body = null;
                // if method is POST or PUT, read next line as body (if available)
                if (requestLine.startsWith("POST") || requestLine.startsWith("PUT")) {
                    body = in.readLine();
                    System.out.println("Body: " + body);
                }
                Response resp = router.route(requestLine, body);
                String jsonResp = gson.toJson(resp);
                // send response and newline
                out.write(jsonResp);
                out.newLine();
                out.flush();
            }

        } catch (IOException e) {
            System.err.println("Connection closed / error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {}
            System.out.println("Disconnected: " + clientInfo);
        }
    }
}
