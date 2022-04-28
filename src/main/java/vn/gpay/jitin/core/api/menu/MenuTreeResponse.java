package vn.gpay.jitin.core.api.menu;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.menu.MenuTree;
import vn.gpay.jitin.core.security.GpayUser;

public class MenuTreeResponse extends ResponseBase{
	public List<MenuTree> children;
	public GpayUser data;
//	public MenuTree data;
}
