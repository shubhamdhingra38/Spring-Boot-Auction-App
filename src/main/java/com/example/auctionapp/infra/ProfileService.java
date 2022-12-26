package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Profile;
import com.example.auctionapp.dtos.ProfileDTO;
import com.example.auctionapp.domain.User;
import com.example.auctionapp.dtos.ProfileUpdateRequestDTO;
import com.example.auctionapp.exceptions.ProfileNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public ProfileDTO updateProfile(final ProfileUpdateRequestDTO profileUpdateRequest, final String userName) {
        final User user = userRepository.findByUsername(userName);
        final Profile existingProfile = user.getUserProfile();
        final Profile modifiedProfile = modelMapper.map(profileUpdateRequest, Profile.class);
        modelMapper.map(modifiedProfile, existingProfile);
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
