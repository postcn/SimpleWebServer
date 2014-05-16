


public class GetRequest extends Request throws AccessDeniedException {
	
	String resourcePath;

	public GetRequest(String firstHeader) {
		this.resourcePath = firstHeader.split(" ")[1];
	}

	@Override
	public String getFullHeader() {
		return "GET " + resourcePath + " HTTP/1.1" + Constants.NEWLINE + super.getCommonHeader();
	}
	
}
