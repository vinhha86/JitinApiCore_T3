package vn.gpay.jitin.core.api.invoice;

import java.util.List;

import vn.gpay.jitin.core.invoice.Invoice;
import vn.gpay.jitin.core.invoice.InvoiceD;

public class InvoiceRequest {

	public Invoice invoice;
	public List<InvoiceD> detail;
}
