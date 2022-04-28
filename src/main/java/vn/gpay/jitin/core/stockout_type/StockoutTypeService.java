package vn.gpay.jitin.core.stockout_type;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class StockoutTypeService  extends AbstractService<StockoutType> implements IStockoutTypeService {
	@Autowired IStockoutTypeRepository repo;
	@Override
	protected JpaRepository<StockoutType, Long> getRepository() {
		return repo;
	}
	@Override
	public List<StockoutType> findStockoutTypeByIdRange(Long idFrom, Long idTo) {
		return repo.findStockoutTypeByIdRange(idFrom, idTo);
	}

}
