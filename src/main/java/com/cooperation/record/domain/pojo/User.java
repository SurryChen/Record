package com.cooperation.record.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cyl
 * @date 2021/10/06
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;
    private String username;
    private String mailbox;
    private String password;
    private String birthday;
    private String picture;
    private String encourage;
    private String otherAccount;
    private String otherPassword;
}
