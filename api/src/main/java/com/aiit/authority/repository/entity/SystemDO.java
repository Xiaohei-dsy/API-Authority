
package com.aiit.authority.repository.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "system")
public class SystemDO extends BaseDO {
    /**
     * 系统名称
     */
    @TableField("system_id")
    private String systemId;
    /**
     * 功能描述
     */
    private String description;

}
