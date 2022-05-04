package com.dh.simulator;

import com.dh.simulator.util.DHUtil;
import com.dh.simulator.util.DTConstant;

import java.io.*;
import java.net.Socket;

/**
 * @author Joey
 * @version v1.0.0
 * @ClassName Client
 * @since 2022/5/2 23:11
 **/
public class Client {

    /**
     * @author Joey
     * @version v1.0.0
     * @ClassName Client
     * @since 2022/5/3 20:51
     **/
    public static void main(String[] args) throws IOException {
        System.out.println("[client]Connecting to [" + DTConstant.HOST + "] on port [" + DTConstant.PORT + "]......");
        Socket socket = new Socket(DTConstant.HOST, DTConstant.PORT);
        System.out.println("[client]Just connected to [" + socket.getRemoteSocketAddress() + "].");
        System.out.println("========================================");

        double sharedKey = greeting(socket);
        interact("Hello, Joey", sharedKey, socket);
    }

    private static double greeting(Socket socket) throws IOException {
        // 公共加密因子g，生成元
        double g = DHUtil.getRandom(DTConstant.MIN_G, DTConstant.MAX_G);
        System.out.println("[client]Generate by client: g = " + g);
        // 公共加密因子p，必须为大质数（至少是1024位，目前主流是2048位），以最小化被除数和除数具有相同公因数的可能性，这减少了模运算可能产生的值的数量。
        double p = DHUtil.getRandomPrime(DTConstant.MIN_P, DTConstant.MAX_P);
        System.out.println("[client]Generate by client: p = " + p);

        // Client私钥
        double clientPrivateKey = DHUtil.getRandom(DTConstant.MIN_KEY, DTConstant.MAX_KEY);
        System.out.println("[client]Generate by client: private key = " + clientPrivateKey);
        // Client公钥
        double clientPublicKey = Math.pow(g, clientPrivateKey) % p;
        System.out.println("[client]Generate by client: public key = " + clientPublicKey);

        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeUTF(Double.toString(g));
        dataOutputStream.writeUTF(Double.toString(p));
        dataOutputStream.writeUTF(Double.toString(clientPublicKey));

        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        double serverPublicKey = Double.parseDouble(dataInputStream.readUTF());
        System.out.println("[client]From server: public key = " + serverPublicKey);

        // a = client, b = server
        // (g ^ b mod p) ^ a mod p = (g ^ a mod p) ^ b mod p = g ^ ab mod p
        double sharedKey = Math.pow(serverPublicKey, clientPrivateKey) % p;
        System.out.println("[client]Generate by client: shared key = " + sharedKey);
        return sharedKey;
    }

    public static void interact(String plaintext, double sharedKey, Socket socket) throws IOException {
        String clientCiphertext = DHUtil.encrypt(plaintext, (int) sharedKey);
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeUTF(clientCiphertext);

        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        String serverCiphertext = dataInputStream.readUTF();
        String serverPlaintext = DHUtil.decrypt(serverCiphertext, (int) sharedKey);
        System.out.println("----------------------------------------");
        System.out.println("[client]Message from server: " + serverPlaintext);
    }
}
