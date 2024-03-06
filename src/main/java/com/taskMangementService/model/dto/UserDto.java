package com.taskMangementService.model.dto;

import com.taskMangementService.model.entities.User.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userName;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public boolean isInValidData() {
        return StringUtils.isEmpty(userName) || StringUtils.isEmpty(password) || StringUtils.isEmpty(password);
    }
}
