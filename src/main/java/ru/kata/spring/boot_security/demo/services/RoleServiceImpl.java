package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RolesRepository;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RolesRepository rolesRepository;

    @Autowired
    public RoleServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return rolesRepository.findAll();
    }

    @Transactional
    public void save(Role role) {
        rolesRepository.save(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Role findOne(int id) {
        return rolesRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        rolesRepository.deleteById(id);
    }
}
