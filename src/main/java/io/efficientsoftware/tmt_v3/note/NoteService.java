package io.efficientsoftware.tmt_v3.note;

import io.efficientsoftware.tmt_v3.project.Project;
import io.efficientsoftware.tmt_v3.project.ProjectRepository;
import io.efficientsoftware.tmt_v3.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final ProjectRepository projectRepository;

    public NoteService(final NoteRepository noteRepository,
            final ProjectRepository projectRepository) {
        this.noteRepository = noteRepository;
        this.projectRepository = projectRepository;
    }

    public List<NoteDTO> findAll(final String filter) {
        List<Note> notes;
        final Sort sort = Sort.by("id");
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            notes = noteRepository.findAllById(longFilter, sort);
        } else {
            notes = noteRepository.findAll(sort);
        }
        return notes.stream()
                .map(note -> mapToDTO(note, new NoteDTO()))
                .toList();
    }

    public NoteDTO get(final Long id) {
        return noteRepository.findById(id)
                .map(note -> mapToDTO(note, new NoteDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final NoteDTO noteDTO) {
        final Note note = new Note();
        mapToEntity(noteDTO, note);
        return noteRepository.save(note).getId();
    }

    public void update(final Long id, final NoteDTO noteDTO) {
        final Note note = noteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(noteDTO, note);
        noteRepository.save(note);
    }

    public void delete(final Long id) {
        noteRepository.deleteById(id);
    }

    private NoteDTO mapToDTO(final Note note, final NoteDTO noteDTO) {
        noteDTO.setId(note.getId());
        noteDTO.setText(note.getText());
        noteDTO.setAuthor(note.getAuthor());
        noteDTO.setProject(note.getProject() == null ? null : note.getProject().getId());
        return noteDTO;
    }

    private Note mapToEntity(final NoteDTO noteDTO, final Note note) {
        note.setText(noteDTO.getText());
        note.setAuthor(noteDTO.getAuthor());
        final Project project = noteDTO.getProject() == null ? null : projectRepository.findById(noteDTO.getProject())
                .orElseThrow(() -> new NotFoundException("project not found"));
        note.setProject(project);
        return note;
    }

}
