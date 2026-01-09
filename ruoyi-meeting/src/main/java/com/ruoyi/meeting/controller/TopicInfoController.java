package com.ruoyi.meeting.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.meeting.domain.TopicInfo;
import com.ruoyi.meeting.service.TopicInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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

    @RequiresPermissions("meeting:topicInfo:view")
    @GetMapping()
    public String topicInfo() {
        return prefix + "/topicInfo";
    }

    /**
     * 查询议题列表
     */
    @RequiresPermissions("meeting:topicInfo:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TopicInfo topicInfo) {
        startPage();
        List<TopicInfo> list = topicInfoService.selectList(topicInfo);
        return getDataTable(list);
    }

    /**
     * 导出议题列表
     */
    @RequiresPermissions("meeting:topicInfo:export")
    @Log(title = "议题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TopicInfo topicInfo) {
        List<TopicInfo> list = topicInfoService.selectList(topicInfo);
        ExcelUtil<TopicInfo> util = new ExcelUtil<>(TopicInfo.class);
        return util.exportExcel(list, "议题数据");
    }

    /**
     * 新增议题
     */
    @RequiresPermissions("meeting:topicInfo:add")
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存议题
     */
    @RequiresPermissions("meeting:topicInfo:add")
    @Log(title = "议题", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TopicInfo topicInfo, 
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
     * 修改议题
     */
    @RequiresPermissions("meeting:topicInfo:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, ModelMap mmap) {
        TopicInfo topicInfo = topicInfoService.selectById(id);
        mmap.put("topicInfo", topicInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存议题
     */
    @RequiresPermissions("meeting:topicInfo:edit")
    @Log(title = "议题", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TopicInfo topicInfo,
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
    @RequiresPermissions("meeting:topicInfo:remove")
    @Log(title = "议题", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(topicInfoService.deleteByIds(ids));
    }

    /**
     * 下载议题文件
     */
    @RequiresPermissions("meeting:topicInfo:view")
    @GetMapping("/download/{id}")
    public void download(@PathVariable Long id, HttpServletResponse response) {
        try {
            topicInfoService.download(id, response);
        } catch (Exception e) {
            logger.error("下载议题文件失败", e);
        }
    }

}
