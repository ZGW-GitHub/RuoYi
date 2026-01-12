package com.ruoyi.meeting.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.meeting.controller.resp.MeetingInfoPageResp;
import com.ruoyi.meeting.controller.resp.RelatedTopicResp;
import com.ruoyi.meeting.domain.MeetingInfo;
import com.ruoyi.meeting.service.MeetingInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 会议信息Controller
 * 
 * @author Snow
 * @date 2026-01-09
 */
@Controller
@RequestMapping("/meeting/meetingInfo")
public class MeetingInfoController extends BaseController {

    private final String prefix = "meeting/meetingInfo";

    @Resource
    private MeetingInfoService meetingInfoService;

    @RequiresPermissions("meeting:meetingInfo:view")
    @GetMapping()
    public String meetingInfo() {
        return prefix + "/meetingInfo";
    }

    /**
     * 查询会议信息列表
     */
    @RequiresPermissions("meeting:meetingInfo:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(MeetingInfo meetingInfo) {
        startPage();
        List<MeetingInfoPageResp> list = meetingInfoService.selectList(meetingInfo);
        return getDataTable(list);
    }

    /**
     * 导出会议信息列表
     */
    @RequiresPermissions("meeting:meetingInfo:export")
    @Log(title = "会议信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MeetingInfo meetingInfo) {
        List<MeetingInfoPageResp> list = meetingInfoService.selectList(meetingInfo);
        ExcelUtil<MeetingInfoPageResp> util = new ExcelUtil<>(MeetingInfoPageResp.class);
        return util.exportExcel(list, "会议信息数据");
    }

    /**
     * 新增会议信息
     */
    @RequiresPermissions("meeting:meetingInfo:add")
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存会议信息
     */
    @RequiresPermissions("meeting:meetingInfo:add")
    @Log(title = "会议信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(MeetingInfo meetingInfo, @RequestParam(value = "selectedTopicIds", required = false) String selectedTopicIds) {
        try {
            return toAjax(meetingInfoService.insert(meetingInfo, selectedTopicIds));
        } catch (Exception e) {
            logger.error("新增会议信息失败", e);
            return AjaxResult.error("新增失败：" + e.getMessage());
        }
    }

    /**
     * 修改会议信息
     */
    @RequiresPermissions("meeting:meetingInfo:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, ModelMap mmap) {
        MeetingInfo meetingInfo = meetingInfoService.selectById(id);
        mmap.put("meetingInfo", meetingInfo);
        return prefix + "/edit";
    }

    /**
     * 查看会议关联议题页面
     */
    @RequiresPermissions("meeting:meetingInfo:edit")
    @GetMapping("/relatedTopics/{meetingId}")
    public String relatedTopics(@PathVariable String meetingId, ModelMap mmap) {
        // 将meetingId作为字符串传递给前端，避免JavaScript精度丢失
        mmap.put("meetingId", meetingId);
        return prefix + "/relatedTopics";
    }

    /**
     * 获取会议关联的议题
     */
    @RequiresPermissions("meeting:meetingInfo:edit")
    @GetMapping("/getRelatedTopics")
    @ResponseBody
    public AjaxResult getRelatedTopics(@RequestParam("meetingId") String meetingIdStr) {
        try {
            Long meetingId = Long.valueOf(meetingIdStr);
            List<RelatedTopicResp> relatedTopics = meetingInfoService.getRelatedTopics(meetingId);
            return AjaxResult.success(relatedTopics);
        }  catch (Exception e) {
            logger.error("获取关联议题失败", e);
            return AjaxResult.error("获取关联议题失败");
        }
    }

    /**
     * 修改保存会议信息
     */
    @RequiresPermissions("meeting:meetingInfo:edit")
    @Log(title = "会议信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(MeetingInfo meetingInfo, @RequestParam(value = "selectedTopicIds", required = false) String selectedTopicIds) {
        try {
            return toAjax(meetingInfoService.update(meetingInfo, selectedTopicIds));
        } catch (Exception e) {
            logger.error("更新会议信息失败", e);
            return AjaxResult.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除会议信息
     */
    @RequiresPermissions("meeting:meetingInfo:remove")
    @Log(title = "会议信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(meetingInfoService.deleteByIds(ids));
    }
}
