package com.jsnjwj.facade.vo;

import lombok.Data;

@Data
public class RouterMeta {

	/** id */
	private String id;

	/** 路由id */
	private String routerId;

	/** 名称;设置该路由在侧边栏和面包屑中展示的名字 */
	private String title;

	/** 侧栏图标 */
	private String icon;

	/** 固定在tags */
	private Boolean affix;

	/** 缓存;如果设置为true，则不会被 <keep-alive> 缓存 */
	private Boolean noCache;

	/** 路由控制角色集;控制页面角色(可以设置多个角色) */
	private String roles;

	/** 面包屑中显示;是否隐藏在breadcrumb中 */
	private Boolean breadcrumb;

	/** 高亮侧边栏路由 */
	private String activeMenu;

}