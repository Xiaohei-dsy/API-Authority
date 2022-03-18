package com.aiit.authority.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "resource")
@Builder
public class ResourceDO extends BaseDO {

    /**
     * 资源ID.
     */
    private String resourceId;

    /**
     * 资源名称.
     */
    private String resourceName;

    /**
     * 系统ID.
     */
    private String systemId;

    /**
     * 资源内容.
     */
    private String resourceDetail;

    /**
     * 资源类型.
     */
    private Integer resourceType;

    /**
     * 资源描述.
     */
    private String description;
}
