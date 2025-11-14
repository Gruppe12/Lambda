package no.lambda.Validator;

public class CoordinatesValidator {

    private static final int MAX_LENGTH = 60;
    private static final String COORDS_REGEX = "^[0-9 .,]+$";

    public static boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }

    public static boolean maxLength(String value) {
        return value != null && value.length() <= MAX_LENGTH;
    }

    public static boolean allowedCharacters(String value) {
        return value != null && value.matches(COORDS_REGEX);
    }

    public static boolean hasTwoParts(String value) {
        if (value == null) return false;
        return value.split(",").length == 2;
    }

    public static boolean isCoordinatePair(String value) {
        if (value == null) return false;
        if (!value.matches("^[0-9 .,]+$")) return false;

        String[] parts = value.split(",");
        return parts.length == 2;
    }

}
