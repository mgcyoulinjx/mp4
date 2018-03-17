package com.youlin.mp4;

public class News
{
	private String imageUrl;
	private String title;
	private String summary;

	public News()
	{

	}


	public News(String imageUrl, String title, String summary)
	{
		this.imageUrl = imageUrl;
		this.title = title;
		this.summary = summary;
	}


	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	public String getImageUrl()
	{
		return imageUrl;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public String getSummary()
	{
		return summary;
	}
}
