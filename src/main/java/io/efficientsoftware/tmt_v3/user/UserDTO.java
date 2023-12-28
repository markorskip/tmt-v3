package io.efficientsoftware.tmt_v3.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    private Boolean enabled;

    @Size(max = 255)
    private String nickname;

    private List<Long> readProjects;

    private List<Long> writeProjects;

}
