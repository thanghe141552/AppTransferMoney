package entity.enums;

public enum ActiveStatus {
    DELETED(0),
    ACTIVE(1);

    private final int value;

    ActiveStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
