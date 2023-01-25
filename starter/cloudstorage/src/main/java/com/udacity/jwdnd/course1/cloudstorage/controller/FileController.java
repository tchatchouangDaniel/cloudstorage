package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/file-upload")
public class FileController {
    private FileService fileService;
    private UserService userService;

    @Autowired
    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping("/get")
    public String getFiles(Authentication authentication,Model model){
        String username = authentication.getName();
        List<File> fileList = fileService.getUserFiles(username);
        model.addAttribute("fileList",fileList);
        return "home";
    }

    @PostMapping("/save")
    public String saveFile(@RequestParam("fileUpload") MultipartFile file, Authentication authentication, Model model) throws SQLException, IOException {
        String username = authentication.getName();
        int saveStatus = fileService.uploadFile(file,username);
        if(saveStatus < 0){
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", "The file was not saved");
        }else{
            model.addAttribute("success", true);
        }
        return "result";
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable String fileId, Model model){
        try {
            int id = Integer.parseInt(fileId);
            fileService.deleteFile(id);
            model.addAttribute("success", true);
        }catch (NumberFormatException nfe){
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", "The file was not deleted");
        }
        return "result";
    }

    @GetMapping("/view/{fileId}")
    public ResponseEntity downloadFile(@PathVariable String fileId,Authentication authentication, Model model){
        try {
            int id = Integer.parseInt(fileId);
            Optional<File> fileOpt = fileService.getFile(id,authentication.getName());
            if(fileOpt.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            File file = fileOpt.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(file.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file.getFileData());
        }catch (NumberFormatException nfe){
            return ResponseEntity.badRequest().build();
        }
    }

}
