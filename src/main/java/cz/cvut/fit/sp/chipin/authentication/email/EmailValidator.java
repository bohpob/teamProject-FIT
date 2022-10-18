package cz.cvut.fit.sp.chipin.authentication.email;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class EmailValidator implements Predicate<String> {

    private final EmailService emailService;

    @Override
    public boolean test(String s) {
        //TODO: regex to validate the email
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return emailService.patternMatches(s, regexPattern);
    }
}