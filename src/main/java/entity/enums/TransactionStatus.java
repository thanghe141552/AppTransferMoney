package entity.enums;

public enum TransactionStatus {
    PENDING(0),
    APPROVED(1),
    DECLINED(2),
    FAILED(3);

    private final int value;

    TransactionStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
