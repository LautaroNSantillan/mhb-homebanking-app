package com.mindhub.homeBanking.utilities;

import java.util.Random;

public final class CardUtils {
    //generateCardDigits
    public static String getCardNumbers() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(String.format("%04d", random.nextInt(10000)));
            if (i < 3) {
                sb.append("-");
            }
        }
        return sb.toString();
    }
    //generateCvv
    public static String getCvv() {
        Random random = new Random();
        int randomNumber = random.nextInt(1000);
        return String.format("%03d", randomNumber);
    }
}
