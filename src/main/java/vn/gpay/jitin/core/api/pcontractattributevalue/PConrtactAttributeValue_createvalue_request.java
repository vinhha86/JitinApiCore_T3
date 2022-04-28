package vn.gpay.jitin.core.api.pcontractattributevalue;


import java.util.List;

import vn.gpay.jitin.core.base.RequestBase;

public class PConrtactAttributeValue_createvalue_request extends RequestBase {
	public List<Long> listAdd;
	public long pcontractid_link;
	public long productid_link;
	public long attributeid_link;
}
