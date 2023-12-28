package io.efficientsoftware.tmt_v3.note;

import io.efficientsoftware.tmt_v3.authority.Authorities;
import io.efficientsoftware.tmt_v3.project.Project;
import io.efficientsoftware.tmt_v3.project.ProjectRepository;
import io.efficientsoftware.tmt_v3.util.CustomCollectors;
import io.efficientsoftware.tmt_v3.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/notes")
@PreAuthorize("hasAuthority('" + Authorities.Fields.ROLE_USER_BASIC + "')")
public class NoteController {

    private final NoteService noteService;
    private final ProjectRepository projectRepository;

    public NoteController(final NoteService noteService,
            final ProjectRepository projectRepository) {
        this.noteService = noteService;
        this.projectRepository = projectRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("projectValues", projectRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Project::getId, Project::getName)));
    }

    @GetMapping
    public String list(@RequestParam(name = "filter", required = false) final String filter,
            final Model model) {
        model.addAttribute("notes", noteService.findAll(filter));
        model.addAttribute("filter", filter);
        return "note/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("note") final NoteDTO noteDTO) {
        return "note/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("note") @Valid final NoteDTO noteDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "note/add";
        }
        noteService.create(noteDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("note.create.success"));
        return "redirect:/notes";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("note", noteService.get(id));
        return "note/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("note") @Valid final NoteDTO noteDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "note/edit";
        }
        noteService.update(id, noteDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("note.update.success"));
        return "redirect:/notes";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        noteService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("note.delete.success"));
        return "redirect:/notes";
    }

}
