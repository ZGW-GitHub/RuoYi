package com.ruoyi.meeting.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.meeting.controller.resp.TopicInfoDetailResp;
import com.ruoyi.meeting.controller.resp.TopicInfoResp;
import com.ruoyi.meeting.domain.TopicInfo;
import com.ruoyi.meeting.service.TopicInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 议题Controller
 * 
 * @author Snow
 * @date 2026-01-09
 */
@Controller
@RequestMapping("/meeting/topicInfo")
public class TopicInfoController extends BaseController {

    private final String prefix = "meeting/topicInfo";

    @Resource
    private TopicInfoService topicInfoService;

    @RequiresPermissions("meeting:topicInfo")
    @GetMapping()
    public String topicInfo() {
        return prefix + "/topicInfo";
    }

    /**
     * 查询议题列表
     */
    @RequiresPermissions("meeting:topicInfo")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TopicInfo topicInfo) {
        startPage();
        List<TopicInfoResp> list = topicInfoService.selectList(topicInfo);
        return getDataTable(list);
    }

    /**
     * 导出议题列表
     */
    @RequiresPermissions("meeting:topicInfo")
    @Log(title = "议题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TopicInfo topicInfo) {
        List<TopicInfoResp> list = topicInfoService.selectList(topicInfo);
        ExcelUtil<TopicInfoResp> util = new ExcelUtil<>(TopicInfoResp.class);
        return util.exportExcel(list, "议题数据");
    }

    /**
     * 新增议题
     */
    @RequiresPermissions("meeting:topicInfo")
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存议题
     */
    @RequiresPermissions("meeting:topicInfo")
    @Log(title = "议题", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Valid TopicInfo topicInfo, 
                             @RequestParam(value = "fileInfoFiles", required = false) MultipartFile[] fileInfoFiles,
                             @RequestParam(value = "attachmentInfoFiles", required = false) MultipartFile[] attachmentInfoFiles) {
        try {
            return toAjax(topicInfoService.insert(topicInfo, fileInfoFiles, attachmentInfoFiles));
        } catch (Exception e) {
            logger.error("保存议题失败", e);
            return AjaxResult.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 查看议题详情
     */
    @RequiresPermissions("meeting:topicInfo")
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, ModelMap mmap) {
        TopicInfoDetailResp topicInfo = topicInfoService.detail(id);
        mmap.put("topicInfo", topicInfo);
        return prefix + "/detail";
    }

    /**
     * 修改议题
     */
    @RequiresPermissions("meeting:topicInfo")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, ModelMap mmap) {
        TopicInfoDetailResp topicInfo = topicInfoService.detail(id);
        mmap.put("topicInfo", topicInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存议题
     */
    @RequiresPermissions("meeting:topicInfo")
    @Log(title = "议题", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Valid TopicInfo topicInfo,
                              @RequestParam(value = "fileInfoFiles", required = false) MultipartFile[] fileInfoFiles,
                              @RequestParam(value = "attachmentInfoFiles", required = false) MultipartFile[] attachmentInfoFiles,
                              @RequestParam(value = "retainedFileInfo", required = false) String retainedFileInfo,
                              @RequestParam(value = "retainedAttachmentInfo", required = false) String retainedAttachmentInfo) {
        try {
            return toAjax(topicInfoService.update(topicInfo, fileInfoFiles, attachmentInfoFiles, retainedFileInfo, retainedAttachmentInfo));
        } catch (Exception e) {
            logger.error("更新议题失败", e);
            return AjaxResult.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除议题
     */
    @RequiresPermissions("meeting:topicInfo")
    @Log(title = "议题", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(topicInfoService.deleteByIds(ids));
    }

    /**
     * 下载议题文件
     */
    @RequiresPermissions("meeting:topicInfo")
    @GetMapping("/download/{id}")
    public void download(@PathVariable Long id, HttpServletResponse response) {
        try {
            topicInfoService.download(id, response);
        } catch (Exception e) {
            logger.error("下载议题文件失败", e);
        }
    }

    /**
     * 议题选择器页面
     */
    @RequiresPermissions("meeting:topicInfo")
    @GetMapping("/selector")
    public String selector() {
        return prefix + "/selector";
    }

    /**
     * 审核议题
     */
    @RequiresPermissions("meeting:topicInfo:audit")
    @Log(title = "议题审核", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    @ResponseBody
    public AjaxResult audit(@RequestParam(value = "id") Long id,
                      @RequestParam(value = "auditResult") Boolean auditResult,
                      @RequestParam(value = "auditRemark", required = false) String auditRemark) {
        return topicInfoService.audit(id, auditResult, auditRemark);
    }

}
