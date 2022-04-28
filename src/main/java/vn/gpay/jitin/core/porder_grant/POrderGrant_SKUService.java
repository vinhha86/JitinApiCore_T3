package vn.gpay.jitin.core.porder_grant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class POrderGrant_SKUService extends AbstractService<POrderGrant_SKU> implements IPOrderGrant_SKUService {
	@Autowired IPOrderGrant_SKURepository repo;
	@Override
	protected JpaRepository<POrderGrant_SKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
}
