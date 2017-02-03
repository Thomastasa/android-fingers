package facers.test.fingers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SecurityActivity extends AppCompatActivity implements FingerprintCallback {

    final String FP_KEY = "fingerprint_key";
    FingerprintAuthentication fingerprintAuthentication;

    TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        progressText = (TextView) findViewById(R.id.label_security_text);
        progressUpdate("INITIALIZING");

    }

    // this will check to see if fingerprint is setup and ready to be checked.
    // it requires context, FingerprintCallback, and key to be used for fingerprint authentication
    // you can show loading screen until fingerprint is enabled or if it fails do something else
    private void initFingerprint(){
        fingerprintAuthentication = new FingerprintAuthentication(this, this, FP_KEY);
        if (fingerprintAuthentication.initialized()) {
            progressUpdate("ENTER AUTHENTICATION");
        }
    }

    private void progressUpdate(String msg){
        if(progressText != null){
            progressText.setText(msg);
        }
    }

    @Override
    public void onFingerprintSuccess() {
        progressUpdate("AUTHENTICATION SUCCESS");
//        Intent homeIntent = new Intent(this, HomeActivity.class);
        Intent testIntent = new Intent(this, TestActivity.class);
        startActivity(testIntent);
    }
    @Override
    public void onFingerprintFailed() {
        progressUpdate("AUTHENTICATION FAILED");
    }
    @Override
    public void onFingerprintError(String error) {
        progressUpdate("AUTHENTICATION ERROR\n\n"+error);
    }

    @Override
    public void onResume() {
        super.onResume();
        initFingerprint();
    }
}
