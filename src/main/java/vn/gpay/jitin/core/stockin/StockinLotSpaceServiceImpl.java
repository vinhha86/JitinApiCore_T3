package vn.gpay.jitin.core.stockin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class StockinLotSpaceServiceImpl extends AbstractService<StockinLotSpace> implements IStockinLotSpaceService{

	@Autowired StockinLotSpaceRepository repository; 
	
	@Override
	protected JpaRepository<StockinLotSpace, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	public List<StockinLotSpace> getByLotAndSpace(Long stockinlotid_link, String spaceepcid_link) {
		// TODO Auto-generated method stub
		return repository.getByLotAndSpace(stockinlotid_link, spaceepcid_link);
	}

	@Override
	public List<StockinLotSpace> getByLot(Long stockinlotid_link) {
		// TODO Auto-generated method stub
		return repository.getByLot(stockinlotid_link);
	}

}
