package com.example.note.controller;

import com.example.note.domain.User;
import com.example.note.service.NoteService;
import com.example.note.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
@Validated
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    private User currentUser(UserDetails principal) {
        return userService.findByUsername(principal.getUsername())
                .orElseThrow(() -> new IllegalStateException("현재 사용자 정보를 찾을 수 없습니다."));
    }

    @GetMapping
    public String list(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = currentUser(principal);
        model.addAttribute("notes", noteService.list(user));
        return "notes/list";
    }

    @GetMapping("/new")
    public String newForm() {
        return "notes/form";
    }

    @PostMapping
    public String create(@AuthenticationPrincipal UserDetails principal,
                         @RequestParam @NotBlank @Size(max = 100) String title,
                         @RequestParam @NotBlank String content) {
        User user = currentUser(principal);
        noteService.create(user, title, content);
        return "redirect:/notes";
    }

    @GetMapping("/{id}")
    public String view(@AuthenticationPrincipal UserDetails principal,
                       @PathVariable Long id, Model model) {
        User user = currentUser(principal);
        model.addAttribute("note", noteService.get(user, id));
        return "notes/view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@AuthenticationPrincipal UserDetails principal,
                           @PathVariable Long id, Model model) {
        User user = currentUser(principal);
        model.addAttribute("note", noteService.get(user, id));
        return "notes/form";
    }

    @PostMapping("/{id}")
    public String update(@AuthenticationPrincipal UserDetails principal,
                         @PathVariable Long id,
                         @RequestParam @NotBlank @Size(max = 100) String title,
                         @RequestParam @NotBlank String content) {
        User user = currentUser(principal);
        noteService.update(user, id, title, content);
        return "redirect:/notes/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@AuthenticationPrincipal UserDetails principal,
                         @PathVariable Long id) {
        User user = currentUser(principal);
        noteService.delete(user, id);
        return "redirect:/notes";
    }
}
