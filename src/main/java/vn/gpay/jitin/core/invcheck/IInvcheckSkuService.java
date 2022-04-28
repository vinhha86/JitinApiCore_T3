package vn.gpay.jitin.core.invcheck;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IInvcheckSkuService extends Operations<InvcheckSku>{

	public List<InvcheckSku> invcheckSkuGetByInvcheckid_link(Long invcheckid_link);
	public void updateTotalCheck(Long invcheckid_link, Long skuid_link);
}
