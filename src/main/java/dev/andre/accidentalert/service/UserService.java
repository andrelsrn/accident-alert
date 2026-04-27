package dev.andre.accidentalert.service;

import dev.andre.accidentalert.dto.request.UserRequestDTO;
import dev.andre.accidentalert.dto.response.UserResponseDTO;
import dev.andre.accidentalert.entity.User;
import dev.andre.accidentalert.entity.enums.Role;
import dev.andre.accidentalert.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO dto){

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(dto.password())
                .role(Role.ROLE_STAFF)
                .build();

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()

        );
    }




}
