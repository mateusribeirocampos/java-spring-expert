package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.projections.UserDetailsProjection;
import com.devsuperior.dscommerce.repositories.UserRepository;
import com.devsuperior.dscommerce.tests.UserDetailsFactory;
import com.devsuperior.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user;
    private String existingUsername, nonExistingUsername;
    private List<UserDetailsProjection> userDetailsProjections;

    @BeforeEach
    void setUp() throws Exception {

        existingUsername = "maria@gmail.com";
        nonExistingUsername = "user@gmail.com";

        user = UserFactory.createCustomClientUser(1L, existingUsername);
        userDetailsProjections = UserDetailsFactory.createCustomAdminUser(existingUsername);

        Mockito.when(userRepository.searchUserAndRolesByEmail(existingUsername)).thenReturn(userDetailsProjections);
        Mockito.when(userRepository.searchUserAndRolesByEmail(nonExistingUsername)).thenReturn(new ArrayList<>());
    }

    @Test
    public void loadUserByUsernameShouldReturnDetailsWhenUserExists() {
        UserDetails result = userService.loadUserByUsername(existingUsername);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(), existingUsername);
    }

    @Test
    public void loadUserByUsernameShouldReturnThrowUserNotFoundExceptionWhenUserDoesNotExists() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
           userService.loadUserByUsername(nonExistingUsername);
        });
    }
}
