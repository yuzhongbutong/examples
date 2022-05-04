package com.dh.simulator;

import com.dh.simulator.util.DHUtil;
import com.dh.simulator.util.DTConstant;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Joey
 * @version v1.0.0
 * @ClassName Server
 * @since 2022/5/2 23:11
 **/
public class Server {
    
    /**
     * @author Joey
     * @version v1.0.0
     * @ClassName Server
     * @since 2022/5/3 20:51
     **/
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(DTConstant.PORT);
        System.out.println("[server]Waiting for client......");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("========================================");
            double sharedKey = greeting(socket);
            interact("yyds", sharedKey, socket);
        }
    }

    private static double greeting(Socket socket) throws IOException {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        // 公共加密因子g，生成元
        double g = Double.parseDouble(input.readUTF());
        System.out.println("[server]From client: g = " + g);
        // 公共加密因子p，必须为大质数（至少是1024位，目前主流是2048位），以最小化被除数和除数具有相同公因数的可能性，这减少了模运算可能产生的值的数量。
        double p = Double.parseDouble(input.readUTF());
        System.out.println("[server]From client: p = " + p);
        // Client公钥
        double clientPublicKey = Double.parseDouble(input.readUTF());
        System.out.println("[server]From client: public key = " + clientPublicKey);

        // Server私钥
        double serverPrivateKey = DHUtil.getRandom(DTConstant.MIN_KEY, DTConstant.MAX_KEY);
        System.out.println("[server]Generate by server: private key = " + serverPrivateKey);
        // Server公钥
        double serverPublicKey = Math.pow(g, serverPrivateKey) % p;
        System.out.println("[server]Generate by server: public key = " + serverPublicKey);

        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeUTF(Double.toString(serverPublicKey));

        // a = client, b = server
        // (g ^ b mod p) ^ a mod p = (g ^ a mod p) ^ b mod p = g ^ ab mod p
        double sharedKey = Math.pow(clientPublicKey, serverPrivateKey) % p;
        System.out.println("[server]Generate by server: shared key = " + sharedKey);
        return sharedKey;
    }

    public static void interact(String plaintext, double sharedKey, Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        String clientCiphertext = dataInputStream.readUTF();
        String clientPlaintext = DHUtil.decrypt(clientCiphertext, (int) sharedKey);
        System.out.println("----------------------------------------");
        System.out.println("[server]Message from client: " + clientPlaintext);

        String serverCiphertext = DHUtil.encrypt(plaintext, (int) sharedKey);
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeUTF(serverCiphertext);
    }
}
