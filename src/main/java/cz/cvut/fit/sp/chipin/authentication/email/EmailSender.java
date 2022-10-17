package cz.cvut.fit.sp.chipin.authentication.email;

public interface EmailSender {
    void send(String to, String email);
}
