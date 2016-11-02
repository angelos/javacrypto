package crypto6.ssl1;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class SSL {
    public static void main(String[] args) throws Exception {
        // Initialize the Server Socket
        SSLServerSocketFactory sslServerSocketfactory =
                (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket =
                (SSLServerSocket) sslServerSocketfactory.createServerSocket(8443);
        SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();

        PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(sslSocket.getInputStream()));

        String input;
        while ((input = in.readLine()) != null) {
            out.println(input);
            System.out.println(input);
        }

        // IO cleanup omitted for readability
    }
}
