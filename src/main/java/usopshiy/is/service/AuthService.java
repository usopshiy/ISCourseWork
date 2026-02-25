package usopshiy.is.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usopshiy.is.dto.JwtAuthenticationResponse;
import usopshiy.is.dto.SignInRequest;
import usopshiy.is.dto.SignUpRequest;
import usopshiy.is.entity.User;
import usopshiy.is.exception.UserNotFoundException;

/**
 * Сервис для аутентификации и регистрации пользователей.
 * Обрабатывает операции входа в систему и регистрации новых пользователей,
 * взаимодействуя с UserService, JwtService и AuthenticationManager.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрирует нового пользователя в системе.
     * <p>
     * Процесс регистрации включает:
     * <ul>
     *   <li>Создание объекта пользователя на основе данных запроса</li>
     *   <li>Кодирование пароля перед сохранением</li>
     *   <li>Сохранение пользователя через UserService</li>
     *   <li>Генерацию JWT-токена для автоматического входа после регистрации</li>
     * </ul>
     *
     * @param request объект запроса на регистрацию, содержащий имя пользователя,
     *                пароль и роль
     * @return JwtAuthenticationResponse с сгенерированным JWT-токеном
     * @throws IllegalArgumentException    если запрос содержит некорректные данные
     */
    @Transactional
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        log.info("Attempting to register new user with username: {}", request.getUsername());

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userService.create(user);
        log.info("User '{}' successfully registered", request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Выполняет аутентификацию пользователя и вход в систему.
     * <p>
     * Процесс входа включает:
     * <ul>
     *   <li>Аутентификацию учетных данных через AuthenticationManager</li>
     *   <li>Загрузку данных пользователя из системы</li>
     *   <li>Генерацию JWT-токена для последующих авторизованных запросов</li>
     * </ul>
     *
     * @param request объект запроса на вход, содержащий имя пользователя и пароль
     * @return JwtAuthenticationResponse с сгенерированным JWT-токеном
     * @throws BadCredentialsException если предоставлены неверные учетные данные
     * @throws UserNotFoundException   если пользователь с указанным именем не найден
     */
    @Transactional(readOnly = true)
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        log.info("Authentication attempt for user: {}", request.getUsername());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            ));
            log.debug("Authentication successful for user: {}", request.getUsername());
        } catch (BadCredentialsException e) {
            log.warn("Authentication failed for user: {} - invalid credentials", request.getUsername());
            throw new BadCredentialsException("Invalid username or password");
        }

        UserDetails userDetails = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(userDetails);
        log.info("User '{}' successfully signed in", request.getUsername());

        return new JwtAuthenticationResponse(jwt);
    }
}