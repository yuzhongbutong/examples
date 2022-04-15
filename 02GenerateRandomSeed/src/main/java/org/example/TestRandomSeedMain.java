package org.example;

import com.google.common.primitives.Bytes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Joey
 * @version v1.0.0
 * @ClassName Test
 * @since 2022/4/15 09:37
 **/
public class TestRandomSeedMain {

    public static void main(String[] args) {
        long seed = -9223372032273901313L;
        String goal = getGoalString(seed);
        System.out.println(String.format("The string goal is: [%s].", goal));
        goal = getGoalByte(seed);
        System.out.println(String.format("The byte goal is: [%s].", goal));
    }

    private static String getGoalString(long seed) {
        Random random = new Random(seed);
        StringBuilder builder = new StringBuilder();
        while (true) {
            int k = random.nextInt(27);
            if (k == 0) {
                break;
            }
            builder.append((char)('`' + k));
        }
        return builder.toString();
    }

    private static String getGoalByte(long seed) {
        Random random = new Random(seed);
        List<Byte> list = new ArrayList<>();
        while (true) {
            int k = random.nextInt(27);
            if (k == 0) {
                break;
            }

            list.add((byte)('`' + k));
        }
        byte[] bytes = Bytes.toArray(list);
        return new String(bytes);
    }
}
