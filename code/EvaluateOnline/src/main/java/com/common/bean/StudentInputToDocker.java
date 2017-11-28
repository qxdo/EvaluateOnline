package com.common.bean;
/**
*  dir    题号
*  headername   头文件名称
*  header    头文件代码
*  srcname    源文件名称
*  src        源文件代码

*/
public class StudentInputToDocker {
	private String dir = "";
	private String headername  = "";
	private String header = "";
	private String srcname = "";
	private String src = "";

	public StudentInputToDocker(String dir, String headername, String header, String srcname, String src) {
		super();
		this.dir = dir;
		this.headername = headername;
		this.header = header;
		this.srcname = srcname;
		this.src = src;
		
	}
	public StudentInputToDocker() {
		super();
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public String getHeadername() {
		return headername;
	}
	public void setHeadername(String headername) {
		this.headername = headername;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getSrcname() {
		return srcname;
	}
	public void setSrcname(String srcname) {
		this.srcname = srcname;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}

	@Override
	public String toString() {
		return "StudentInputToDocker [dir=" + dir + ", headername=" + headername + ", header=" + header + ", srcname="
				+ srcname + ", src=" + src + "]";
	}
}
