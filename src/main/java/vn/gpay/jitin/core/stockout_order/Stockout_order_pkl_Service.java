package vn.gpay.jitin.core.stockout_order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class Stockout_order_pkl_Service extends AbstractService<Stockout_order_pkl> implements IStockout_order_pkl_Service{
	@Autowired Stockout_order_pkl_repository repo;
	@Override
	protected JpaRepository<Stockout_order_pkl, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Stockout_order_pkl> getByStockoutOrderDId(Long stockoutorderdid_link) {
		// TODO Auto-generated method stub
		return repo.getByStockoutOrderDId(stockoutorderdid_link);
	}
	@Override
	public List<Stockout_order_pkl> getByLotnumberAndPackageId(Long stockoutorderdid_link, String lotnumber,
			Integer packageid) {
		return repo.getByLotnumberAndPackageId(stockoutorderdid_link, lotnumber, packageid);
	}
	@Override
	public List<Stockout_order_pkl> getByStockoutOrderDId_lotnumber(Long stockoutorderdid_link, String lotnumber) {
		return repo.getByStockoutOrderDId_lotnumber(stockoutorderdid_link, lotnumber);
	}
	@Override
	public List<Stockout_order_pkl> getByStockoutOrderDId_epc(Long stockoutorderdid_link, String epc) {
		return repo.getByStockoutOrderDId_epc(stockoutorderdid_link, epc);
	}

}
