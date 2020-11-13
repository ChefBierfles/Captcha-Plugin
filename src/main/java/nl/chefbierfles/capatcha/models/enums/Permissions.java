package nl.chefbierfles.capatcha.models.enums;

public enum Permissions {

    /*
    Allow to bypass the capatcha
     */
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
