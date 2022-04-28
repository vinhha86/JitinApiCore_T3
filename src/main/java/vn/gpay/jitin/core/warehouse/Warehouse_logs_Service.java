package vn.gpay.jitin.core.warehouse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.StringAbstractService;

@Service
public class Warehouse_logs_Service extends StringAbstractService<Warehouse_logs> implements IWarehouse_logs_Service{

	@Autowired
	Warehouse_logs_Repository repositoty;

	@Override
	protected JpaRepository<Warehouse_logs, String> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

}
