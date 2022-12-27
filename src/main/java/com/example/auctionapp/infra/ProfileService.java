package com.example.auctionapp.infra;

import com.example.auctionapp.accessors.S3Accessor;
import com.example.auctionapp.domain.Profile;
import com.example.auctionapp.dtos.ProfileDTO;
import com.example.auctionapp.domain.User;
import com.example.auctionapp.dtos.ProfileUpdateRequestDTO;
import com.example.auctionapp.exceptions.ProfileNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ProfileService {
    private final String PROFILE_DIRECTORY = "profile";
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private S3Accessor s3Accessor;

    public ProfileDTO updateProfile(final ProfileUpdateRequestDTO profileUpdateRequest, final MultipartFile image, final String userName) throws IOException {
        final User user = userRepository.findByUsername(userName);
        final Profile existingProfile = user.getUserProfile();
        final Profile modifiedProfile = modelMapper.map(profileUpdateRequest, Profile.class);
        modelMapper.map(modifiedProfile, existingProfile);

        // Save profile picture
        // TODO: Cleanup directory post saving
        if (image != null && !image.isEmpty()) {
            final String directoryName = userName + File.separator + PROFILE_DIRECTORY;
            final String s3ImageUrl = s3Accessor.putS3Object(image, directoryName);
            existingProfile.setProfilePictureS3URL(s3ImageUrl);
        }
        final Profile savedProfile = profileRepository.save(existingProfile);
        return convertToDto(savedProfile);
    }

    public ProfileDTO getProfile(final String userName) {
        final User user = userRepository.findByUsername(userName);
        return convertToDto(user.getUserProfile());
    }
    public ProfileDTO getProfile(long profileId) throws ProfileNotFoundException {
        final Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException());
        return convertToDto(profile);
    }

    private ProfileDTO convertToDto(final Profile profile) {
        return modelMapper.map(profile, ProfileDTO.class);
    }

    private Profile convertToEntity(final ProfileDTO profileDTO) {
        final Profile profile = modelMapper.map(profileDTO, Profile.class);
        profile.setId(null);
        return profile;
    }
}
