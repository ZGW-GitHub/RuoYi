package com.ruoyi.meeting.utils;

import com.ruoyi.common.core.domain.entity.SysRole;

import java.util.Arrays;
import java.util.List;

/**
 * @author Snow
 */
public class AuditUtil {

    private static final List<String> auditTopicRoleList = Arrays.asList("admin", "bus_meeting_audit");

    public static boolean hasAuditPermission(List<SysRole> roleList) {
        return roleList.stream().anyMatch(role -> auditTopicRoleList.contains(role.getRoleKey()));
    }

}
