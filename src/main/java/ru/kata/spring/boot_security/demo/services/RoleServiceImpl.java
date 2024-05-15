package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RolesRepository;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private RolesRepository rolesRepository;

    @Autowired
    public RoleServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public List<Role> findAll() {
        return rolesRepository.findAll();
    }

    public void save(Role role) {
        rolesRepository.save(role);
    }

    @Override
    public Role findOne(int id) {
        return rolesRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(int id) {
        rolesRepository.deleteById(id);
    }
}
