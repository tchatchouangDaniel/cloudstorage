package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class NoteController {
    @Autowired
    private NoteService noteService;

    @GetMapping("/get-notes")
    public String getNotes(Authentication authentication, Model model){
        String username = authentication.getName();
        List<Note> noteList = noteService.getAllNotes(username);
        model.addAttribute("noteList", noteList);
        return "home";
    }

    @PostMapping("/save-note")
    public String saveNote(@RequestParam(required = false) Integer noteId, @RequestParam String noteTitle, @RequestParam String noteDescription, Authentication authentication, Model model){

        String username = authentication.getName();
        Note note = new Note(noteId,noteTitle,noteDescription,null);
        if(noteId == null){
            noteService.saveNote(note, username);
        }else{
            noteService.saveNote(note,username, noteId);
        }
        model.addAttribute("success",true);
        return "result";
    }

    @GetMapping("/delete-note/{noteId}")
    public String deleteNote(@PathVariable String noteId, Authentication authentication,Model model){
        String username = authentication.getName();
        try{
            int id = Integer.parseInt(noteId);
            noteService.deleteNote(id,username);
            model.addAttribute("success",true);
        }catch (NumberFormatException nfe){
            model.addAttribute("error",true);
            model.addAttribute("errorMessage", nfe.getMessage());
        }
        return "result";
    }

}
