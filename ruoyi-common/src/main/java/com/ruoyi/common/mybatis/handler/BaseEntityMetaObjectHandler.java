package com.ruoyi.common.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Snow
 */
@Slf4j
@Component
public class BaseEntityMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // log.info("start insert fill ....");

        // 插入时设置创建者和创建时间，以及更新时间
        String loginName = Optional.ofNullable(ShiroUtils.getSysUser()).map(SysUser::getLoginName).orElse(StrUtil.EMPTY);
        this.setFieldValByName("createBy", loginName, metaObject);
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        // this.strictInsertFill(metaObject, "createBy", String.class, loginName);
        // this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        // this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        //
        // log.info("insert fill completed, createBy: {}, createTime: {}", loginName, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // log.info("start update fill ....");

        // 更新时设置更新者和更新时间
        String loginName = Optional.ofNullable(ShiroUtils.getSysUser()).map(SysUser::getLoginName).orElse(StrUtil.EMPTY);
        this.setFieldValByName("updateBy", loginName, metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        // this.strictUpdateFill(metaObject, "updateBy", String.class, loginName);
        // this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        //
        // log.info("update fill completed, updateBy: {}, updateTime: {}", loginName, LocalDateTime.now());
    }

}
