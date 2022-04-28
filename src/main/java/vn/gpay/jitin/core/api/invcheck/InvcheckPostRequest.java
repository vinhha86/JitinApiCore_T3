package vn.gpay.jitin.core.api.invcheck;

import java.util.List;

import vn.gpay.jitin.core.invcheck.InvcheckEpc;

public class InvcheckPostRequest {
	public String invcheckcode;
	public List<InvcheckEpc> data ;
}

