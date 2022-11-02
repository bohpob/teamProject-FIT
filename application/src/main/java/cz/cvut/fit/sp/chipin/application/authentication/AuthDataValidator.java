package cz.cvut.fit.sp.chipin.application.authentication;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthDataValidator {

    private final int MINIMUM_PASSWORD_LENGTH = 8;
    private final int MAXIMUM_PASSWORD_LENGTH = 64;

    private boolean digit;
    private boolean lower_case;
    private boolean special_symbol;
    private boolean colon;
    private boolean ws;

    boolean minimumPasswordLength(CharSequence s) {
        return s.length() >= MINIMUM_PASSWORD_LENGTH;
    }

    boolean maximumPasswordLength(CharSequence s) {
        return s.length() <= MAXIMUM_PASSWORD_LENGTH;
    }

    boolean containsLetterDigitAndSpecialSymbol(CharSequence s) {
        String specialCharacters = "!@#$%^&*(){}[]|?<>~;";
        digit = lower_case = special_symbol = false;

        for (char ch : s.toString().toCharArray()) {
            digit = digit || Character.isDigit(ch);
            lower_case = lower_case || Character.isLowerCase(ch);
            special_symbol = special_symbol || specialCharacters.indexOf(ch) >= 0;
        }

        return digit && lower_case && special_symbol;
    }

    boolean containsWhitespaces(CharSequence s) {
        ws = false;
        for (char ch : s.toString().toCharArray())
            ws = ws || Character.isWhitespace(ch);
        return ws;
    }

    boolean containsColon(CharSequence s) {
        colon = false;
        for (char ch : s.toString().toCharArray())
            colon = colon || ch == ':';
        return colon;
    }

    public boolean isValidEmail(CharSequence s) {
        return (!TextUtils.isEmpty(s) && Patterns.EMAIL_ADDRESS.matcher(s).matches());
    }

    public boolean isValidName(CharSequence s) {
        return (!TextUtils.isEmpty(s.toString().trim()));
    }

    public boolean isValidPassword(CharSequence s) {
        return minimumPasswordLength(s)                 &&
               maximumPasswordLength(s)                 &&
               containsLetterDigitAndSpecialSymbol(s)   &&
               !containsColon(s)                        &&
               !containsWhitespaces(s);
    }
}
