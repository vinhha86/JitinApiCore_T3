package vn.gpay.jitin.core.tagencode;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IWareHouse_Encode_EPC_History_Service extends Operations<WareHouse_Encode_EPC_History> {
	public List<WareHouse_Encode_EPC_History> get_epc_his(String epc, long orgencodeid_link);
}
