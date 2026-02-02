package com.devsuperior.bds04.repositories;

import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.User;
import com.devsuperior.bds04.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);

    @Query(nativeQuery = true, value = """
        SELECT tb_user.email as username, tb_user.password, tb_role.id AS roleId, tb_role.authority
        FROM tb_user
        INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id
        INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
        WHERE tb_user.email = :email
""")
    List<UserDetailsProjection> searchUserAndRoleByEmail(String email);
}
