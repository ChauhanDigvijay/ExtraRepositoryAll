package com.identity.arx.fingerprint;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.CryptoObject;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec.Builder;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.identity.arx.R;
import com.identity.arx.general.MasterActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class FingerprintActivity extends MasterActivity {
    private static final String KEY_NAME = "IdentityAXS";
    private Cipher cipher;
    private KeyStore keyStore;
    private TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_fingerprint);
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService("keyguard");
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService("fingerprint");
        this.textView = (TextView) findViewById(R.id.errorText);
        if (!fingerprintManager.isHardwareDetected()) {
            this.textView.setText("Your Device does not have a Fingerprint Sensor");
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.USE_FINGERPRINT") != 0) {
            this.textView.setText("Fingerprint authentication permission not enabled");
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            this.textView.setText("Register at least one fingerprint in Settings");
        } else if (keyguardManager.isKeyguardSecure()) {
            generateKey();
            if (cipherInit()) {
                new FingerprintHandler(this).startAuth(fingerprintManager, new CryptoObject(this.cipher));
            }
        } else {
            this.textView.setText("Lock screen security not enabled in Settings");
        }
    }

    @TargetApi(23)
    protected void generateKey() {
        Exception e;
        GeneralSecurityException e2;
        try {
            this.keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "AndroidKeyStore");
            NoSuchAlgorithmException e3;
            try {
                this.keyStore.load(null);
                keyGenerator.init(new Builder(KEY_NAME, 3).setBlockModes(new String[]{"CBC"}).setUserAuthenticationRequired(true).setEncryptionPaddings(new String[]{"PKCS7Padding"}).build());
                keyGenerator.generateKey();
            } catch (NoSuchAlgorithmException e4) {
                e3 = e4;
                throw new RuntimeException(e3);
            } catch (InvalidAlgorithmParameterException e1) {
                e1.printStackTrace();
            } catch (CertificateException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e8) {
            e2 = e8;
            throw new RuntimeException("Failed to get KeyGenerator instance", e2);
        } catch (NoSuchProviderException e9) {
            e2 = e9;
            throw new RuntimeException("Failed to get KeyGenerator instance", e2);
        }
    }

    @TargetApi(23)
    public boolean cipherInit() {
        Exception e;
        GeneralSecurityException e2;
        try {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            try {
                this.keyStore.load(null);
                this.cipher.init(1, (SecretKey) this.keyStore.getKey(KEY_NAME, null));
                return true;
            } catch (KeyPermanentlyInvalidatedException e3) {
                return false;
            } catch (KeyStoreException e4) {
                e = e4;
                throw new RuntimeException("Failed to init Cipher", e);
            } catch (CertificateException e5) {
                e = e5;
                throw new RuntimeException("Failed to init Cipher", e);
            } catch (UnrecoverableKeyException e6) {
                e = e6;
                throw new RuntimeException("Failed to init Cipher", e);
            } catch (IOException e7) {
                e = e7;
                throw new RuntimeException("Failed to init Cipher", e);
            } catch (NoSuchAlgorithmException e8) {
                e = e8;
                throw new RuntimeException("Failed to init Cipher", e);
            } catch (InvalidKeyException e9) {
                e = e9;
                throw new RuntimeException("Failed to init Cipher", e);
            }
        } catch (NoSuchAlgorithmException e10) {
            e2 = e10;
            throw new RuntimeException("Failed to get Cipher", e2);
        } catch (NoSuchPaddingException e11) {
            e2 = e11;
            throw new RuntimeException("Failed to get Cipher", e2);
        }
    }
}
