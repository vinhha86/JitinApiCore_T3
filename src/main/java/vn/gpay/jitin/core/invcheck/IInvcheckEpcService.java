package vn.gpay.jitin.core.invcheck;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IInvcheckEpcService extends Operations<InvcheckEpc>{

	public List<InvcheckEpc> findEpcBySkuId(long invcheckid_link, long skuid_link);
}
