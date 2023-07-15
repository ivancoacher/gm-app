package com.jsnjwj.facade.vo;

import lombok.Data;

import java.util.List;

@Data
public class RouterVo {
 
    /** 路径 */
    private String path;
    /** 装饰;使用哪个layout装饰 */
    private String component;
    /** 路由名字 */
    private String name;
    /** 重定向地址;重定向地址，在面包屑中点击会重定向去的地址 */
    private String redirect;
    /** 是否一直显示根路由 */
    private Boolean alwaysShow;
    /** 是否显示;如果设置为true，项目将不会显示在侧栏中(默认为false) */
    private Boolean hidden;
    /** meta_id */
    private RouterMetaVo meta;
    /** 子路由*/
    private List<RouterVo> children;
 
}