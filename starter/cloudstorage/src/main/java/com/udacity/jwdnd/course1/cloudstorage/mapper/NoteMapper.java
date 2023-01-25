package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid=#{userId}")
    List<Note> getAllNotes(int userId);

    @Select("SELECT * FROM NOTES WHERE noteid=#{noteId} AND userid=#{userId}")
    Note getNote(int noteId, int userId);

    @Insert("INSERT INTO NOTES (notetitle,notedescription,userid) VALUES(#{noteTitle},#{noteDescription},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int create(Note note);

    @Update("UPDATE NOTES SET notetitle=#{noteTitle}, notedescription=#{noteDescription} WHERE noteid=#{noteId} AND userid=#{userId}")
    int update(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid=#{noteId} AND userid=#{userId}")
    int delete(int noteId, int userId);
}
