package dev.andre.accidentalert.ambulatory.service;

import dev.andre.accidentalert.ambulatory.dto.request.ChangePasswordDTO;
import dev.andre.accidentalert.ambulatory.dto.request.CreateUserDTO;
import dev.andre.accidentalert.ambulatory.dto.request.UpdateUserRoleDTO;
import dev.andre.accidentalert.ambulatory.dto.response.UserResponseDTO;
import dev.andre.accidentalert.ambulatory.entity.User;
import dev.andre.accidentalert.ambulatory.entity.enums.Role;
import dev.andre.accidentalert.ambulatory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(CreateUserDTO dto){

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode("123456"))
                .mustChangePassword(true)
                .role(Role.STAFF)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getActive()

        );
    }

    public void changePassword(ChangePasswordDTO dto, String email) {

        User user = userRepository  .findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND
                        , "User not found"));

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED
                    ,"Current password is incorrect");
        }

        if (passwordEncoder.matches(dto.newPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                    ,"New password cannot be the same as the current password");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        user.setMustChangePassword(false);
        userRepository.save(user);
    }

    public void deactivateUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND
                , "User not found"));

        user.setActive(false);
        userRepository.save(user);
    }

   public List<UserResponseDTO> findUsersByActive(boolean active) {
        return userRepository.findByActive(active).stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getActive()
                ))
                .toList();
   }

   public void updateUserRole(Long id, UpdateUserRoleDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND
                        , "User not found"));

        user.setRole(dto.role());
        userRepository.save(user);
   }

    public UserResponseDTO findUserById(Long id) {
          User user = userRepository.findById(id)
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND
                            , "User not found"));

          return new UserResponseDTO(
                 user.getId(),
                 user.getName(),
                 user.getEmail(),
                 user.getRole(),
                 user.getActive()
          );
    }

}
