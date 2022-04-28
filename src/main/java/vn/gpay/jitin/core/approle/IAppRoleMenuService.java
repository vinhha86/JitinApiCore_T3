package vn.gpay.jitin.core.approle;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;
import vn.gpay.jitin.core.menu.Menu;

public interface IAppRoleMenuService extends Operations<AppRoleMenu> {
	public List<String> getmenuid_byrole(Long roleid_link);
	public AppRoleMenu getRoleMenu_by_MenuAndRole(String menuid_link, long roleid_link);
}
