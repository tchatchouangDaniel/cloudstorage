package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userid=#{userId}")
    List<Credential> findAllCredential(int userId);

    @Select("SELECT * FROM CREDENTIALS WHERE userid=#{userId} AND credentialid=#{credentialId}")
    Credential findCredentialById(int userId, int credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, keyentry, password, userId) " +
            "VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int save(Credential credential);

    @Update("UPDATE CREDENTIALS SET url=#{url},username=#{username},keyentry=#{key}, password=#{password} WHERE userid=#{userId} AND credentialid=#{credentialId}")
    int update(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid=#{credentialId} AND userid=#{userId}")
    int delete(int credentialId, int userId);
}
