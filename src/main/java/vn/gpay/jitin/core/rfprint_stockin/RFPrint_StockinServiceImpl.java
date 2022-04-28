package vn.gpay.jitin.core.rfprint_stockin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import vn.gpay.jitin.core.base.AbstractService;

@Service
public class RFPrint_StockinServiceImpl extends AbstractService<RFPrint_Stockin> implements IRFPrint_StockinService{

	@Autowired
	RFPrint_StockinRepository repository; 
	
	@Override
	protected JpaRepository<RFPrint_Stockin, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
}
