package vn.gpay.jitin.core.api.encode;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.tagencode.WareHouse_Encode_EPC;

public class EncodeResponse extends ResponseBase{

	public List<WareHouse_Encode_EPC> data;
}
