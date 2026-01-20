package com.ruoyi.meeting.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.meeting.domain.DeviceInfo;
import com.ruoyi.meeting.service.DeviceInfoService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    @ResponseBody
    @PostMapping("/saveConnectedWiredDevice")
    public AjaxResult saveConnectedWiredDeviceToDB() {
        try {
            deviceInfoService.saveConnectedWiredDeviceToDB();
            return AjaxResult.success("保存成功");
        } catch (Exception e) {
            log.error("保存失败. 异常: {}", e.getMessage(), e);
            return AjaxResult.error("保存失败：" + e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/commonIp")
    public AjaxResult commonIp() {
        try {
            List<String> commonIpList = deviceInfoService.commonIp();
            return AjaxResult.success("获取成功", commonIpList);
        } catch (Exception e) {
            log.error("获取失败. 异常: {}", e.getMessage(), e);
            return AjaxResult.error("获取IP前缀配置失败：" + e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/updateCommonIp")
    public AjaxResult updateCommonIp(@RequestParam("ip1") String ip1, 
                                   @RequestParam("ip2") String ip2, 
                                   @RequestParam("ip3") String ip3) {
        try {
            deviceInfoService.updateCommonIp(ip1, ip2, ip3);
            return AjaxResult.success("更新成功");
        } catch (Exception e) {
            log.error("更新IP前缀配置失败. 异常: {}", e.getMessage(), e);
            return AjaxResult.error("更新IP前缀配置失败：" + e.getMessage());
        }
    }

}
