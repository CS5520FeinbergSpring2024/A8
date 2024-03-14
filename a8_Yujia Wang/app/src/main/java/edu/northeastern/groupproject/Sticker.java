package edu.northeastern.groupproject;
public class Sticker {
    public String sender;
    public String receiver;
    public String stickerCode;
    public long timestamp;

    public Sticker() {
        // Default constructor required for calls to DataSnapshot.getValue(Sticker.class)
    }

    public Sticker(String sender, String receiver, String stickerCode) {
        this.sender = sender;
        this.receiver = receiver;
        this.stickerCode = stickerCode;
        this.timestamp = System.currentTimeMillis();
    }
}


