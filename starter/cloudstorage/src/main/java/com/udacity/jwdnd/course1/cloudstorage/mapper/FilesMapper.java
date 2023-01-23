package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FilesMapper {
    @Select("SELECT * FROM FILES WHERE userId=#{userId}")
    List<File> findAllByUserId(int userId);

//    @Select("SELECT * FROM FILES")
//    List<File> findAll();
//
    @Select("SELECT * FROM FILES WHERE fileId=#{id} AND userid=#{userId}")
    Optional<File> findById(int id, int userId);

    @Insert("INSERT INTO FILES (filename, contentType, fileSize, fileData, userId) VALUES (#{filename}, #{contentType}, #{fileSize}, #{fileData}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int save(File file);

//    @Update("UPDATE FILES SET filename = #{file.filename}, contentType = #{file.contentType}, fileSize = #{file.fileSize}, fileData = #{file.fileData} WHERE ID = #{id}")
//    void update(File file, int id);
//
    @Delete("DELETE from FILES WHERE fileId=#{id}")
    int delete(int id);
}
