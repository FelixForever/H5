package com.linsen.h5.photopicker;

import java.io.Serializable;

public class ItemImageInfo implements Serializable {
	private static final long serialVersionUID = 2264159504184872800L;
	public long imageId;
	public String filePath;
	public long size;
    public String orientation;
	
	public boolean isChecked = false;

}
