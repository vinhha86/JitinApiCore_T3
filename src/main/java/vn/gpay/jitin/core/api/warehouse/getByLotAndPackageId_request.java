package vn.gpay.jitin.core.api.warehouse;

import vn.gpay.jitin.core.base.RequestBase;

public class getByLotAndPackageId_request extends RequestBase{
	public Long skuid_link;
	public String lotnumber;
	public Integer packageid;
	public Integer status;
}
