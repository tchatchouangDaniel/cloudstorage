package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private UserService userService;

    public int saveNote(Note note, String username){
        int userId = userService.getUserId(username);
        return noteMapper.create(new Note(null, note.getNoteTitle(), note.getNoteDescription(),userId));
    }

    public int saveNote(Note note, String username, int noteId){
        int userId = userService.getUserId(username);
        return noteMapper.update(new Note(noteId, note.getNoteTitle(), note.getNoteDescription(),userId));
    }

    public int deleteNote(int noteId, String username){
        int userId = userService.getUserId(username);
        return noteMapper.delete(noteId, userId);
    }

    public List<Note> getAllNotes(String username){
        int userId = userService.getUserId(username);
        return  noteMapper.getAllNotes(userId);
    }

    public Note getNote(int noteId, String username){
        int userId = userService.getUserId(username);
        return noteMapper.getNote(noteId,userId);
    }
}
