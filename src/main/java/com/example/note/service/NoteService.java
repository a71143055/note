package com.example.note.service;

import com.example.note.domain.Note;
import com.example.note.domain.User;
import com.example.note.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> list(User user) {
        return noteRepository.findByUserOrderByUpdatedAtDesc(user);
    }

    public Note create(User user, String title, String content) {
        Note note = new Note();
        note.setUser(user);
        note.setTitle(title);
        note.setContent(content);
        return noteRepository.save(note);
    }

    public Note update(User user, Long id, String title, String content) {
        Note note = noteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다."));
        note.setTitle(title);
        note.setContent(content);
        return noteRepository.save(note);
    }

    public void delete(User user, Long id) {
        Note note = noteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다."));
        noteRepository.delete(note);
    }

    public Note get(User user, Long id) {
        return noteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다."));
    }
}
