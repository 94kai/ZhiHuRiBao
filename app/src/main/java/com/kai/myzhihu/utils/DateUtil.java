package com.kai.myzhihu.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by 凯 on 2016/3/26 22:17
 */
public class DateUtil {
    /**
     * 年月日转换成 月 日 周
     * create at 2016/3/26 22:19 by 凯
     */
    public static String nyrToyrx(String nyr) {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar calendra = Calendar.getInstance();
        try {
            calendra.setTime(format.parse(nyr));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int y = calendra.get(Calendar.MONTH);
        y++;
        int r = calendra.get(Calendar.DAY_OF_MONTH);

        int dayOfWeek = calendra.get(Calendar.DAY_OF_WEEK);

        String dayOfWeekChi = "";
        switch (dayOfWeek) {
            case 1:
                dayOfWeekChi = "日";
                break;
            case 2:
                dayOfWeekChi = "一";

                break;
            case 3:
                dayOfWeekChi = "二";

                break;
            case 4:
                dayOfWeekChi = "三";

                break;
            case 5:
                dayOfWeekChi = "四";

                break;
            case 6:
                dayOfWeekChi = "五";

                break;
            case 7:
                dayOfWeekChi = "六";

                break;
        }
        return y + "月" + r + "日 星期" + dayOfWeekChi;


    }
}
