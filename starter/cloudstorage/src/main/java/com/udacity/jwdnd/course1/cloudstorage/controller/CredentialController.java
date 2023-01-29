package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CredentialController {
    @Autowired
    private CredentialService credentialService;

    @GetMapping("/get-credential")
    public String getCredentials(Authentication authentication, Model model){
        List<Credential> credentialList = credentialService.findAllCredential(authentication.getName());
        model.addAttribute("credentialList",credentialList);
        return "home";
    }

    @PostMapping("/save-credential")
    public String saveCredentials(@RequestParam(required = false) Integer credentialId, @RequestParam String url, @RequestParam String username, @RequestParam String password, Authentication authentication, Model model){
        int status = -1;
        String userName = authentication.getName();

        if(credentialId == null){
            Credential credential = new Credential(null,url,username,null,password,null);
            status = credentialService.saveCredential(credential, userName);
        }else{
            Credential credential = new Credential(credentialId,url,username,null,password,null);
            status = credentialService.updateCredential(credential, userName);
        }
        if(status >= 0){
            model.addAttribute("success", true);
        }else{
            model.addAttribute("error",true);
        }
        return "result";
    }

    @GetMapping("/delete-credential/{credentialId}")
    public String deleteCredential(@PathVariable String credentialId, Authentication authentication, Model model){
        String username = authentication.getName();
        try{
            int id = Integer.parseInt(credentialId);
            credentialService.deleteCredential(id,username);
            model.addAttribute("success",true);
        }catch (NumberFormatException nfe){
            model.addAttribute("error",true);
            model.addAttribute("errorMessage", nfe.getMessage());
        }
        return "result";
    }
}
