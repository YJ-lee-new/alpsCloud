package com.alps.provider.system.api;

import java.util.List;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.alps.provider.system.domain.SysConfig;

/**
 * 参数配置 信息操作处理
 * 
 * @author : yujie.lee
 */
public interface ISysConfigClient
{

    /**
     * 查询参数配置列表
     */
    @PostMapping("/list")
    public List<SysConfig> selectConfigList(SysConfig config);

    /**
     * 新增保存参数配置
     */
    @PostMapping("/add")
    public int addSave(SysConfig config);

    /**
     * 修改参数配置
     */
    @GetMapping("/edit/{configId}")
    public ModelMap edit(@PathVariable("configId") Long configId, ModelMap mmap);

    /**
     * 修改保存参数配置
     */
    @PostMapping("/edit")
    public int editSave(SysConfig config);

    /**
     * 删除参数配置
     */
    @PostMapping("/remove")
    public int remove(String ids);

    /**
     * 校验参数键名
     */
    @PostMapping("/checkConfigKeyUnique")
    public String checkConfigKeyUnique(SysConfig config);
}
