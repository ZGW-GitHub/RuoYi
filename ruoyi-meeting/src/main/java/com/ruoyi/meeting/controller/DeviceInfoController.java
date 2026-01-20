package com.ruoyi.meeting.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.meeting.domain.DeviceInfo;
import com.ruoyi.meeting.service.DeviceInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 设备信息Controller
 * 
 * @author Snow
 * @date 2026-01-20
 */
@Controller
@RequestMapping("/meeting/deviceInfo")
public class DeviceInfoController extends BaseController {

    private final String prefix = "meeting/deviceInfo";

    @Resource
    private DeviceInfoService deviceInfoService;

    @RequiresPermissions("meeting:deviceInfo")
    @GetMapping()
    public String deviceInfo() {
        return prefix + "/deviceInfo";
    }

    /**
     * 查询设备信息列表
     */
    @RequiresPermissions("meeting:deviceInfo")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DeviceInfo deviceInfo) {
        startPage();
        List<DeviceInfo> list = deviceInfoService.selectList(deviceInfo);
        return getDataTable(list);
    }

    /**
     * 导出设备信息列表
     */
    @RequiresPermissions("meeting:deviceInfo")
    @Log(title = "设备信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(DeviceInfo deviceInfo) {
        List<DeviceInfo> list = deviceInfoService.selectList(deviceInfo);
        ExcelUtil<DeviceInfo> util = new ExcelUtil<>(DeviceInfo.class);
        return util.exportExcel(list, "设备信息数据");
    }

    /**
     * 新增设备信息
     */
    @RequiresPermissions("meeting:deviceInfo")
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存设备信息
     */
    @RequiresPermissions("meeting:deviceInfo")
    @Log(title = "设备信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Valid DeviceInfo deviceInfo) {
        return toAjax(deviceInfoService.insert(deviceInfo));
    }

    /**
     * 修改设备信息
     */
    @RequiresPermissions("meeting:deviceInfo")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        DeviceInfo deviceInfo = deviceInfoService.selectById(id);
        mmap.put("deviceInfo", deviceInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存设备信息
     */
    @RequiresPermissions("meeting:deviceInfo")
    @Log(title = "设备信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Valid DeviceInfo deviceInfo) {
        return toAjax(deviceInfoService.update(deviceInfo));
    }

    /**
     * 删除设备信息
     */
    @RequiresPermissions("meeting:deviceInfo")
    @Log(title = "设备信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(deviceInfoService.deleteByIds(ids));
    }

    @ResponseBody
    @PostMapping("checkConnection")
    public AjaxResult checkConnection(@RequestParam(value = "deviceId", required = false) Long deviceId) {
        try {
            deviceInfoService.checkConnection(deviceId);
            return AjaxResult.success("检测成功");
        } catch (Exception e) {
            return AjaxResult.error("检测失败：" + e.getMessage());
        }
    }

}
