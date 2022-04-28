package vn.gpay.jitin.core.api.devices;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.devices.DeviceGroupTree;

public class DeviceGroup_getTree_response extends ResponseBase {
	public List<DeviceGroupTree> children;
}
