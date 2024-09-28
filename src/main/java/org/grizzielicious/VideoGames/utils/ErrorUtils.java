package org.grizzielicious.VideoGames.utils;

import org.grizzielicious.VideoGames.exceptions.InvalidParameterException;
import org.springframework.validation.Errors;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import static org.grizzielicious.VideoGames.constants.ValidationsConstants.CANNOT_BE_NULL_OR_EMPTY;
import static org.grizzielicious.VideoGames.constants.ValidationsConstants.LENGTH_NOT_VALID25;

public class ErrorUtils {
    public static Set<String> errorsToStringSet(Errors errores) {
        Set<String> errorSet = new TreeSet<>();
        errores.getFieldErrors().forEach(error -> errorSet.add(error.getDefaultMessage()));
        return errorSet;
    }

    public static void validateString25 (String toValidate, String name) throws InvalidParameterException {
        if(Objects.isNull(toValidate) || toValidate.isBlank()) {
            throw new InvalidParameterException(name + CANNOT_BE_NULL_OR_EMPTY);
        }else if (toValidate.length()>25 || toValidate.length()<3) {
            throw new InvalidParameterException(name + LENGTH_NOT_VALID25);
        }
    }
}
