package com.aiit.authority.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "logs")
public class LogDO extends BaseDO implements Serializable {

    /**
     * 操作者
     */
    private String operator;


    /**
     * 操作种类：0插入，1更新，2删除
     */
    private Integer operation;


    /**
     * 操作详情
     */
    private String record;

}
