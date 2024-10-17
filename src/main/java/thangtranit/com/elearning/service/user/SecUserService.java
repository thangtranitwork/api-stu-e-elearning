package thangtranit.com.elearning.service.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.entity.user.Platform;
import thangtranit.com.elearning.entity.user.User;
import thangtranit.com.elearning.entity.user.SecUser;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.repository.user.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecUserService implements UserDetailsService {
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByEmailAndPlatform(username, Platform.APP);
        return user.map(SecUser::new)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
    }
}
