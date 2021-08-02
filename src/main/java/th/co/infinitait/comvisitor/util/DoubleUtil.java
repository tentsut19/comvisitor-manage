package th.co.infinitait.comvisitor.util;

import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;


@Slf4j
public class DoubleUtil {

    private static final DecimalFormat df2 = new DecimalFormat("#,##0.00");

    public static String toStringDecimalFormat(double value, DecimalFormat df) {
//        log.info("value : {}",value);
        String result =  df.format(value);
//        log.info("result : {}",result);
        return result;
    }

    public static String toString2DecimalFormat(double value) {
//        log.info("value : {}",value);
        String result =  df2.format(value);
//        log.info("result : {}",result);
        return result;
    }

}