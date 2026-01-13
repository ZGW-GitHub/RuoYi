package com.ruoyi.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 会议信息对象 bus_meeting_info
 *
 * @author Snow
 * @date 2026-01-09
 */
@Data
@TableName("bus_meeting_info")
@EqualsAndHashCode(callSuper = true)
public class MeetingInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    @Excel(name = "标题")
    @NotBlank(message = "标题不能为空")
    @Size(max = 30, message = "标题长度不能超过30个字符")
    private String title;

    /**
     * 类型
     */
    private String meetingType;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String meetingStatus;

    /**
     * 会议时间
     */
    @Excel(name = "会议时间")
    private String meetingTime;

    /**
     * 主持人
     */
    @Excel(name = "主持人")
    @Size(max = 30, message = "主持人长度不能超过30个字符")
    private String meetingHost;

    /**
     * 参会情况
     */
    @Size(max = 200, message = "参会情况长度不能超过200个字符")
    private String participationInfo;

    /**
     * 议题文件信息
     */
    private String fileInfo;

    /**
     * 附件信息
     */
    private String attachmentInfo;

    /**
     * 扩展信息
     */
    private String extInfo;

    /**
     * 序号
     */
    @Excel(name = "序号")
    private Long orderNo;

    /**
     * 删除标志（0代表存在）
     */
    @TableLogic(value = "0", delval = "id")
    private Long deleted;

}
