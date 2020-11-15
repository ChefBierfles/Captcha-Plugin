package nl.chefbierfles.capatcha.models.enums;

public enum DatabaseFields {

    /*
    Allow to bypass the capatcha
    */
    CAPATCHA_LASTDONE_DATE("lastDoneDate");

    private final String value;

    DatabaseFields(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
