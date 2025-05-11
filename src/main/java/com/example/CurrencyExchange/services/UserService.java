package com.example.CurrencyExchange.services;

import com.example.CurrencyExchange.dto.SafetyUserDTO;
import com.example.CurrencyExchange.dto.UserDTO;
import com.example.CurrencyExchange.dto.UserRoleDTO;
import com.example.CurrencyExchange.entities.User;
import com.example.CurrencyExchange.entities.UserRole;
import com.example.CurrencyExchange.jwt.JWTCore;
import com.example.CurrencyExchange.repositories.UserRepository;
import com.example.CurrencyExchange.utils.mapping.SafetyUserMapper;
import com.example.CurrencyExchange.utils.mapping.UserMapper;
import com.example.CurrencyExchange.utils.mapping.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SafetyUserMapper safetyUserMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTCore jwtCore;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserRoleMapper userRoleMapper;

    public List<SafetyUserDTO> getUsers(){
        return userRepository.findAll(Sort.by("id"))
                .stream()
                .map(safetyUserMapper::fromEntityToDTO)
                .toList();
    }

    public List<SafetyUserDTO> getUsers(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        return userRepository.findAll(pageable)
                .stream()
                .map(safetyUserMapper::fromEntityToDTO)
                .toList();
    }

    public SafetyUserDTO getUser(Long id){
        return safetyUserMapper.fromEntityToDTO(userRepository.findById(id).orElse(null));
    }

    public String register(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent() ||
            userRepository.findByName(userDTO.getName()).isPresent() ||
            userRepository.findByPhone(userDTO.getPhone()).isPresent()
        ) return null;

        User user = userMapper.fromDTOToEntity(userDTO);
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        UserRoleDTO userRoleDTO = userRoleService.getUserRole("user");
        user.setRole(userRoleMapper.fromDTOToEntity(userRoleDTO));

        userRepository.save(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
        );

        return jwtCore.generateToken(authentication);
    }

    public String login(UserDTO userDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtCore.generateToken(authentication);
    }

    public SafetyUserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return null;

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());

        try {
            user = userRepository.save(user);
        } catch(DataAccessException e) {
            return null;
        }
        return safetyUserMapper.fromEntityToDTO(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
