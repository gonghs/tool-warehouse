package com.maple.utils

import org.apache.commons.lang3.StringUtils
import java.security.MessageDigest

/**
 * md5加密工具包
 *
 * @author maple
 * @version 1.0
 * @since 2019-03-15 15:22
 */
object MdUtils {
    /**
     * 十六进制下数字到字符的映射数组
     */
    private val hexDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")

    private const val defaultCode = "1"
    /**
     * 把inputString加密
     */
    fun md5(inputStr: String): String = encodeByMD5(inputStr)

    /**
     * 把inputString加密  加密次数
     */
    fun md5(inputStr: String, time: Int = 1): String {
        return if (time <= 1) {
            encodeByMD5(inputStr)
        } else {
            md5(encodeByMD5(inputStr), time - 1)
        }
    }

    /**
     * 加密多个字符串并进行拼接
     *
     * @param inputArr 输入
     * @return md5字符串
     */
    fun md5(vararg inputArr: String): String {
        val sb = StringBuilder()
        for (str in inputArr) {
            if (StringUtils.isNotBlank(str)) {
                sb.append(encodeByMD5(str)).append(",")
            }
        }
        //默认值为1
        return if (sb.isNotEmpty()) sb.substring(0, sb.length - 1) else defaultCode
    }

    /**
     * 验证输入的密码是否正确
     *
     * @param password    真正的密码（加密后的真密码）
     * @param inputString 输入的字符串
     * @return 验证结果，boolean类型
     */
    fun authenticatePassword(password: String, inputString: String): Boolean = password == encodeByMD5(inputString)

    /**
     * 对字符串进行MD5编码
     */
    private fun encodeByMD5(originString: String): String {
        return try {
            //创建具有指定算法名称的信息摘要
            val md5 = MessageDigest.getInstance("MD5")
            //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
            val results = md5.digest(originString.toByteArray())
            //将得到的字节数组变成字符串返回
            byteArrayToHexString(results)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 轮换字节数组为十六进制字符串
     *
     * @param b 字节数组
     * @return 十六进制字符串
     */
    private fun byteArrayToHexString(b: ByteArray): String {
        val resultSb = StringBuilder()
        for (i in b.indices) {
            resultSb.append(byteToHexString(b[i]))
        }
        return resultSb.toString()
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     */
    private fun byteToHexString(b: Byte): String {
        var n = b.toInt()
        if (n < 0) {
            n += 256
        }
        val d1 = n / 16
        val d2 = n % 16
        return hexDigits[d1] + hexDigits[d2]
    }
}

/**
 * 将字符串转化为md5格式
 */
fun String.encodeByMD5():String = MdUtils.md5(this)
/**
 * 将字符串转化为md5格式 指定次数
 */
fun String.encodeByMD5(time: Int):String = MdUtils.md5(this, time)