package vn.gpay.jitin.core.api.devices;

import vn.gpay.jitin.core.base.RequestBase;

public class Devices_getByOrgEPC_Request extends RequestBase {
	public String epc;
	public Long org_governid_link;
}
