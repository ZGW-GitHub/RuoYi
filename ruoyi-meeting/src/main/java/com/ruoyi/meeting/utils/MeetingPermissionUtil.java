package com.ruoyi.meeting.utils;

import com.ruoyi.common.core.domain.entity.SysRole;

import java.util.Arrays;
import java.util.List;

/**
 * @author Snow
 */
public class MeetingPermissionUtil {

    private static final List<String> auditTopicRoleList = Arrays.asList("admin", "busMeeting_topicAudit");
    private static final List<String> viewAllTopicRoleList = Arrays.asList("admin", "busMeeting_topicAudit", "busMeeting_meetingManage");

    public static boolean hasAuditPermission(List<SysRole> roleList) {
        return roleList.stream().anyMatch(role -> auditTopicRoleList.contains(role.getRoleKey()));
    }

    public static boolean hasViewAllTopicPermission(List<SysRole> roleList) {
        return roleList.stream().anyMatch(role -> viewAllTopicRoleList.contains(role.getRoleKey()));
    }

}
