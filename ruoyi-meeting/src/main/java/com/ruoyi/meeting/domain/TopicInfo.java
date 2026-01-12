package com.ruoyi.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 议题对象 bus_topic_info
 * 
 * @author Snow
 * @date 2026-01-09
 */
@Data
@TableName("bus_topic_info")
@EqualsAndHashCode(callSuper = true)
public class TopicInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    @TableId
    private Long id;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 类型 */
    private String topicType;

    /** 状态 */
    @Excel(name = "状态")
    private String topicStatus;

    /** 汇报人 */
    @Excel(name = "汇报人")
    private String reportPeople;

    /** 汇报单位 */
    @Excel(name = "汇报单位")
    private String reportUnit;

    /** 文件信息 */
    private String fileInfo;

    /** 附件信息 */
    private String attachmentInfo;

    /** 扩展信息 */
    private String extInfo;

    /** 序号 */
    @Excel(name = "序号")
    private Long orderNo;

    /** 删除标志（0代表存在） */
    @TableLogic(value = "0", delval = "id")
    private Long deleted;

}
