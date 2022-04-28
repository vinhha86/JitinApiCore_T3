package vn.gpay.jitin.core.devicesinvcheck;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IDevicesInvCheckEPCService extends Operations<DevicesInvCheckEPC>{

	public List<DevicesInvCheckEPC> findByDevicesInvCheckId(Long devices_invcheckid_link);
}
