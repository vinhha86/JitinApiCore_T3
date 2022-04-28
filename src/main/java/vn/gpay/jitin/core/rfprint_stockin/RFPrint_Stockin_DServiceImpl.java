package vn.gpay.jitin.core.rfprint_stockin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;
@Service
public class RFPrint_Stockin_DServiceImpl extends AbstractService<RFPrint_Stockin_D> implements IRFPrint_Stockin_DService{

	@Autowired
	RFPrint_Stockin_DRepository repository; 
	@Override
	protected JpaRepository<RFPrint_Stockin_D, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
}
