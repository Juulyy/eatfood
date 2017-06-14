package com.eat.utils.math;

import java.text.DecimalFormat;

public interface NumberRounder {

    default Double round(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        return Double.valueOf(decimalFormat.format(value));
    }

    static Double roundToThousands(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        return Double.valueOf(decimalFormat.format(value));
    }

}