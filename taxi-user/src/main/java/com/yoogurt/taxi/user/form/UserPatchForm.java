package com.yoogurt.taxi.user.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPatchForm {
    private String avatar;
    private String password;
    private Long userId;
    private Integer userStatus;
}
