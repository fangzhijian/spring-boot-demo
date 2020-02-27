package com.example.boot;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 2020/2/27 15:14
 * fzj
 */
@Data
@Accessors(chain = true)
public class ColumnAndType {

    private String column;
    private String type;
}
