package unittest;

import no.lambda.Validator.TripValidator;
import no.lambda.client.entur.dto.TripRequestDto;
import no.lambda.client.entur.dto.TripRequestInputDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class TripValidatorUnitTest {
    // ---- validTime

    @Test
    public void validTime_ReturnsTrue_ForValidString() {
        String input = "2025-11-18T12:00:00+02:00";

        boolean result = TripValidator.validTime(input);

        Assertions.assertTrue(result);
    }

    @Test
    public void validTime_ReturnsFalse_ForInvalidFormat() {
        String input = "12.10.2025 10:00";

        boolean result = TripValidator.validTime(input);

        Assertions.assertFalse(result);
    }

    @Test
    public void validTime_ReturnsFalse_ForRandom() {
        String input = "abc";

        boolean result = TripValidator.validTime(input);

        Assertions.assertFalse(result);
    }
}
