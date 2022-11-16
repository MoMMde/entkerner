package net.kerner.entkerner.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SshDaemon {
    private static final int PORT = 5501;
    private static final String SSH_VERSION = "2.0";
    private static final String IDENTIFICATION = "SSH-" +  SSH_VERSION + "entkernerSsh";
    private ServerSocket socket = new ServerSocket(PORT);

    public static void main(String[] args) throws IOException {
        new SshDaemon();
    }

    public SshDaemon() throws IOException {
        System.out.println("* Started SSH Daemon");
        while (true) {
            handleSshConnection(socket.accept());
        }
    }

    private void handleSshConnection(Socket socket) throws IOException {
        System.out.println("recv: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println(in.readLine());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.write(IDENTIFICATION);

    }

    private byte[] sendPacket(byte[] data) {
        byte[] packet = new byte[data.length];
        packet[3] = (byte) (data.length & 0xff);
        packet[2] = (byte) ((data.length >> 8) & 0xff);
        packet[1] = (byte) ((data.length >> 16) & 0xff);
        packet[0] = (byte) ((data.length >> 24) & 0xff);

        packet[4] = 11;

        return packet;
    }
}
