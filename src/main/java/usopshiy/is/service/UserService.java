package usopshiy.is.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usopshiy.is.entity.User;
import usopshiy.is.exception.UserAlreadyExistsException;
import usopshiy.is.exception.UserNotFoundException;
import usopshiy.is.repository.UserRepository;

/**
 * Сервис для управления пользователями системы.
 * Предоставляет функционал для создания, получения и аутентификации пользователей,
 * а также для получения информации о текущем авторизованном пользователе.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Сохраняет пользователя в базе данных.
     *
     * @param user объект пользователя для сохранения
     */
    @Transactional
    public void save(User user) {
        log.debug("Saving user: {}", user != null ? user.getUsername() : "null");
        User savedUser = userRepository.save(user);
        log.info("User saved successfully with id: {}", savedUser.getId());
    }

    /**
     * Создает нового пользователя в системе.
     * <p>
     * Перед созданием проверяет уникальность имени пользователя.
     * Если пользователь с таким именем уже существует, выбрасывает исключение.
     *
     * @param user объект нового пользователя
     * @throws UserAlreadyExistsException если пользователь с таким именем уже существует
     */
    @Transactional
    public void create(User user) {
        log.info("Attempting to create new user with username: {}",
                user != null ? user.getUsername() : "null");

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("Cannot create user: username '{}' already exists", user.getUsername());
            throw new UserAlreadyExistsException("User with username '" + user.getUsername() + "' already exists");
        }

        save(user);
        log.info("User '{}' created successfully", user.getUsername());
    }

    /**
     * Возвращает пользователя по его имени пользователя.
     *
     * @param username имя пользователя для поиска
     * @return найденный пользователь
     * @throws UserNotFoundException если пользователь с указанным именем не найден
     * @throws IllegalArgumentException если username равен null или пуст
     */
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        log.debug("Fetching user by username: '{}'", username);

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.warn("User with username '{}' not found", username);
            throw new UserNotFoundException("User with username '" + username + "' not found");
        }

        log.debug("User found: {}", username);
        return user;
    }

    /**
     * Предоставляет сервис для загрузки пользователя по имени (необходимо для Spring Security).
     *
     * @return UserDetailsService, использующий метод {@link #getByUsername(String)}
     */
    public UserDetailsService userDetailsService() {
        log.debug("Providing UserDetailsService");
        return this::getByUsername;
    }

    /**
     * Возвращает текущего аутентифицированного пользователя.
     * <p>
     * Извлекает имя пользователя из контекста безопасности Spring Security
     * и выполняет поиск пользователя в базе данных.
     *
     * @return текущий аутентифицированный пользователь
     * @throws UserNotFoundException если пользователь не найден или не аутентифицирован
     */
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        log.debug("Fetching current authenticated user");

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            log.warn("No authenticated user found in security context");
            throw new UserNotFoundException("No authenticated user found");
        }

        String username = authentication.getName();
        log.debug("Current username from security context: {}", username);

        return getByUsername(username);
    }
}