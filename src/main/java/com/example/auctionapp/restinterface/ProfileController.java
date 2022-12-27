package com.example.auctionapp.restinterface;

import com.example.auctionapp.dtos.ProfileDTO;
import com.example.auctionapp.dtos.ProfileUpdateRequestDTO;
import com.example.auctionapp.exceptions.ProfileNotFoundException;
import com.example.auctionapp.infra.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile")
    public ProfileDTO getCurrentUserProfile(final Principal principal) {
        return profileService.getProfile(principal.getName());
    }

    @GetMapping("/profile/{profileId}")
    public ProfileDTO getProfile(@PathVariable Long profileId) throws ProfileNotFoundException {
        System.out.println("Profile ID: " + profileId);
        return profileService.getProfile(profileId);
    }

    @PatchMapping(value = "/profile", consumes = {"multipart/form-data"})
    public ProfileDTO updateProfile(
            @RequestPart("profile") final ProfileUpdateRequestDTO profileUpdateRequest,
            @RequestPart(required = false) final MultipartFile image, final Principal principal) throws IOException {
        final String username = principal.getName();
        return profileService.updateProfile(profileUpdateRequest, image, username);
    }
}
