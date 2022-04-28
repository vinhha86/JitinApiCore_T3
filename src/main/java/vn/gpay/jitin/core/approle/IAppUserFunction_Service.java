package vn.gpay.jitin.core.approle;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IAppUserFunction_Service extends Operations<AppUserFunction> {
	public List<AppUserFunction> getby_function_and_user(Long functionid_link, Long userid_link);
}
