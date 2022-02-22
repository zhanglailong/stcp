package org.jeecg.modules.service;

import com.alibaba.fastjson.JSON;
import io.ipfs.api.JSONParser;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.entity.PinKeys;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zlf
 */
@Service
@Slf4j
public class IpfsService {

	/**
	 * ipfs的服务器地址和端口
	 */
	 //static IPFSS ipfs = new IPFSS("/ip4/218.94.117.178/tcp/5010");
	 static IPFSS ipfs = new IPFSS("/ip4/192.168.1.242/tcp/5010");
	//static IPFSS ipfs = new IPFSS("/ip4/192.168.10.101/tcp/5010");

	/**
	 * 上传文件夹/文件
	 * @param filePathName 文件路径/文件名
	 * @return
	 * @throws IOException
	 */
	public String upload(String filePathName) throws IOException {
		//判断操作系统是否是Windows
		if (isWindowsOS()) {
			filePathName = "c:"+filePathName;
		}
		//filePathName指的是文件的上传路径+文件名，如D:/1.png
		NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(filePathName));
		List<MerkleNode> nodes = ipfs.add(file);
		List<String> strings = new ArrayList<>();
		nodes.forEach(n->{
			log.info("file:"+JSONParser.toString(n.toString()));
			strings.add(JSONParser.toString(n));
		});
		MerkleNode addResult = ipfs.add(file).get(nodes.size()-1);
		return addResult.hash.toString();
	}

	/**
	 * 下载文件夹/文件
	 * @param filePathName 文件路径/文件名
	 * @param hash hash值
	 * @throws IOException
	 */
	public String download(String filePathName,String hash) throws IOException {
		//判断操作系统是否是Windows
		if (isWindowsOS()) {
			if (!filePathName.contains("c:\\")&&!filePathName.contains("c:/")){
				filePathName = "c:"+filePathName;
			}
		}else {
			if (!filePathName.contains("\\home\\")&&!filePathName.contains("/home/")){
				filePathName = "/home"+filePathName;
			}
		}
		String finalFilePathName = filePathName;
		Multihash filePointer = Multihash.fromBase58(hash);
		//判断是文件还是文件夹
		File fileNode  = new File(filePathName);
		if (fileNode.isDirectory()){
			//文件夹
			List<MerkleNode> nodes = ipfs.ls(filePointer);
			nodes.forEach(n->{
				try {
					Multihash filePointer1 = Multihash.fromBase58(String.valueOf(n.hash));
					log.info("fine Name:"+n.name.get()+",hash:"+n.hash);
					if(!fileNode.exists()){
						fileNode.mkdir();
					}
					File file  = new File(finalFilePathName +n.name.get());
					if(file.isDirectory()) {
						log.info(n.name.get()+"是文件夹");
						file.mkdir();
						download(finalFilePathName +n.name.get()+"\\",String.valueOf(n.hash));
					}else {
						byte[] data = ipfs.cat(filePointer1);
						if(data != null){
							if(file.exists()){
								file.delete();
							}
							FileOutputStream fos = new FileOutputStream(file);
							fos.write(data,0,data.length);
							fos.flush();
							fos.close();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}else {
			//文件
			byte[] data = ipfs.cat(filePointer);
			if(data != null){
				File file  = new File(filePathName);
				if(file.exists()){
					file.delete();
				}
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(data,0,data.length);
				fos.flush();
				fos.close();
			}
		}
		return finalFilePathName;
	}
	
	public String addRecode(String recode) throws IOException {
		NamedStreamable.ByteArrayWrapper byteArray=new NamedStreamable.ByteArrayWrapper(recode.getBytes());
		MerkleNode addResult = ipfs.add(byteArray).get(0);
		System.out.println("MerkleNode:"+ JSONParser.toString(addResult));
		return addResult.hash.toString();
	}
	public String selectRecode(String hash) throws IOException {
		Multihash filePointer = Multihash.fromBase58(hash);
		byte[] data = ipfs.cat(filePointer);
		return new String(data);
	}

	/**
	 * 管理ipfs对象的固定
	 * @return
	 */
	public Map<String, PinKeys> getPins(){
		try {
			Map<String, PinKeys> pins = ipfs.pin();
			pins.forEach((k,v)->{
				System.out.println("pins v:"+ JSON.toJSONString(k)+",v:"+JSON.toJSONString(v));
			});
			return pins;
		}catch (Exception e){
			System.out.println("异常:"+e);
			return null;
		}
	}

	/**
	 * 判断操作系统是否是Windows
	 *
	 * @return
	 */
	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}
}
