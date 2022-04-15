package org.example;

import java.util.NoSuchElementException;
import java.util.Random;

/**
 * @author Joey
 * @version v1.0.0
 * @ClassName GenerateRandomSeedMain
 * @since 2022/4/15 08:08
 **/
public class GenerateRandomSeedMain {

    public static void main(String[] args) {
        String goal = "script";
        long seed = generateSeed(goal, Long.MIN_VALUE, Long.MAX_VALUE);
        System.out.println(String.format("The seed is: [%d].", seed));
    }

    public static long generateSeed(String goal, long start, long finish) {
        char[] input = goal.toCharArray();
        char[] pool = new char[input.length];
        label:
        for (long seed = start; seed < finish; seed++) {
            Random random = new Random(seed);

            for (int i = 0; i < input.length; i++) {
                pool[i] = (char) (random.nextInt(27) + '`');
            }

            if (random.nextInt(27) == 0) {
                for (int i = 0; i < input.length; i++) {
                    if (input[i] != pool[i])
                        continue label;
                }
                return seed;
            }
        }
        throw new NoSuchElementException("Sorry :/");
    }
}
