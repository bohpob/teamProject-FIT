package cz.cvut.fit.sp.chipin.authentication.email.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token){
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        if (confirmationTokenRepository.findByToken(token).isEmpty())
            throw new IllegalStateException("Token nod found");

        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token).get();
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
    }

    public List<ConfirmationToken> getAllTokensByUserId(Long id) {
        return confirmationTokenRepository.findAllByUserId(id);
    }
}
