package com.aiit.authority.manager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.aiit.authority.BaseTest;
import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.dto.ListSystemDTO;
import com.aiit.authority.repository.dao.SystemDao;
import com.aiit.authority.repository.entity.SystemDO;

public class SystemManagerTest extends BaseTest {

    @Resource
    private SystemDao systemDao;

    @Resource
    private SystemManager systemManager;

    @Test
    @Transactional
    @Rollback
    public void insertSystemTest() {
        systemManager.insertSystem("etl-test", "test");
        List<SystemDO> systemInfoList = systemDao.selectList(null);
        SystemDO systemDO = systemInfoList.get(0);
        Assertions.assertEquals(systemDO.getSystemId(), "etl-test");
        Assertions.assertEquals(systemDO.getDescription(), "test");
        Assertions.assertThrows(DatabaseException.class, () -> systemManager.insertSystem("etl-test", "test"));
    }

    @Test
    @Transactional
    @Rollback
    public void getSystemTest() {
        systemManager.insertSystem("testName", "test");
        Assertions.assertNotNull(systemManager.getSystem("testName"));
    }

    @Test
    @Transactional
    @Rollback
    public void deleteSystemTest(){
        systemManager.insertSystem("testSystem","test");
        systemManager.deleteSystem("testSystem");
        SystemDO systemDO=systemManager.getSystem("testSystem");
        Assertions.assertEquals(systemDO,null);

        //重复删除
        Assertions.assertTrue(systemManager.deleteSystem("testSystem"));
    }

    @Test
    @Transactional
    @Rollback
    public void updateSystemTest(){
        systemManager.insertSystem("testSystem","test");
        systemManager.updateSystem("testSystem","Revised");
        SystemDO systemDO=systemManager.getSystem("testSystem");
        Assertions.assertEquals(systemDO.getDescription(),"Revised");

        //重复修改
        Assertions.assertTrue(systemManager.updateSystem("testSystem","Revised"));
    }

    @Test
    @Transactional
    @Rollback
    public void listSystemTest() {

        // 插入4条数据，查第一页，每页3条。
        SystemDO systemDO1 = creatEntity("test1", "test1");
        systemDao.insert(systemDO1);
        SystemDO systemDO2 = creatEntity("test2", "test2");
        systemDao.insert(systemDO2);
        SystemDO systemDO3 = creatEntity("test3", "test3");
        systemDao.insert(systemDO3);
        // 插入几条数据可能在同一秒内完成，可能无法根据时间准确排序，手动设置延时，用于后续验证数据准确性。
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
        SystemDO systemDO4 = creatEntity("test4", "test4");
        systemDao.insert(systemDO4);

        ListSystemDTO listSystemDTO = systemManager.listSystem(1, 3);
        List<SystemDO> list = listSystemDTO.getList();
        PageInfo pageInfo = listSystemDTO.getPageInfo();

        // 当前页面为1，每页展示为2，总页数应为2，数据总条数为4。
        Assertions.assertEquals(pageInfo.getCurrentPage(), (long)1);
        Assertions.assertEquals(pageInfo.getPageSize(), (long)3);
        Assertions.assertEquals(pageInfo.getTotalSize(), (long)4);
        Assertions.assertEquals(pageInfo.getTotalPage(), (long)2);

        // 查第一页，下一页存在，上一页不存在。
        Assertions.assertTrue(pageInfo.getHasNext());
        Assertions.assertFalse(pageInfo.getHasPrevious());

        // 第一页数据条数为3
        Assertions.assertEquals(list.size(), 3);

        // 验证提取数据正确性，分页查询为逆时间序，第一个数据应为最后插入的数据。
        Assertions.assertEquals(list.get(0).getSystemId(), "test4");
    }

    private SystemDO creatEntity(String systemId, String description) {
        SystemDO systemDO = new SystemDO();
        systemDO.setSystemId(systemId);
        systemDO.setDescription(description);
        return systemDO;
    }

}