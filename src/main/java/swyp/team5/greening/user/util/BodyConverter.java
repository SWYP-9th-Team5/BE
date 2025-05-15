package swyp.team5.greening.user.util;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BodyConverter {

    public static String fromFormData(Map<String, String> formData) {
        StringBuilder stringBuilder = new StringBuilder();

        boolean isFirst = true;

        for (Map.Entry<String, String> entry : formData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            stringBuilder
                    .append(isFirst ? "" : "&")
                    .append(key)
                    .append("=")
                    .append(value);
            isFirst = false;
        }

        return stringBuilder.toString();
    }

}
