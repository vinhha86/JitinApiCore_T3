package vn.gpay.jitin.core.invoice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class InvoiceDRepositoryImpl extends AbstractService<InvoiceD> implements IInvoiceDService{

	@Autowired
	InvoiceDRepository repositoty;

	@Override
	protected JpaRepository<InvoiceD, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<InvoiceD> findBy_ContractAndSku(Long pcontractid_link, Long skuid_link){
		return repositoty.findBy_ContractAndSku(pcontractid_link, skuid_link);
	}
}
