
public class HeadRequest extends GetRequest {

	public HeadRequest(String firstHeader) {
		super(firstHeader);
	}

	@Override
	public String getFullHeader() {
		return "HEAD " + resourcePath + " HTTP/1.1" + Constants.NEWLINE + super.getCommonHeader();
	}
}
