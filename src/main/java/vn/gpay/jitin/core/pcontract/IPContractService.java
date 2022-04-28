package vn.gpay.jitin.core.pcontract;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.gpay.jitin.core.api.pcontract.PContract_getbypaging_request;
import vn.gpay.jitin.core.base.Operations;


public interface IPContractService extends Operations<PContract> {
	public Page<PContract> getall_by_orgrootid_paging(Long orgrootid_link, PContract_getbypaging_request request);
	public List<PContract> getby_code(long orgrootid_link, String contractcode, long pcontractid_link);
	List<Long> getidby_contractcode(String contractcode);
}
