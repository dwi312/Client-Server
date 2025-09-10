package view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.Mahasiswa;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * Console UI: menampilkan menu, menerima input, berkomunikasi dengan server via TCP.
 *
 * Protocol:
 * - Kirim 1 baris: "METHOD /path"
 * - Jika POST/PUT: kirim 1 baris berisi JSON body
 * - Terima 1 baris JSON response dari server
 */
public class ConsoleUI {
    private final String host;
    private final int port;
    private final Gson gson = new Gson();
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            return true;
        } catch (IOException e) {
            System.err.println("Cannot connect to server: " + e.getMessage());
            return false;
        }
    }

    public void run() {
        if (!connect()) return;
        System.out.println("Connected to server " + host + ":" + port);
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1": // list
                        sendAndPrint("GET /mahasiswa", null);
                        break;
                    case "2": // create
                        Mahasiswa m = inputMahasiswa(true);
                        String body = gson.toJson(m);
                        sendAndPrint("POST /mahasiswa", body);
                        break;
                    case "3": // get by nim
                        System.out.print("NIM: ");
                        String nim = scanner.nextLine().trim();
                        sendAndPrint("GET /mahasiswa/" + nim, null);
                        break;
                    case "4": // update
                        System.out.print("NIM to update: ");
                        String nimUp = scanner.nextLine().trim();
                        Mahasiswa mu = inputMahasiswa(false);
                        mu.setNim(nimUp);
                        sendAndPrint("PUT /mahasiswa/" + nimUp, gson.toJson(mu));
                        break;
                    case "5": // delete
                        System.out.print("NIM to delete: ");
                        String nimDel = scanner.nextLine().trim();
                        sendAndPrint("DELETE /mahasiswa/" + nimDel, null);
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Pilihan tidak dikenal.");
                }
            } catch (IOException e) {
                System.err.println("IO error: " + e.getMessage());
                running = false;
            }
        }
        close();
        System.out.println("Client exit.");
    }

    private void printMenu() {
        System.out.println("\n===== MAHASISWA APP (TCP Client) =====");
        System.out.println("1. List semua mahasiswa");
        System.out.println("2. Tambah mahasiswa");
        System.out.println("3. Lihat mahasiswa by NIM");
        System.out.println("4. Update mahasiswa (by NIM)");
        System.out.println("5. Delete mahasiswa (by NIM)");
        System.out.println("0. Exit");
        System.out.print("Pilih> ");
    }

    private Mahasiswa inputMahasiswa(boolean askNim) {
        String nim = null;
        if (askNim) {
            System.out.print("NIM: ");
            nim = scanner.nextLine().trim();
        }
        System.out.print("Nama: ");
        String nama = scanner.nextLine().trim();
        System.out.print("Jurusan: ");
        String jurusan = scanner.nextLine().trim();
        System.out.print("Angkatan (tahun): ");
        int angkatan = 0;
        try {
            angkatan = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ignored) {}
        if (askNim) return new Mahasiswa(nim, nama, jurusan, angkatan);
        return new Mahasiswa(null, nama, jurusan, angkatan);
    }

    private void sendAndPrint(String requestLine, String body) throws IOException {
        // kirim
        out.write(requestLine);
        out.newLine();
        if (body != null) {
            out.write(body);
            out.newLine();
        }
        out.flush();

        // terima response (satu baris JSON)
        String jsonResp = in.readLine();
        if (jsonResp == null) {
            System.err.println("Server closed connection.");
            return;
        }

        // parse response: we expect structure {status, message, data}
        Type mapType = new TypeToken<java.util.Map<String, Object>>(){}.getType();
        java.util.Map<String, Object> resp = gson.fromJson(jsonResp, mapType);
        Double statusD = (Double) resp.get("status"); // Gson parses numbers as Double
        int status = statusD.intValue();
        String message = (String) resp.get("message");
        Object data = resp.get("data");

        System.out.println("Status: " + status + " - " + message);
        if (data != null) {
            // if data is a list or object, print prettily
            String pretty = gson.toJson(data);
            // try to determine content: list of mahasiswa or single object
            if (pretty.startsWith("[")) {
                // list
                Type listType = new TypeToken<List<Mahasiswa>>(){}.getType();
                List<Mahasiswa> list = gson.fromJson(pretty, listType);
                System.out.println("== List Mahasiswa ==");
                for (Mahasiswa m : list) {
                    System.out.println(m);
                }
            } else {
                Mahasiswa m = gson.fromJson(pretty, Mahasiswa.class);
                System.out.println("== Mahasiswa ==");
                System.out.println(m);
            }
        }
    }

    private void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }
}
