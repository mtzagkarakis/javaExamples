package manos.examples;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class CipherTest {
    @Test
    public void encryptDecryptTest() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {

        String originalMessage = "This is a secret message";

        Assert.assertEquals(originalMessage, encryptDecryptMessage(originalMessage, false));
    }

    @Test
    public void encryptDecryptWithBCTest() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {

        String originalMessage = "This is a secret message";

        Assert.assertEquals(originalMessage, encryptDecryptMessage(originalMessage, true));
    }

    private String encryptDecryptMessage(String message, boolean useBouncyCastle) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {
        String encryptionKeyString =  "thisisa128bitkey";
        byte[] encryptionKeyBytes = encryptionKeyString.getBytes();

        if (useBouncyCastle){
            Security.addProvider(new BouncyCastleProvider());
        }

        Cipher cipher = useBouncyCastle?Cipher.getInstance("AES/ECB/PKCS5Padding", "BC"):Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(encryptionKeyBytes, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedMessageBytes = cipher.doFinal(message.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decryptedMessageBytes = cipher.doFinal(encryptedMessageBytes);
        return new String(decryptedMessageBytes);
    }
}
