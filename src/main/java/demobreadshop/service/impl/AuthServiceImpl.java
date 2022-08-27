package demobreadshop.service.impl;

import demobreadshop.constants.ConstProperties;
import demobreadshop.domain.Delivery;
import demobreadshop.domain.ProductList;
import demobreadshop.domain.Role;
import demobreadshop.domain.User;
import demobreadshop.domain.enums.RoleName;
import demobreadshop.payload.LoginDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.RegisterDto;
import demobreadshop.payload.ResultLogin;
import demobreadshop.repository.DeliveryRepository;
import demobreadshop.repository.RoleRepository;
import demobreadshop.repository.UserRepository;
import demobreadshop.security.JwtProvider;
import demobreadshop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, DeliveryRepository deliveryRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public HttpEntity<?> login(LoginDto dto) {
        Optional<User> byPhoneNumber = userRepository.findByPhoneNumber(dto.getPhoneNumber());
        if (byPhoneNumber.isPresent()) {
            User user = byPhoneNumber.get();
            if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                AuthenticationManager authenticationManager = authentication -> new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );
                return ResponseEntity.ok(
                        new ResultLogin(
                                "Successfully login",
                                true,
                                jwtProvider.generateToken(
                                        user.getUsername(),
                                        user.getRoles()
                                ),
                                user.getRoles().stream().findFirst()
                        )
                );
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MyResponse.WRONG_PASSWORD);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MyResponse.USER_NOT_FOUND);
    }

    @Override
    public MyResponse register(RegisterDto dto) {
        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            return MyResponse.PHONE_NUMBER_EXISTS;
        }
        if (userRepository.existsByFullName(dto.getFullName())) {
            return MyResponse.FULL_NAME_EXISTS;
        }
        final Optional<Role> byId = roleRepository.findById(dto.getRoleId());
        if (byId.isPresent()) {
            final Role role = byId.get();
            User user = new User(
                    dto.getFullName(),
                    dto.getPhoneNumber(),
                    passwordEncoder.encode(dto.getPassword()),
                    Collections.singleton(role),
                    dto.getUserKPI()
            );

            if (role.getRoleName().name().equals(RoleName.SELLER_CAR.name())) {
                user = userRepository.save(user);
                deliveryRepository.save(
                        new Delivery(
                                user,
                                new HashSet<>()
                        )
                );
            }

            return MyResponse.SUCCESSFULLY_CREATED;
        }
        return MyResponse.ROLE_NOT_FOUND;
    }

    @Override
    public User me() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (
                authentication != null
                        && authentication.isAuthenticated()
                        && !authentication.getPrincipal().equals("anonymousUser")
        ) return (User) authentication.getPrincipal();
        return null;
    }

    @Override
    public MyResponse update(long id, User dto) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();

            if (userRepository.existsByPhoneNumberAndIdIsNot(dto.getPhoneNumber(), id)) {
                return MyResponse.PHONE_NUMBER_EXISTS;
            }

            user.setPhoneNumber(dto.getPhoneNumber());
            user.setUserKPI(dto.getUserKPI());

            userRepository.save(user);
            return MyResponse.SUCCESSFULLY_UPDATED;
        }
        return MyResponse.USER_NOT_FOUND;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    static boolean isNonDeletable(long time) {
        long minute = System.currentTimeMillis() - time;
        minute /= 60 * 1000L;
        return minute > ConstProperties.DELETE_TIME;
    }
}
