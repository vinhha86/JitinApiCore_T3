package vn.gpay.jitin.core.org;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IOrg_AutoID_Service extends Operations<Org_AutoID>{
	public List<String> getLastID(String Prefix);
}