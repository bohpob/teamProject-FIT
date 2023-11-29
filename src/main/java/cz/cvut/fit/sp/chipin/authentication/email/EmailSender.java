package cz.cvut.fit.sp.chipin.authentication.email;

public interface EmailSender {
    void sendConfirmation(String to, String name, String token);
}
