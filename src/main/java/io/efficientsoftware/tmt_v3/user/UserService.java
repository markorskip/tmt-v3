package io.efficientsoftware.tmt_v3.user;

import io.efficientsoftware.tmt_v3.authority.Authority;
import io.efficientsoftware.tmt_v3.authority.AuthorityRepository;
import io.efficientsoftware.tmt_v3.project.Project;
import io.efficientsoftware.tmt_v3.project.ProjectRepository;
import io.efficientsoftware.tmt_v3.util.NotFoundException;
import io.efficientsoftware.tmt_v3.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public UserService(final UserRepository userRepository,
            final ProjectRepository projectRepository, final PasswordEncoder passwordEncoder,
            final AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("email"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final String email) {
        return userRepository.findById(email)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        user.setEmail(userDTO.getEmail());
        return userRepository.save(user).getEmail();
    }

    public void update(final String email, final UserDTO userDTO) {
        final User user = userRepository.findById(email)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final String email) {
        userRepository.deleteById(email);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setEmail(user.getEmail());
        userDTO.setEnabled(user.getEnabled());
        userDTO.setNickname(user.getNickname());
        userDTO.setReadProjects(user.getReadProjects().stream()
                .map(project -> project.getId())
                .toList());
        userDTO.setWriteProjects(user.getWriteProjects().stream()
                .map(project -> project.getId())
                .toList());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEnabled(userDTO.getEnabled());
        user.setNickname(userDTO.getNickname());
        final List<Project> readProjects = projectRepository.findAllById(
                userDTO.getReadProjects() == null ? Collections.emptyList() : userDTO.getReadProjects());
        if (readProjects.size() != (userDTO.getReadProjects() == null ? 0 : userDTO.getReadProjects().size())) {
            throw new NotFoundException("one of readProjects not found");
        }
        user.setReadProjects(readProjects.stream().collect(Collectors.toSet()));
        final List<Project> writeProjects = projectRepository.findAllById(
                userDTO.getWriteProjects() == null ? Collections.emptyList() : userDTO.getWriteProjects());
        if (writeProjects.size() != (userDTO.getWriteProjects() == null ? 0 : userDTO.getWriteProjects().size())) {
            throw new NotFoundException("one of writeProjects not found");
        }
        user.setWriteProjects(writeProjects.stream().collect(Collectors.toSet()));
        return user;
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public String getReferencedWarning(final String email) {
        final User user = userRepository.findById(email)
                .orElseThrow(NotFoundException::new);
        final Project ownerProject = projectRepository.findFirstByOwner(user);
        if (ownerProject != null) {
            return WebUtils.getMessage("user.project.owner.referenced", ownerProject.getId());
        }
        final Authority userAuthority = authorityRepository.findFirstByUser(user);
        if (userAuthority != null) {
            return WebUtils.getMessage("user.authority.user.referenced", userAuthority.getId());
        }
        return null;
    }

}
