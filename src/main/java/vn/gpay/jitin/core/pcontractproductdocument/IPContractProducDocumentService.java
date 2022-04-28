package vn.gpay.jitin.core.pcontractproductdocument;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;


public interface IPContractProducDocumentService extends Operations<PContractProductDocument> {
	public List<PContractProductDocument> getlist_byproduct(long orgrootid_link, long pcontractid_link,long productid_link);
}
