package vn.gpay.jitin.core.api.users;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.security.GpayUser;


public class UserResponse extends ResponseBase{
	public List<GpayUser> data;
}
