package com.paran.sample.domain.user.persistence.converter;

import com.paran.sample.config.AesKeyConfig;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;

import static org.springframework.util.ObjectUtils.isEmpty;

@Converter
public class EncryptConverter implements AttributeConverter<String, String> {

    private String secret;
    private static final String AES = "AES";
    private final Cipher cipher;

    public EncryptConverter() throws Exception {
        cipher = Cipher.getInstance(AES);
    }

    private Key getKey() {
        secret = new AesKeyConfig().getJpaDbKey();
        return new SecretKeySpec(secret.getBytes(), AES);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if(isEmpty(attribute)) return null;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, getKey());
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, getKey());
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            return null;
        }
    }
}
