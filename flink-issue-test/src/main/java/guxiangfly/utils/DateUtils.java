package guxiangfly.utils;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {
    private static final Logger LOG = LoggerFactory.getLogger(DateUtils.class);

    public static final String TIME_PATERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_PATERN_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String HOUR_PATERN = "yyyyMMddHH";
    public static final String SECOND_PATERN = "yyyyMMddHHmmss";
    public static final String DAY_PATERN = "yyyy-MM-dd";
    public static final String DAY_NUM = "yyyyMMdd";
    public static final String DAY_PART_NUM = "yyMMdd";
    public static final String MM_DD_PATTERN = "MM.dd";



    /**
     * 一年的毫秒数
     */
    private static final long MILLIS_OF_YEAR = 365 * 24 * 3600 * 1000L;

    public static final long MILLS_OF_DAY = 1000 * 3600 * 24;

    public static final long MILLS_OF_MINUTE = 60 * 1000L;

    public static final int SECONDS_OF_DAY = 3600 * 24;

    public static final int SECONDS_OF_HOUR = 60 * 60;

    /**
     * 第一轮的结束时间
     */
    private static final int FIRST_ROUND_FINISH_HOUR = 10;

    private DateUtils() {
    }

    public static Date getDateByStr(String dateStr) {
        return getDateByStr(dateStr, DateUtils.DAY_NUM);
    }

    /**
     * 将指定格式的String转换为Date
     */
    public static Date getDateByStr(String dateStr, String pattern) {
        try {
            if (StringUtils.isBlank(pattern)) {
                pattern = TIME_PATERN; //"yyyy-MM-dd HH:mm:ss"
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            LOG.error("getDateByStr error", e);
        }
        return new Date();
    }

    public static String getStrByDateLong(Long timeLong){
        return   getStrByDateLong( timeLong,DateUtils.TIME_PATERN);
    }

    public static String getStrByDateLong(Long timeLong,String pattern){
        try {
            Date date = new Date(timeLong);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            LOG.error("getStrByDate error", e);
        }
        return "";
    }

    public static String getStrByDate(Date date,String pattern){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            LOG.error("getStrByDate error", e);

        }
        return "";
    }

    public static Date getBeginOfToday(){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(new Date());

            Date parse = simpleDateFormat.parse(format);
            return parse;
        } catch (Exception e) {
            LOG.error("getBeginOfToday error", e);

        }

        return new Date();
    }

    public static Date getBeginOfDay(Date date){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(date);

            Date parse = simpleDateFormat.parse(format);
            return parse;
        } catch (Exception e) {
            LOG.error("getBeginOfDay error", e);
        }

        return new Date();
    }

    public static void main(String[] args) {
        Date beginOfToday = getBeginOfToday();
        System.out.println(beginOfToday.getTime());
        System.out.println(beginOfToday.getTime() > 1610194823605L);
        System.out.println(getStrByDate(beginOfToday,TIME_PATERN));
    }
}
