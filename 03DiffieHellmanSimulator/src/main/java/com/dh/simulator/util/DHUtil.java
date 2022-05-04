package com.dh.simulator.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Random;

/**
 * @author Joey
 * @version v1.0.0
 * @ClassName DHUtil
 * @since 2022/5/2 23:12
 **/
public class DHUtil {
    public static boolean isPrime(int num) {
        /*
         * 质数定义：只有1和它本身两个因数的自然数
         *
         * 1. 小于等于1或者是大于2的偶数，直接返回false
         * 2. 2直接返回true
         * 3. 从3开始算起(每次加2，截止为输入值的平方根)，每次输入值除以前者，若出现一个能除尽则直接返回false
         * 4. 全都除不尽，则为质数，返回true
         * */
        if (num <= 1 || num > 2 && num % 2 == 0) {
            return false;
        } else if (num == 2) {
            return true;
        }
        int sqrtNum = (int) Math.sqrt(num);
        for (int i = 3; i <= sqrtNum; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static int getRandomPrime(int min, int max) {
        int random = (int) (Math.random() * (max - min)) + min;
        for (int i = random; i >= min; i--) {
            if (isPrime(i)) {
                return i;
            }
        }
        return 0;
    }

    public static int getRandom(int min, int max) {
        int random = (int) (Math.random() * (max - min)) + min;
        return random;
    }

    public static String encrypt(String plaintext, int secret) throws UnsupportedEncodingException {
        byte[] ciphertextArray = plaintext.getBytes();
        for (int i = 0; i < ciphertextArray.length; i++) {
            ciphertextArray[i] = (byte) (ciphertextArray[i] ^ secret);
        }
        String result = Base64.getEncoder().encodeToString(ciphertextArray);
        return result;
    }

    public static String decrypt(String ciphertext, int secret) throws UnsupportedEncodingException {
        byte[] plaintextArray = Base64.getDecoder().decode(ciphertext);
        for (int i = 0; i < plaintextArray.length; i++) {
            plaintextArray[i] = (byte) (plaintextArray[i] ^ secret);
        }
        String result = new String(plaintextArray);
        return result;
    }
}
