package no.lambda.Validator;

public class GeoCodeValidator {

    // spesifikk validation regler til GeoCoder

    private static final int MAX_LENGTH = 60;
    private static final String ALLOWED_CHARACTERS = "^[A-Za-zØøÅåÆæ0-9 .,&\\-]+$";

    public static boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }

    public static boolean maxLength(String value) {
        return value != null && value.length() <= MAX_LENGTH;
    }

    public static boolean allowedCharacters(String value) {
        return value != null && value.matches(ALLOWED_CHARACTERS);
    }
}
