package facers.test.fingers;


import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
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

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

public class FingerprintAuthentication extends FingerprintManager.AuthenticationCallback {

    private Context appContext;
    private String keyName = "";
    private FingerprintCallback cb;

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;
    private CancellationSignal cancellationSignal;
    private FingerprintManager.AuthenticationCallback fingerprintCallback;
    private Handler fingerprintHandler;

    /**
     * @param context application context (this)
     * @param callback callback (this)
     * @param key key name (fingerprint_key)
     */
    FingerprintAuthentication(Context context, FingerprintCallback callback, String key) {
        appContext = context;
        cb = callback;
        keyName = key;
    }

    boolean initialized(){
        if(keyName.length() > 0){
            keyguardManager = (KeyguardManager) appContext.getSystemService(KEYGUARD_SERVICE);
            fingerprintManager = (FingerprintManager) appContext.getSystemService(FINGERPRINT_SERVICE);
            if (!keyguardManager.isKeyguardSecure()) {
                Toast.makeText(appContext, "Lock screen security not enabled in Settings",Toast.LENGTH_LONG).show();
                return false;
            }else if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(appContext, "Fingerprint authentication permission not enabled", Toast.LENGTH_LONG).show();
                return false;
            }else if (!fingerprintManager.hasEnrolledFingerprints()) {
                Toast.makeText(appContext, "Register at least one fingerprint in Settings", Toast.LENGTH_LONG).show();
                return false;
            }else{
                if(generateKey()){
                    if(cipherInit()){
                        cryptoObject = new FingerprintManager.CryptoObject(cipher);
                        cancellationSignal = new CancellationSignal();
                        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(appContext, "Failed to initialize Fingerprint authentication: Permission not enabled", Toast.LENGTH_LONG).show();
                            return false;
                        }
                        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
                        return true;
                    }else{
                        Toast.makeText(appContext, "Failed to initialize Fingerprint authentication", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }else{
                    Toast.makeText(appContext, "Failed to initialize Fingerprint authentication", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }else{
            Toast.makeText(appContext, "Fingerprint authentication not initialized correctly", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean generateKey(){
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            Log.e("FingerprintAuth","Failed to get AndroidKeyStore instance");
            return false;
        }
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            Log.e("FingerprintAuth","Failed to get KeyGenerator instance");
            return false;
        }
        try {
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(keyName, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
            return true;
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | CertificateException | IOException e) {
            Log.e("FingerprintAuth","Failed to init KeyGenerator");
            return false;
        }
    }

    private boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            Log.e("FingerprintAuth","Failed to get Cipher");
            return false;
        }
        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(keyName, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            Log.e("FingerprintAuth","Failed to init Cipher");
            return  false;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        cb.onFingerprintError(errString.toString());
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        cb.onFingerprintError(helpString.toString());
    }

    @Override
    public void onAuthenticationFailed() {
        cb.onFingerprintFailed();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        cb.onFingerprintSuccess();
    }
}
