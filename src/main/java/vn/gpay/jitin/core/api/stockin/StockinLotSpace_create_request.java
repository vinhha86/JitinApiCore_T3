package vn.gpay.jitin.core.api.stockin;

import vn.gpay.jitin.core.base.RequestBase;

public class StockinLotSpace_create_request extends RequestBase{
	public Long stockinid_link;
	public Long stockinlotid_link;
    public String spaceepcid_link;
    public Integer totalpackage;
}
