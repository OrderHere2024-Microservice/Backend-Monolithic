package com.backend.orderhere.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserAvatarUpdateDto {
    private MultipartFile imageFile;
}
