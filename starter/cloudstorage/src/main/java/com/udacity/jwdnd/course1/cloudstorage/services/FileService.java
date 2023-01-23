package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    private FilesMapper filesMapper;
    private UserService userService;

    @Autowired
    public FileService(FilesMapper filesMapper, UserService userService) {
        this.filesMapper = filesMapper;
        this.userService = userService;
    }

    public int uploadFile(MultipartFile file, String username) throws IOException, SQLException {
        int userId = userService.getUserId(username);
        String fileSize = String.valueOf(file.getSize());
        return filesMapper.save(new File(null,file.getOriginalFilename(),file.getContentType(),fileSize, userId, file.getBytes()));
    }

    public List<File> getUserFiles(String username){
        return filesMapper.findAllByUserId(userService.getUserId(username));
    }

    public int deleteFile(int fileId){
        return filesMapper.delete(fileId);
    }

    public Optional<File> getFile(int fileId, String username) {
        int userId = userService.getUserId(username);
        return filesMapper.findById(fileId,userId);
    }
}
