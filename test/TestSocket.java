import javax.net.ServerSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TestSocket extends Thread {
    private final int port;
    private final ServerSocket socket;
    private List<SocketClient> clients = new ArrayList<>();

    public TestSocket(int port) throws IOException {
        this.port = port;
        this.socket = ServerSocketFactory.getDefault().createServerSocket(port);
    }

    @Override
    public void run() {
        try {
            Socket client;
            while((client = socket.accept()) != null) {
                SocketClient so = new SocketClient(client) {
                    @Override
                    public void receive(String msg) {
                        System.out.println(msg);
                    }
                };
                clients.add(so);
                so.start();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private abstract static class SocketClient extends Thread {
        private Socket socket;
        private final PrintStream out;
        private final BufferedReader in;

        private SocketClient(Socket socket) throws IOException {
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void send(String str) {
            out.println(str);
            out.flush();
        }

        @Override
        public void run() {
            try {
                String line;

                while((line = in.readLine()) != null) {
                    receive(line);
                }

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public abstract void receive(String msg);
    }
}
