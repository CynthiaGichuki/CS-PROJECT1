package com.example.qrcode;

public interface QrCodeFoundListener {
    void onQRCodeFound(String qrCode);
    void qrCodeNotFound();
}
