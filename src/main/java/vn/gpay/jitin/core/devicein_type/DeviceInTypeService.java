package vn.gpay.jitin.core.devicein_type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class DeviceInTypeService extends AbstractService<DeviceInType> implements IDeviceInTypeService{
	@Autowired IDeviceInTypeRepository repo;
	@Override
	protected JpaRepository<DeviceInType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
