package com.aiit.authority.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user")
public class UserDO extends BaseDO {

    /**
     * 用户名
     */
    private String username;


    /**
     * 密码
     */
    private String password;


    /**
     * 别名
     */
    private String remarkName;

    /**
     * 用户种类：管理员：1，普通用户：0
     *
     * @see com.aiit.authority.enums.UserPositionEnum
     */
    private Integer isManager;

    /**
     * 状态：待审批0，启用1，禁用2
     *
     * @see com.aiit.authority.enums.UserStatusEnum
     */
    private Integer status;

}
