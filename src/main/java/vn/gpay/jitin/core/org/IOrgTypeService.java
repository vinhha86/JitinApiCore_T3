package vn.gpay.jitin.core.org;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IOrgTypeService extends Operations<OrgType>{
	public List<OrgType> findOrgTypeForMenuOrg();
}
