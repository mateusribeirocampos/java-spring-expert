package com.devsuperior.demo.services;

import com.devsuperior.demo.entities.User;
import com.devsuperior.demo.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    /**
     * Valida se o usuário autenticado é ADMIN ou se é o próprio usuário (self).
     * Lança ForbiddenException se não tiver permissão.
     *
     * @param userId ID do usuário a ser validado
     * @throws ForbiddenException se não for ADMIN nem o próprio usuário
     */
    public void validateSelfOrAdmin(long userId) {
        User me = userService.authenticated();
        if (!me.hasRole("ROLE_ADMIN") &&
                !me.getId().equals(userId)) {
            throw new ForbiddenException("Access denied");
        }
    }

    /**
     * Valida se o usuário autenticado é ADMIN.
     * Lança ForbiddenException se não tiver a role ROLE_ADMIN.
     *
     * @throws ForbiddenException se não for ADMIN
     */
    public void validateAdmin() {
        User me = userService.authenticated();
        if (!me.hasRole("ROLE_ADMIN")) {
            throw new ForbiddenException("Access denied. Only admin can do this operation");
        }
    }
}
