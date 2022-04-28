package vn.gpay.jitin.core.deviceout_d;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IDeviceOut_dService extends Operations<DeviceOut_d>{

	List<DeviceOut_d> getByDeviceOut(Long orgrootid_link, Long deviceoutid_link);

}
