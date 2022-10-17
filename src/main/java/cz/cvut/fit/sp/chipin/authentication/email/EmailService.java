package cz.cvut.fit.sp.chipin.authentication.email;

import java.util.regex.Pattern;

public class EmailService {

    public boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

}
