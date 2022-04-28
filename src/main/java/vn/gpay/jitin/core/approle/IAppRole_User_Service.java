package vn.gpay.jitin.core.approle;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IAppRole_User_Service extends Operations<AppRole_User> {
	public List<AppRole_User> getby_user_and_role(Long userid, Long roleid_link);
}
