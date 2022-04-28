package vn.gpay.jitin.core.stockin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class StockinProductServiceImpl extends AbstractService<StockinProduct> implements IStockinProductService{
	@Autowired
	StockinProductRepository repository;

	@Override
	protected JpaRepository<StockinProduct, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	
}
