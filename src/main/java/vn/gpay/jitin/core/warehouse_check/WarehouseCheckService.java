package vn.gpay.jitin.core.warehouse_check;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class WarehouseCheckService extends AbstractService<WarehouseCheck> implements IWarehouseCheckService{
	@Autowired WarehouseCheckRepository repo;
	
	@Override
	protected JpaRepository<WarehouseCheck, Long> getRepository() {
		return repo;
	}

	@Override
	public List<WarehouseCheck> findByStockoutOrderAndSku(Long stockoutorderid_link, Long skuid_link) {
		return repo.findByStockoutOrderAndSku(stockoutorderid_link, skuid_link);
	}

	@Override
	public List<WarehouseCheck> findBy_Warehouse_StockoutOrder(Long stockoutorderid_link, Long warehouseid_link) {
		return repo.findBy_Warehouse_StockoutOrder(stockoutorderid_link, warehouseid_link);
	}

}
