package com.example.auctionapp.restinterface;

import com.example.auctionapp.dtos.ProfileDTO;
import com.example.auctionapp.dtos.ProfileUpdateRequestDTO;
import com.example.auctionapp.exceptions.ProfileNotFoundException;
import com.example.auctionapp.infra.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PatchMapping("/profile")
    public ProfileDTO updateProfile(@Valid @RequestBody final ProfileUpdateRequestDTO profileUpdateRequest, final Principal principal) {
        final String username = principal.getName();
        return profileService.updateProfile(profileUpdateRequest, username);
    }
}
