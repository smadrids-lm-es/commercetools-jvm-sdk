package io.sphere.sdk.models;

import io.sphere.sdk.models.errors.InvalidJsonInputError;
import io.sphere.sdk.json.JsonUtils;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class SphereErrorTest {
    @Test
    public void castToConcreteError() throws Exception {
        final String json = "{\n" +
                "  \"statusCode\" : 400,\n" +
                "  \"message\" : \"Request body does not contain valid JSON.\",\n" +
                "  \"errors\" : [ {\n" +
                "    \"code\" : \"InvalidJsonInput\",\n" +
                "    \"message\" : \"Request body does not contain valid JSON.\",\n" +
                "    \"detailedErrorMessage\" : \"detailed error message\"" +
                "  } ]\n" +
                "}";
        final ErrorResponse sphereErrorResponse = JsonUtils.readObjectFromJsonString(ErrorResponse.typeReference(), json);
        final InvalidJsonInputError jsonError = sphereErrorResponse.getErrors().get(0).as(InvalidJsonInputError.class);


        assertThat(jsonError.getDetailedErrorMessage()).isEqualTo("detailed error message");
    }

    @Test
    public void notMatchingError() throws Exception {
        final String json = "{\n" +
                "  \"statusCode\" : 400,\n" +
                "  \"message\" : \"Message.\",\n" +
                "  \"errors\" : [ {\n" +
                "    \"code\" : \"OtherCode\",\n" +
                "    \"message\" : \"Message.\"" +
                "  } ]\n" +
                "}";
        final ErrorResponse sphereErrorResponse = JsonUtils.readObjectFromJsonString(ErrorResponse.typeReference(), json);
        assertThatThrownBy(() -> sphereErrorResponse.getErrors().get(0).as(InvalidJsonInputError.class))
                .isInstanceOf(IllegalArgumentException.class);
    }
}