package vn.gpay.jitin.core.menu;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IUserMenuService extends Operations<UserMenu>{

	public List<UserMenu>findByUserid(long userid);
}
