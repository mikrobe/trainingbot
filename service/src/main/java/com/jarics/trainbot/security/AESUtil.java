package com.jarics.trainbot.security;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AESUtil {

    private static final String key = "aesEncryptionKey";
    private static final String initVector = "encryptionIntVec";

    //    /**
    //     * Generates a key and an initialization vector (IV) with the given salt and password.
    //     * <p>
    //     * This method is equivalent to OpenSSL's EVP_BytesToKey function
    //     * (see https://github.com/openssl/openssl/blob/master/crypto/evp/evp_key.c).
    //     * By default, OpenSSL uses a single iteration, MD5 as the algorithm and UTF-8 encoded password data.
    //     * </p>
    //     * @param keyLength the length of the generated key (in bytes)
    //     * @param ivLength the length of the generated IV (in bytes)
    //     * @param iterations the number of digestion rounds
    //     * @param salt the salt data (8 bytes of data or <code>null</code>)
    //     * @param password the password data (optional)
    //     * @param md the message digest algorithm to use
    //     * @return an two-element array with the generated key and IV
    //     */
    //    public static byte[][] generateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password, MessageDigest md) {
    //
    //        int digestLength = md.getDigestLength();
    //        int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
    //        byte[] generatedData = new byte[requiredLength];
    //        int generatedLength = 0;
    //
    //        try {
    //            md.reset();
    //
    //            // Repeat process until sufficient data has been generated
    //            while (generatedLength < keyLength + ivLength) {
    //
    //                // Digest data (last digest if available, password data, salt if available)
    //                if (generatedLength > 0) md.update(generatedData, generatedLength - digestLength, digestLength);
    //                md.update(password);
    //                if (salt != null) md.update(salt, 0, 8);
    //                md.digest(generatedData, generatedLength, digestLength);
    //
    //                // additional rounds
    //                for (int i = 1; i < iterations; i++) {
    //                    md.update(generatedData, generatedLength, digestLength);
    //                    md.digest(generatedData, generatedLength, digestLength);
    //                }
    //
    //                generatedLength += digestLength;
    //            }
    //
    //            // Copy key and IV into separate byte arrays
    //            byte[][] result = new byte[2][];
    //            result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
    //            if (ivLength > 0) result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);
    //
    //            return result;
    //
    //        } catch (DigestException e) {
    //            throw new RuntimeException(e);
    //
    //        } finally {
    //            // Clean out temporary data
    //            Arrays.fill(generatedData, (byte) 0);
    //        }
    //    }

    //    public static String decrypt(String cipherText){
    //
    //        String secret = "Erick Is Magic";
    ////        String cipherText = "U2FsdGVkX1+tsmZvCEFa/iGeSA0K7gvgs9KXeZKwbCDNCs2zPo+BXjvKYLrJutMK+hxTwl/hyaQLOaD7LLIRo2I5fyeRMPnroo6k8N9uwKk=";
    //
    //        byte[] cipherData = Base64
    //          .getDecoder()
    //          .decode(cipherText);
    //
    //        byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);
    //
    //        try {
    //            MessageDigest md5 = MessageDigest.getInstance("MD5");
    //            final byte[][] keyAndIV = generateKeyAndIV(32, 16, 1, saltData, secret.getBytes(StandardCharsets.UTF_8), md5);
    //            SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
    //            IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);
    //
    //            byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
    //            Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
    //            aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
    //            byte[] decryptedData = aesCBC.doFinal(encrypted);
    //            String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);
    //
    //            System.out.println(decryptedText);
    //            return decryptedText;
    //        } catch (Exception e){
    //            e.printStackTrace();
    //            return null;
    //        }
    //    }

    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
