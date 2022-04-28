package vn.gpay.jitin.core.devicesinvcheck;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IDevicesInvCheckService  extends Operations<DevicesInvCheck>{

	public List<DevicesInvCheck> findByOrgCheckId(Long orgcheckid_link);
	
	public List<DevicesInvCheck> findByOrgCheckIdActive(Long orgcheckid_link);
}
