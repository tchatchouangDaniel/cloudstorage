package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;
    private UserService userService;

    @Autowired
    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService, UserService userService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    public int saveCredential(Credential credential, String username){
        int userId = userService.getUserId(username);
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        return credentialMapper.save(
                new Credential(
                        null,
                        credential.getUrl(),
                        credential.getUsername(),
                        encodedKey,
                        encryptedPassword,
                        userId
                )
        );
    }

    public int updateCredential(Credential credential, String username){
        int credentialId = credential.getCredentialId();
        int userId = userService.getUserId(username);
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String newEncryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        return credentialMapper.update(
                new Credential(
                        credentialId,
                        credential.getUrl(),
                        credential.getUsername(),
                        encodedKey,
                        newEncryptedPassword,
                        userId
                )
        );
    }

    public List<Credential> findAllCredential(String username){
        int userId = userService.getUserId(username);
        return credentialMapper.findAllCredential(userId).stream().map(credential -> {
            String keyEntry = credential.getKey();
            String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), keyEntry);
            return new Credential(credential.getCredentialId(), credential.getUrl(), credential.getUsername(), credential.getKey(), decryptedPassword, userId);
        }).collect(Collectors.toList());
    }

    public void deleteCredential(int credentialId, String username){
        int userId = userService.getUserId(username);
        credentialMapper.delete(credentialId, userId);
    }
}
