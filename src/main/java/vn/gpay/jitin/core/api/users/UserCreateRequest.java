package vn.gpay.jitin.core.api.users;

import java.util.List;

import vn.gpay.jitin.core.base.RequestBase;
import vn.gpay.jitin.core.security.GpayUser;

public class UserCreateRequest extends RequestBase{
	public GpayUser data;
	public List<MenuId> usermenu;
	
}
