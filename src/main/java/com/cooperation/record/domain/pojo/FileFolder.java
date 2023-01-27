package com.cooperation.record.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cyl
 * @date 2021/11/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileFolder {

    private int userId;
    private String fileFolderName;
    private String aheadFolderName;

}
