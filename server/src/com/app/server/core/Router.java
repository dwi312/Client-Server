package com.app.server.core;

import com.app.server.controller.MahasiswaController;
import com.app.server.model.Mahasiswa;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Router sederhana mem-parsing METHOD dan PATH,
 * memanggil controller yang sesuai.
 */
public class Router {
    private final MahasiswaController ctrl = new MahasiswaController();
    private final Gson gson = new Gson();

    /**
     * requestLine: "METHOD /path" . bodyJson optional
     */
    public Response route(String requestLine, String bodyJson) {
        try {
            if (requestLine == null || requestLine.trim().isEmpty()) {
                return new Response(400, "Empty request", null);
            }

            String[] parts = requestLine.trim().split("\\s+");
            if (parts.length < 2) return new Response(400, "Invalid request", null);

            String method = parts[0].toUpperCase();
            String path = parts[1];

            // paths: /mahasiswa  or /mahasiswa/{nim}
            if (path.equals("/mahasiswa")) {
                switch (method) {
                    case "GET":
                        return ctrl.list();
                    case "POST":
                        if (bodyJson == null) return new Response(400, "Missing body", null);
                        try {
                            Mahasiswa m = gson.fromJson(bodyJson, Mahasiswa.class);
                            return ctrl.create(m);
                        } catch (JsonSyntaxException e) {
                            return new Response(400, "Invalid JSON body", null);
                        }
                    default:
                        return new Response(405, "Method Not Allowed for /mahasiswa", null);
                }
            } else if (path.startsWith("/mahasiswa/")) {
                String nim = path.substring("/mahasiswa/".length());
                switch (method) {
                    case "GET":
                        return ctrl.get(nim);
                    case "PUT":
                        if (bodyJson == null) return new Response(400, "Missing body", null);
                        try {
                            Mahasiswa m = gson.fromJson(bodyJson, Mahasiswa.class);
                            // ensure nim in path matches body (if body set)
                            m.setNim(nim);
                            return ctrl.update(m);
                        } catch (JsonSyntaxException e) {
                            return new Response(400, "Invalid JSON body", null);
                        }
                    case "DELETE":
                        return ctrl.delete(nim);
                    default:
                        return new Response(405, "Method Not Allowed for /mahasiswa/{nim}", null);
                }
            } else {
                return new Response(404, "Not Found", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(500, "Internal server error: " + e.getMessage(), null);
        }
    }
}
