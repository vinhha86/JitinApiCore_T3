package vn.gpay.jitin.core.porders_poline;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;
import vn.gpay.jitin.core.porder.POrder;

@Service
public class POrder_POLine_Service extends AbstractService<POrder_POLine> implements IPOrder_POLine_Service{
	@Autowired POrder_POLine_Repo repo;
	@Override
	protected JpaRepository<POrder_POLine, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Long> get_porderid_by_line(Long pcontract_poid_link) {
		// TODO Auto-generated method stub
		return repo.get_porderid_by_line(pcontract_poid_link);
	}
	@Override
	public List<POrder> getporder_by_po(Long pcontract_poid_link) {
		// TODO Auto-generated method stub
		return repo.get_porder_by_line(pcontract_poid_link);
	}
	@Override
	public List<POrder_POLine> get_porderline_by_po(Long pcontract_poid_link) {
		// TODO Auto-generated method stub
		return repo.get_porderline_by_line(pcontract_poid_link);
	}

}
