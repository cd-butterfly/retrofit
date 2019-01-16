package com.bty.retrofit.demo.bean;


import com.bty.retrofit.net.bean.JsonBeanRequest;

/**
 *
 */
public class PostBean extends JsonBeanRequest {

	public String City;

	public String Platform;


	public PostBean() {
		super();
		this.Platform = "test";
	}

	public String getCity() {
		return "shanghai";
	}

	public void setCity(String city) {
		City = city;
	}

	public String getPlatform() {
		return Platform;
	}

	public void setPlatform(String platform) {
		Platform = platform;
	}
}
