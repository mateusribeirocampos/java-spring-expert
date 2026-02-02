package com.devsuperior.bds04.services;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.dto.UserDTO;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.entities.User;
import com.devsuperior.bds04.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Transactional
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> entityList = repository.findAll(pageable);
        return entityList.map(x -> new UserDTO(x));
    }
}
