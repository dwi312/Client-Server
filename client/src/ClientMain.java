

import view.ConsoleUI;

/**
 * Entry point client
 */
public class ClientMain {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8080;
        ConsoleUI ui = new ConsoleUI(host, port);
        ui.run();
    }
}
