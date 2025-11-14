package unittest;

import no.lambda.Validator.GeoCodeValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GeoCodeValidatorUnitTest {

    // ---- Unit testing the notBlank validator.
    @Test
    public void notBlank_ReturnsTrue_ForNormalText() {
        String input = "Oslo";
        boolean result = GeoCodeValidator.notBlank(input);
        Assertions.assertTrue(result);
    }
    @Test
    public void notBlank_ReturnsTrue_ForCoordinates() {
        String input = "59.12524782274888, 11.388324293452069";
        boolean result = GeoCodeValidator.notBlank(input);
        Assertions.assertTrue(result);
    }
    @Test
    public void notBlank_ReturnsFalse_ForEmptyString() {
        String input = "";
        boolean result = GeoCodeValidator.notBlank(input);
        Assertions.assertFalse(result);
    }
    @Test
    public void notBlank_ReturnsFalse_ForSpacesOnly() {
        String input = "   ";
        boolean result = GeoCodeValidator.notBlank(input);
        Assertions.assertFalse(result);
    }
    @Test
    public void notBlank_ReturnsFalse_ForNull() {
        String input = null;
        boolean result = GeoCodeValidator.notBlank(input);
        Assertions.assertFalse(result);
    }

    // ---- maxLength validator
    @Test
    public void maxLength_ReturnTrue_ForNormalLength(){
        String input = "Sarpsborg bussterminal";
        boolean result = GeoCodeValidator.maxLength(input);
        Assertions.assertTrue(result);
    }
    @Test
    public void maxLength_ReturnTrue_ForExactly60Char(){
        String input = "123456789 123456789 123456789 123456789 123456789 123456789 ";
        boolean result = GeoCodeValidator.maxLength(input);
        Assertions.assertTrue(result);
    }
    @Test
    public void maxLength_ReturnFalse_ForOver60Char(){
        String input = "123456789 123456789 123456789 123456789 123456789 123456789 1";
        boolean result = GeoCodeValidator.maxLength(input);
        Assertions.assertFalse(result);
    }

    // ---- allowedCharacters Validator
    @Test
    public void allowedCharacters_ReturnTrue_ForAllAllowedChar(){
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz ØøÅåÆæ 0123456789 . , & -";
        boolean result = GeoCodeValidator.allowedCharacters(input);
        Assertions.assertTrue(result);
    }
    @Test
    public void allowedCharacters_ReturnFalse_ForAlotOfNotAllowedChar(){
        String input = "!@# é ñ";
        boolean result = GeoCodeValidator.allowedCharacters(input);
        Assertions.assertFalse(result);
    }




}
