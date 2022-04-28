package vn.gpay.jitin.core.warehouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class EpcWarehouseCheckRepositoryImpl extends AbstractService<EpcWarehouseCheck> implements IEpcWarehouseCheckService{

	@Autowired
	EpcWarehouseCheckRepository repositoty;

	@Override
	protected JpaRepository<EpcWarehouseCheck, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	

}
