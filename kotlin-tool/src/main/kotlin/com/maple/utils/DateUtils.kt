package com.maple.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间工具类
 *
 * @author maple
 * @version 1.0
 * @since 2019-02-24 17:34
 */
object DateUtils {
    val defaultSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val simpleDateFormat = SimpleDateFormat()

    fun format2Str(date: Date):String = defaultSimpleDateFormat.format(date)

    /**
     * 计算距离某时间还有多久
     *
     * @param date 需要计算的时间
     * @return
     * @author Hunter
     * @version [1.0.0, 2015年10月22日]
     * @since [1.0.0]
     */
    fun computedTimeDistanceToChinese(date: Date): String {
        val sb = StringBuilder()
        // 过期时间
        val validTime = date.time
        val now = System.currentTimeMillis()
        val dur = validTime - now
        if (dur < 0) {
            return " 00天 00时 00分 00秒"
        }
        // 天
        val day = (dur / (60 * 60 * 24 * 1000)).toInt()
        if (day < 10) {
            sb.append(0)
        }
        sb.append(day)
        sb.append(" 天")
        // 小时
        val hours = ((dur - day.toLong() * (60 * 60 * 24 * 1000)) / (60 * 60 * 1000)).toInt()
        if (hours < 10) {
            sb.append(0)
        }
        sb.append(hours)
        sb.append(" 时")
        // 分钟
        val min =
            ((dur - day.toLong() * (60 * 60 * 24 * 1000) - hours.toLong() * (60 * 60 * 1000)) / (60 * 1000)).toInt()
        if (min < 10) {
            sb.append(0)
        }
        sb.append(min)
        sb.append(" 分")
        // 秒
        val seconds =
            ((dur - day.toLong() * (60 * 60 * 24 * 1000) - hours.toLong() * (60 * 60 * 1000) - (min * (60 * 1000)).toLong()) / 1000).toInt()
        if (seconds < 10) {
            sb.append(0)
        }
        sb.append(seconds)
        sb.append(" 秒")
        return sb.toString()
    }


    /**
     * 根据 时间值获取耗时 天 小时 分 秒
     *
     * @param currentTimeMillis 毫秒级的时间
     * @return xx天 xx小时 xx分
     */
    fun computedTakeTimeToChinese(currentTimeMillis: Long): String {
        var millis = currentTimeMillis
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = millis / daysInMilli
        millis %= daysInMilli

        val elapsedHours = millis / hoursInMilli
        millis %= hoursInMilli

        val elapsedMinutes = millis / minutesInMilli
        millis %= minutesInMilli
        val elapsedSeconds = millis / secondsInMilli
        return elapsedDays.toString() + "天" + elapsedHours + "小时" + elapsedMinutes + "分" + elapsedSeconds + "秒"
    }

    /**
     * 数字转换成星期 1对应周日 <br></br>
     * eg:入参：1 出参：星期日 ；如果参数不在 (0,8)输出 ：输入的值超过范围！
     *
     * @param weekDay 星期对应的数字
     * @return 星期X
     */
    private fun changeWeekDayToChinese(weekDay: Int): String {
        return if (weekDay in 1..7) {
            when (weekDay) {
                Calendar.SUNDAY -> "星期日"
                Calendar.MONDAY -> "星期一"
                Calendar.TUESDAY -> "星期二"
                Calendar.WEDNESDAY -> "星期三"
                Calendar.THURSDAY -> "星期四"
                Calendar.FRIDAY -> "星期五"
                Calendar.SATURDAY -> "星期六"
                else -> ""
            }
        } else {
            "输入的值超过范围！"
        }
    }

    /**
     * 计算时间间隔 <br></br>
     *
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return 两个时间之间相差的 月日时分秒
     */
    fun computedDateIntervalToChinese(beginDate: Date, endDate: Date): String {
        val d1 = beginDate.time
        val d2 = endDate.time
        val millisecond = d2 - d1
        // 判断是否超过一个小时
        val interval = Math.abs(millisecond.toFloat() / (1000 * 60 * 60))
        // 大于一个小时
        return if (interval >= 1.0) {
            beginDate.format2Str("MM月dd日  HH:mm:ss")
        } else {
            Date(millisecond).format2Str("mm分ss秒")
        }
    }
}

//将工具类使用拓展方式写入Date类中

/**
 * 使用指定方式格式化时间字符串
 */
fun Date.format2Str(pattern:String):String{
    DateUtils.simpleDateFormat.applyPattern(pattern)
    return DateUtils.simpleDateFormat.format(this)
}
/**
 * 使用默认方式格式化时间字符串
 */
fun Date.format2Str():String = DateUtils.defaultSimpleDateFormat.format(this)