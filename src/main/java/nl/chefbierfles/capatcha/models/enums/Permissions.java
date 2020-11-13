package nl.chefbierfles.capatcha.models.enums;

public enum Permissions {

    PERMISSION_CAPATCHA_BYPASS("capatcha.bypass");

    private final String value;

    Permissions(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
