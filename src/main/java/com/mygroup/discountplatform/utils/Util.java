package com.mygroup.discountplatform.utils;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

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

    /**
     * Returns time ago as human‑readable string
     */
    public String formatTimeAgo(LocalDateTime past){
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(past, now);

        // Returns human‑readable time difference as string
        if (duration.toDays() > 0) {
            Period period = Period.between(past.toLocalDate(), now.toLocalDate());

            // Returns years, months, or days as human‑readable string
            if (period.getYears() > 0) {
                int years = period.getYears();
                return years == 1 ? "One year ago" : years + " years ago";
            } else if (period.getMonths() > 0) {
                int months = period.getMonths();
                return months == 1 ? "One month ago" : months + " months ago";
            } else {
                int days = period.getDays();
                return days == 1 ? "One day ago" : days + " days ago";
            }
        } else if (duration.toHours() > 0) {
            long hours = duration.toHours();
            return hours == 1 ? "One hour ago" : hours + " hours ago";
        } else if (duration.toMinutes() > 0) {
            long minutes = duration.toMinutes();
            return minutes == 1 ? "One minute ago" : minutes + " minutes ago";
        } else {
            long seconds = duration.getSeconds();
            return seconds == 1 ? "One second ago" : seconds + " seconds ago";
        }
    }
}
