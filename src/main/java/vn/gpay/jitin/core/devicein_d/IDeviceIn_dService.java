package vn.gpay.jitin.core.devicein_d;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IDeviceIn_dService extends Operations<DeviceIn_d>{

	List<DeviceIn_d> getByDeviceIn(Long orgrootid_link, Long deviceinid_link);

}
