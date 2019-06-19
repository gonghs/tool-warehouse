package com.maple.utils

import org.apache.commons.codec.binary.Base64
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.Key
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.SecretKeySpec

/**
 * aes加密工具包
 *
 * @author maple
 * @version 1.0
 * @since 2019-06-20 01:15
 */
object AesUtils {
    private val LOG = LoggerFactory.getLogger(AesUtils::class.java)

    private const val CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding"

    private val DEF_CHARSET = charset("utf-8")

    private const val BYTE_LENGTH = 32

    /**
     * 本地线程定义
     */
    private val cipherThread = ThreadLocal<Cipher>()

    @Throws(UnsupportedEncodingException::class)
    private fun getSecretKey(mesKey: String): Key {
        var oigByte = Base64.decodeBase64(mesKey.toByteArray(DEF_CHARSET))
        if (oigByte.size != BYTE_LENGTH) {
            val newOigByte = ByteArray(BYTE_LENGTH)
            for (i in 0 until BYTE_LENGTH) {
                newOigByte[i] = if (i < oigByte.size) oigByte[i] else 0
            }
            oigByte = newOigByte
        }
        return SecretKeySpec(oigByte, "AES")

    }

    /**
     * 获取加密函数
     *
     * @param secretKey 密钥key
     * @return Cipher 对象
     */
    @Throws(NoSuchAlgorithmException::class, NoSuchPaddingException::class, InvalidKeyException::class)
    private fun getCipher(secretKey: Key, cipherMode: Int): Cipher {
        try {
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            cipher.init(cipherMode, secretKey)
            cipherThread.set(cipher)
        } catch (e: NoSuchAlgorithmException) {
            throw e
        } catch (e: NoSuchPaddingException) {
            throw e
        } catch (e: InvalidKeyException) {
            throw e
        }

        val result = cipherThread.get()
        cipherThread.remove()
        return result
    }

    private fun decrypt(encryptData: String, secretKey: Key?): String? {
        try {
            if (secretKey != null) {
                var encryptByte = encryptData.toByteArray(DEF_CHARSET)
                encryptByte = Base64.decodeBase64(encryptByte)
                val cipher = getCipher(secretKey, Cipher.DECRYPT_MODE)
                val enBytes = cipher.doFinal(encryptByte)
                return String(enBytes, DEF_CHARSET)
            } else {
                throw IllegalAccessException("secretKey is null")
            }
        } catch (e: Exception) {
            LOG.error("decrypt Exception", e)
        }

        return null
    }

    fun decryptWithOutBase64(encryptData: String, secretKey: String): String? {
        try {
            val key = getSecretKey(secretKey)
            return decrypt(encryptData, key)
        } catch (e: UnsupportedEncodingException) {
            LOG.error("AesUtils decryptWithOutBase64 Exception", e)
        }

        return null
    }

    /**
     * 老的解密方法，兼容用，后期可以下掉
     *
     * @param encryptData 加密数据
     * @param secretKey 加密key
     * @return
     */
    fun decrypt(encryptData: String, secretKey: String): String? {
        try {
            val base64 = encodeBase64(secretKey)
            val key = getSecretKey(base64)
            return decrypt(encryptData, key)
        } catch (e: UnsupportedEncodingException) {
            LOG.error("AesUtils decrypt Exception", e)
        }
        return null
    }

    fun encrypt(originData: String, secretKey: String): String? {
        try {
            val key = getSecretKey(secretKey)
            return encrypt(originData, key)
        } catch (e: Exception) {
            LOG.error("AesUtils init", e)
        }

        return null
    }

    /**
     * base64加密
     *
     * @param secretKey 16个字符的字符串
     * @return 经过base64加密后的字符串
     */
    @Throws(UnsupportedEncodingException::class)
    private fun encodeBase64(secretKey: String): String {
        val mesKeyByteArr = Base64.encodeBase64(secretKey.toByteArray(DEF_CHARSET))
        return String(mesKeyByteArr)
    }

    /**
     * 加密算法
     *
     * @param originData 原始数据
     * @param key        加密key
     * @return 加密字符串
     */
    private fun encrypt(originData: String, key: Key): String? {
        val cipher: Cipher
        try {
            cipher = getCipher(key, Cipher.ENCRYPT_MODE)
            val dataBytes = originData.toByteArray(DEF_CHARSET)
            var enBytes = cipher.doFinal(dataBytes)
            enBytes = Base64.encodeBase64(enBytes)
            return String(enBytes, DEF_CHARSET)
        } catch (e: Exception) {
            LOG.error("encrypt Exception ", e)
        }

        return null
    }

    /***
     * 由于JCE加密对密钥长度有要求，所以需要对密钥长度进行截取，保证32位
     *
     * @param key
     * @return
     */
    fun getAesTimeKey(key: String?): String? {
        return key
    }
}