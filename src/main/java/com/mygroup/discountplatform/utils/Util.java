package com.mygroup.discountplatform.utils;

import org.springframework.stereotype.Component;

@Component
public class Util {

    public String rutNormalization(String rut){
        // remove dots
        rut = rut.replace(".", "").toUpperCase();
        return rut;
    }

    /**
     * Validates rut by comparing check digits
     */
    public boolean rutValidation(String rut){
        try {
            String[] parts = rut.split("-");
            if (parts.length != 2){
                return false;
            }

            String number = parts[0];
            char dv = parts[1].charAt(0);

            // Calculate an expected check digit
            int rutInt = Integer.parseInt(number);
            char expectedDV = calculateDV(rutInt);

            return dv == expectedDV;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * Computes check digit from integer via weighted sum
     */
    private char calculateDV(int rut){
        int sum = 0;
        int factor = 2;
        while (rut > 0) {
            sum += (rut % 10) * factor;
            rut /= 10;
            factor = (factor == 7) ? 2 : factor + 1;
        }

        int dv = 11 - (sum % 11);
        if (dv == 11) return '0';
        else if (dv == 10) return 'K';
        else return (char) (dv + '0');
    }
}
