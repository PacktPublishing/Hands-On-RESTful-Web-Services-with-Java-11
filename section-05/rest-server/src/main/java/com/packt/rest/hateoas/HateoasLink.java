package com.packt.rest.hateoas;

public class HateoasLink {
	private String rel;
	private String href;

	public HateoasLink() {}

	public HateoasLink(String rel, String href) {
		this.rel = rel;
		this.href = href;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
}
