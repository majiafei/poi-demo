package com.yyt.poi;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Master {

    public static void main(String[] args) {
/*        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("ha", "xiaoming");
        System.out.println(1 << 30);*/

        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int x = 200;

        int a = x / 26;
        if (a == 0) {
            System.out.println(str.charAt(x % 26 - 1));
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < a; i++) {
                stringBuilder.append(str.charAt(a));
            }
            stringBuilder.append(str.charAt(x % 26 - 1));

            System.out.println(String.format("%3s", stringBuilder.toString()));
        }

        int[] xxxx = xxxx(100);
        for (int i = 0; i < xxxx.length; i++) {
            System.out.println(xxxx[i]);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < xxxx.length; i++) {
            if (xxxx[i] != 0) {
                stringBuilder.append(str.charAt(xxxx[i]));
            }
        }

        stringBuilder.append(str.charAt(100 % 26 - 1));
        System.out.println(stringBuilder);
        int len = 4 - stringBuilder.length();
        StringBuilder sss = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sss.append("A");
        }
        System.out.println(sss.append(stringBuilder));
    }

    public static int[] xxxx(int s) {
        int a = s / 26;
        if (a == 0) {
            return new int[]{a};
        }
        int b[] = new int[a];
        int i = 0;
        while (s > 0) {
            s /= 26;
            b[i] = s;
            i++;
        }

        return b;
    }

}
