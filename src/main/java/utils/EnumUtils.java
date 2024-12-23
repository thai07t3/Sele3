package utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class EnumUtils {
    public static <T extends Enum<T>> T getByValue(Class<T> enumType, String value, String methodName) {
        for (T enumConstant : enumType.getEnumConstants()) {
            try {
                Method getValueMethod = enumType.getMethod(methodName);
                String enumValue = (String) getValueMethod.invoke(enumConstant);
                if (enumValue.equals(value)) {
                    return enumConstant;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error("Error accessing method {} in enum {}: {}", methodName, enumType.getName(), e.getMessage());
            }
        }
        return null;
    }

    public static <T extends Enum<T>> T getByValueOrThrow(Class<T> enumType, String value, String methodName) {
        T result = getByValue(enumType, value, methodName);
        if (result == null) {
            throw new IllegalArgumentException("No enum constant in " + enumType.getName() + " with value: " + value);
        }
        return result;
    }
}
