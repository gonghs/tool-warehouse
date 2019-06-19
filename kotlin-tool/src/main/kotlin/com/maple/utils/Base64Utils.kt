package com.maple.utils

import org.apache.commons.lang3.StringUtils
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset

/**
 * base64工具类
 *
 * @author maple
 * @version 1.0
 * @since 2019-06-20 01:01
 */
class Base64Utils {

    /**
     * map 6-bit int to char
     */
    private val chars64 = charArrayOf(
        'A',
        'B',
        'C',
        'D',
        'E',
        'F',
        'G',
        'H',
        'I',
        'J',
        'K',
        'L',
        'M',
        'N',
        'O',
        'P',
        'Q',
        'R',
        'S',
        'T',
        'U',
        'V',
        'W',
        'X',
        'Y',
        'Z',
        'a',
        'b',
        'c',
        'd',
        'e',
        'f',
        'g',
        'h',
        'i',
        'j',
        'k',
        'l',
        'm',
        'n',
        'o',
        'p',
        'q',
        'r',
        's',
        't',
        'u',
        'v',
        'w',
        'x',
        'y',
        'z',
        '0',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        '+',
        '/'
    )

    private var pad = '='

    /**
     * map char to 6-bit int
     */
    private val ints64 = IntArray(128)

    /**
     * 默认编码表
     */
    constructor() {
        for (i in 0..63) {
            ints64[chars64[i].toInt()] = i
        }
    }

    /**
     * 自定义编码表,替换'+'、'/'、'='
     */
    constructor(chPlus: Char, chSplash: Char, chPad: Char) {
        chars64[62] = chPlus
        chars64[63] = chSplash
        pad = chPad
        for (i in 0..63) {
            ints64[chars64[i].toInt()] = i
        }
    }

    /** Convert byte array into Base64 string  */
    private fun encode(unencoded: ByteArray): String {
        // Take 24-bits from three octets, translate into four encoded chars.
        // If necessary, pad with 0 bits on the right at the end
        // Use = signs as padding at the end to ensure encodedLength % 4 == 0

        val out = ByteArrayOutputStream((unencoded.size * 1.37).toInt())
        var byteCount = 0
        var carryOver = 0

        for (i in unencoded.indices) {
            val bc = byteCount % 3
            val b = unencoded[i]
            var lookup = 0

            // First byte use first six bits, save last two bits
            when (bc) {
                0 -> {
                    lookup = b.toInt() shr 2 and 0x3F
                    // last two bits
                    carryOver = b.toInt() and 0x03
                    out.write(chars64[lookup].toInt())
                }
                1 -> {
                    // Second byte use previous two bits and first four new bits,
                    // save last four bits
                    lookup = carryOver shl 4 or (b.toInt() shr 4 and 0x0F)
                    // last four bits
                    carryOver = b.toInt() and 0x0F
                    out.write(chars64[lookup].toInt())
                }
                2 -> {
                    // Third byte use previous four bits and first two new bits,
                    // then use last six new bits
                    lookup = carryOver shl 2 or (b.toInt() shr 6 and 0x03)
                    out.write(chars64[lookup].toInt())
                    // last six bits
                    lookup = b.toInt() and 0x3F
                    out.write(chars64[lookup].toInt())
                    carryOver = 0
                }
            }
            byteCount++
        }
        if (byteCount % 3 == 1) {
            // one leftover
            val lookup = carryOver shl 4 and 0xF0
            out.write(chars64[lookup].toInt())
            out.write(pad.toInt())
            out.write(pad.toInt())
        } else if (byteCount % 3 == 2) {
            // two leftovers
            val lookup = carryOver shl 2 and 0x3C
            out.write(chars64[lookup].toInt())
            out.write(pad.toInt())
        }
        return out.toString()
    }

    /** Decode Base64 string back to byte array  */
    private fun decode(encoded: String): ByteArray {

        val bytes = encoded.toByteArray()

        val out = ByteArrayOutputStream((bytes.size * 0.67).toInt())
        var byteCount = 0
        var carryOver = 0

        for (i in bytes.indices) {
            val ch = bytes[i].toInt()

            // Read the next non-whitespace character
            // The '=' sign is just padding; geffective end of stream
            if (ch == pad.toInt()) {
                break
            }

            // Convert from raw form to 6-bit form
            val newbits = ints64[ch]

            val bc = byteCount % 4
            var data = 0
            when (bc) {
                0 ->
                    // First char save all six bits, go for another
                    carryOver = newbits and 0x3F
                1 -> {
                    // second char use 6 previous bits and first 2 new bits
                    data = (carryOver shl 2) + (newbits shr 4 and 0x03)
                    out.write(data)
                    // save 4 bits
                    carryOver = newbits and 0x0F
                }

                2 -> {
                    // Third char use previous four bits and first four new bits,
                    // save last two bits
                    data = (carryOver shl 4) + (newbits shr 2 and 0x0F)
                    out.write(data)
                    // save 2 bits
                    carryOver = newbits and 0x03
                }

                3 -> {
                    // Fourth char use previous two bits and all six new bits
                    data = (carryOver shl 6) + (newbits and 0x3F)
                    out.write(data)
                    carryOver = 0
                }
            }
            byteCount++
        }
        return out.toByteArray()
    }

    /**
     * base64 加密、返回加密后的字符串
     *
     * @param srcString
     * @param charset 当不传递字符集编码时默认为utf-8
     * @return
     */
    fun encodeBase64(srcString: String?, charset: String): String? {
        var nowCharset = DEFAULT_CHARSET
        if (srcString == null) {
            return null
        }
        if (!StringUtils.isEmpty(charset)) {
            nowCharset = charset
        }
        return encode(srcString.toByteArray(Charset.forName(nowCharset)))
    }

    fun decodeBase64(srcString: String?, charset: String): String? {
        var nowCharset = DEFAULT_CHARSET
        if (srcString == null) {
            return null
        }
        if (!StringUtils.isEmpty(charset)) {
            nowCharset = charset
        }
        return String(decode(srcString), Charset.forName(nowCharset))
    }

    companion object {
        private const val DEFAULT_CHARSET = "utf-8"
    }
}
