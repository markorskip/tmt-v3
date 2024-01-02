package io.efficientsoftware.tmt_v3.project;

import io.efficientsoftware.tmt_v3.authority.Authorities;
import io.efficientsoftware.tmt_v3.user.User;
import io.efficientsoftware.tmt_v3.user.UserRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/projects")
@PreAuthorize("hasAuthority('" + Authorities.Fields.ROLE_USER_BASIC + "')")
public class ProjectController {

    private final ProjectService projectService;
    private final UserRepository userRepository;

    public ProjectController(final ProjectService projectService,
            final UserRepository userRepository) {
        this.projectService = projectService;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("ownerValues", userRepository.findAll(Sort.by("email"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getEmail, User::getEmail)));
    }


    @GetMapping("/{id}")
    public String projectExplorer(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("project", projectService.get(id));
        return "explorer/explorer";
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("projects", projectService.findAll());
        return "project/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("project") final ProjectDTO projectDTO) {
        return "project/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("project") @Valid final ProjectDTO projectDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "project/add";
        }
        projectService.create(projectDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("project.create.success"));
        return "redirect:/projects";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("project", projectService.get(id));
        return "project/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("project") @Valid final ProjectDTO projectDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "project/edit";
        }
        projectService.update(id, projectDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("project.update.success"));
        return "redirect:/projects";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = projectService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            projectService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("project.delete.success"));
        }
        return "redirect:/projects";
    }

}
