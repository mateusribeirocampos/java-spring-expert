package com.devsuperior.bds02.services;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServices {

    @Autowired
    private EventRepository repository;

    public EventDTO update(Long id, EventDTO dto) {
        try {
            Event entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
        }
    }

    private void copyDtoToEntity(EventDTO dto, Event entity) {
        entity.setName(dto.getName());
        entity.setDate(dto.getDate());
        entity.setUrl(dto.getUrl());
    }
}
