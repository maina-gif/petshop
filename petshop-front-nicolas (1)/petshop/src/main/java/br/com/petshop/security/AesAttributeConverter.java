package br.com.petshop.security;

import jakarta.persistence.AttributeConverter; import jakarta.persistence.Converter; import javax.crypto.Cipher; import javax.crypto.spec.GCMParameterSpec; import javax.crypto.spec.SecretKeySpec; import java.nio.charset.StandardCharsets; import java.security.SecureRandom; import java.util.Base64;

@Converter
public class AesAttributeConverter implements AttributeConverter<String,String> {
 private static final String ALGORITHM="AES/GCM/NoPadding"; private static final int IV_SIZE=12; private static final int TAG_BITS=128;
 private byte[] key(){String value=System.getenv("AES_SECRET_KEY"); if(value==null||value.length()!=32) throw new IllegalStateException("AES_SECRET_KEY deve ter exatamente 32 caracteres"); return value.getBytes(StandardCharsets.UTF_8);}
 public String convertToDatabaseColumn(String value){if(value==null)return null; try{byte[] iv=new byte[IV_SIZE];new SecureRandom().nextBytes(iv);Cipher c=Cipher.getInstance(ALGORITHM);c.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(key(),"AES"),new GCMParameterSpec(TAG_BITS,iv));byte[] out=c.doFinal(value.getBytes(StandardCharsets.UTF_8));byte[] all=new byte[iv.length+out.length];System.arraycopy(iv,0,all,0,iv.length);System.arraycopy(out,0,all,iv.length,out.length);return Base64.getEncoder().encodeToString(all);}catch(Exception e){throw new IllegalStateException("Falha ao criptografar dado",e);}}
 public String convertToEntityAttribute(String value){if(value==null)return null; try{byte[] all=Base64.getDecoder().decode(value);byte[] iv=java.util.Arrays.copyOfRange(all,0,IV_SIZE);byte[] encrypted=java.util.Arrays.copyOfRange(all,IV_SIZE,all.length);Cipher c=Cipher.getInstance(ALGORITHM);c.init(Cipher.DECRYPT_MODE,new SecretKeySpec(key(),"AES"),new GCMParameterSpec(TAG_BITS,iv));return new String(c.doFinal(encrypted),StandardCharsets.UTF_8);}catch(Exception e){throw new IllegalStateException("Falha ao descriptografar dado",e);}}
}
