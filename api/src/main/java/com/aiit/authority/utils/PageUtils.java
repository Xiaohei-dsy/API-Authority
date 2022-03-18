package com.aiit.authority.utils;

import com.aiit.authority.controller.response.PageInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class PageUtils {
    /**
     *获取查询后的Page信息并封装到VO中
     */
    public static PageInfo getPageInfo(Page page) {
        PageInfo pageInfo = new PageInfo() {
            {
                setCurrentPage(page.getCurrent());
                setTotalPage(page.getPages());
                setTotalSize(page.getTotal());
                setPageSize(page.getSize());
                setHasNext(page.hasNext());
                setHasPrevious(page.hasPrevious());
            }
        };
        return pageInfo;
    }

}
