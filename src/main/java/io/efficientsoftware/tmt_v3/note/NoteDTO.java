package io.efficientsoftware.tmt_v3.note;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NoteDTO {

    private Long id;

    @NotNull
    @Size(max = 5000)
    private String text;

    @NotNull
    @Size(max = 255)
    private String author;

    private Long project;

}
