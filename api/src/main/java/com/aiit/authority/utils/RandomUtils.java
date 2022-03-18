package com.aiit.authority.utils;

import java.util.Random;

public class RandomUtils {

    private static final Random RANDOM = new Random();

    public static Integer randomInt() {
        return RANDOM.nextInt(100);
    }

    public static Integer randomMax() {
        return RANDOM.nextInt(Integer.MAX_VALUE);
    }

    public static String randomString() {
        return Integer.toString(randomMax());
    }

    public static String randomJson() {
        return JsonUtils.toJson(randomMax());
    }
}
