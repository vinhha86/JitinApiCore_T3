package vn.gpay.jitin.core.porder_grant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class POrderGrant_Service extends AbstractService<POrder_Grant> implements IPOrderGrant_Service {
	@Autowired IPOrderGrant_Repository repo;
	@Override
	protected JpaRepository<POrder_Grant, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
}
