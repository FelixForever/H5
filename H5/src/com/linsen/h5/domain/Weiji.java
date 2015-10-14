package com.linsen.h5.domain;

import java.io.Serializable;

public class Weiji implements Serializable{
	private static final long serialVersionUID = 4865904834163833859L;
	
	private String username;
	private int userid;
	private String basepath;
	private String thumbimage;
	private String audiopath1;
	private String audiopath2;
	private String savetime;
	private String pingtai;
	private String zhanghao;
	private int clicknum;
	private String title;
	private int style;
	private String presign;
	private String allword;
	private int signmusic;

	public String getBasepath() {
		return basepath;
	}

	public void setBasepath(String basepath) {
		this.basepath = basepath;
	}

	public String getThumbimage() {
		return thumbimage;
	}

	public void setThumbimage(String thumbimage) {
		this.thumbimage = thumbimage;
	}

	public String getAudiopath1() {
		return audiopath1;
	}

	public void setAudiopath1(String audiopath1) {
		this.audiopath1 = audiopath1;
	}

	public String getAudiopath2() {
		return audiopath2;
	}

	public void setAudiopath2(String audiopath2) {
		this.audiopath2 = audiopath2;
	}

	public int getSignmusic() {
		return signmusic;
	}

	public void setSignmusic(int signmusic) {
		this.signmusic = signmusic;
	}

	public String getAllword() {
		return allword;
	}

	public void setAllword(String allword) {
		this.allword = allword;
	}

	public String getPresign() {
		return presign;
	}

	public void setPresign(String presign) {
		this.presign = presign;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public int getClicknum() {
		return clicknum;
	}

	public void setClicknum(int clicknum) {
		this.clicknum = clicknum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getSavetime() {
		return savetime;
	}

	public void setSavetime(String savetime) {
		this.savetime = savetime;
	}

	public String getPingtai() {
		return pingtai;
	}

	public void setPingtai(String pingtai) {
		this.pingtai = pingtai;
	}

	public String getZhanghao() {
		return zhanghao;
	}

	public void setZhanghao(String zhanghao) {
		this.zhanghao = zhanghao;
	}
}
