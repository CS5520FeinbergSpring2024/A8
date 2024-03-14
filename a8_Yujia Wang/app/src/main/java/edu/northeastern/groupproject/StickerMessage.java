package edu.northeastern.groupproject;

public class StickerMessage {
    private String senderUid;
    private String receiverUid;
    private String stickerIdentifier;
    private long timestamp;

    public StickerMessage() {
        // Default constructor required for calls to DataSnapshot.getValue(StickerMessage.class)
    }

    public StickerMessage(String senderUid, String receiverUid, String stickerIdentifier, long timestamp) {
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.stickerIdentifier = stickerIdentifier;
        this.timestamp = timestamp;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getStickerIdentifier() {
        return stickerIdentifier;
    }

    public void setStickerIdentifier(String stickerIdentifier) {
        this.stickerIdentifier = stickerIdentifier;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}


