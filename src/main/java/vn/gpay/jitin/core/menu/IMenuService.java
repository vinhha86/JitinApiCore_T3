package vn.gpay.jitin.core.menu;

import java.util.List;

import vn.gpay.jitin.core.base.StringOperations;



public interface IMenuService extends StringOperations<Menu>{
	
	public List<Menu>findByUserid(long userid);
	public List<MenuTree> createTree( List<Menu> nodes);
	public List<Menu> getMenu_byRole(long roleid_link);
	public List<Menu> getby_parentid(String menu_parentid);
}
