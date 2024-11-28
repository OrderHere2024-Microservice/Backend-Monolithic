package com.backend.orderhere.auth;

import com.backend.orderhere.dto.user.*;
import com.backend.orderhere.service.storageService.StorageService;
import jakarta.transaction.Transactional;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeyCloakService {

    private final static String INIT_AVATAR_URL = "SOME_DEFAULT_URL";

    @Value("${storage.bucketName}")
    private String bucketName;

    @Value("${keyCloak.realm}")
    private String realm;

    private final Keycloak keycloak;
    private final StorageService storageService;

    private final JwtUtil jwtUtil;

    public KeyCloakService(Keycloak keycloak, StorageService storageService, JwtUtil jwtUtil) {
        this.storageService = storageService;
        this.keycloak = keycloak;
        this.jwtUtil = jwtUtil;
    }

    public String getUsernameFromKeycloak(String userId) {
        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
        return user.getUsername();
    }

    public UserGetDto getUserProfile(String token) {

        String userId = jwtUtil.getUserIdFromToken(token);

        UserResource userResource = keycloak.realm(realm).users().get(userId);
        UserRepresentation user = userResource.toRepresentation();

        String avatarUrl = null;
        if (user.getAttributes() != null && user.getAttributes().containsKey("avatar_url")) {
            avatarUrl = user.getAttributes().get("avatar_url").get(0);
        }

        return new UserGetDto(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                0,
                avatarUrl != null ? avatarUrl : INIT_AVATAR_URL,
                "DEFAULT_ADDRESS"
        );
    }

    @Transactional
    public UserProfileUpdateDTO updateUserProfile(String userId, UserProfileUpdateDTO userProfileUpdateDTO) {

        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();

        user.setUsername(userProfileUpdateDTO.getUsername());
        user.setFirstName(userProfileUpdateDTO.getFirstname());
        user.setLastName(userProfileUpdateDTO.getLastname());

        keycloak.realm(realm).users().get(userId).update(user);

        return userProfileUpdateDTO;
    }

    @Transactional
    public UserProfileUpdateDTO updateUserProfileWithToken(String token, UserProfileUpdateDTO userProfileUpdateDTO) {

        String userId = jwtUtil.getUserIdFromToken(token);

        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();

        user.setUsername(userProfileUpdateDTO.getUsername());
        user.setFirstName(userProfileUpdateDTO.getFirstname());
        user.setLastName(userProfileUpdateDTO.getLastname());

        keycloak.realm(realm).users().get(userId).update(user);

        return userProfileUpdateDTO;
    }

    @Transactional
    public String updateUserAvatar(String token, UserAvatarUpdateDto userAvatarUpdateDto) throws Exception {

        String userId = jwtUtil.getUserIdFromToken(token);

        UserRepresentation userRepresentation = keycloak.realm(realm).users().get(userId).toRepresentation();

        String oldImageUrl = userRepresentation.getAttributes() != null
                ? userRepresentation.getAttributes().getOrDefault("avatar_url", List.of(INIT_AVATAR_URL)).get(0)
                : INIT_AVATAR_URL;

        String imageUrl = storageService.uploadFile(userAvatarUpdateDto.getImageFile(), bucketName);

        Map<String, List<String>> attributes = userRepresentation.getAttributes() != null
                ? userRepresentation.getAttributes()
                : new HashMap<>();
        attributes.put("avatar_url", List.of(imageUrl));
        userRepresentation.setAttributes(attributes);

        keycloak.realm(realm).users().get(userId).update(userRepresentation);

        if (!oldImageUrl.equals(INIT_AVATAR_URL)) {
            storageService.deleteFile(bucketName, oldImageUrl);
        }

        return imageUrl;
    }

}
