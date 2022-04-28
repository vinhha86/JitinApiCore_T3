package vn.gpay.jitin.core.invoice;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IInvoiceDService extends Operations<InvoiceD>{

	List<InvoiceD> findBy_ContractAndSku(Long pcontractid_link, Long skuid_link);
}
