package facers.test.fingers;

interface FingerprintCallback {
    void onFingerprintSuccess();
    void onFingerprintFailed();
    void onFingerprintError(String error);
}