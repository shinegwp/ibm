package com.shine.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.shine.po.MyFile;
import com.shine.service.DispatcherBaseOperate;
import com.shine.utils.BeanFactory;

/**
 * @author 高伟鹏
 * @email gaoweipeng3@gmail.com
 * @version 创建时间：2018年10月29日 下午3:11:11
 * @describe
 */
public class Handler implements Runnable {

	private DispatcherBaseOperate dispatcher;
    Lock lock = new ReentrantLock();
	public Handler() {
		this.dispatcher = BeanFactory.getBean("dispatcher");
	}

	@Override
	public void run() {
		MyFile flag = dispatcher.take();
		lock.lock();
		System.out.println("这是我获取的数据" + flag);
		switch (flag.operate) {
		case COPY:
			copy(flag);
			break;
		case CUT:
			cut(flag);
			break;
		case DELETE:
			delete(flag);
			break;
		}
		lock.unlock();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void copy(MyFile myFile) {
		RandomAccessFile raf1 = null;
		RandomAccessFile raf2 = null;
		FileChannel channel2 = null;
		FileChannel channel1 = null;
		try {
			System.out.println(myFile.operate.toString());
			raf1 = new RandomAccessFile(myFile.operate.getFilePath(), "rw");
			// 1. 获取通道
			channel1 = raf1.getChannel();
			// 2. 分配指定大小的缓冲区
			ByteBuffer buf1 = ByteBuffer.allocate(100);
			ByteBuffer buf2 = ByteBuffer.allocate(1024);
			// 3. 分散读取
			ByteBuffer[] bufs = { buf1, buf2 };
			channel1.read(bufs);

			for (ByteBuffer byteBuffer : bufs) {
				// 创建新的字节缓冲区
				byteBuffer.flip();
			}
			// 4. 聚集写入
			raf2 = new RandomAccessFile(myFile.operate.getTargetPath(), "rw");
			channel2 = raf2.getChannel();
			channel2.write(bufs);
			System.out.println("复制操作成功");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (channel2 != null) {
					channel2.close();
				}
				if (raf2 != null) {
					raf2.close();
				}
				if (channel1 != null) {
					channel1.close();
				}
				if (raf1 != null) {
					raf1.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public  void delete(MyFile myFile) {
		System.out.println(myFile.operate.toString());
		File file = new File(myFile.operate.getFilePath());
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				System.out.println("删除操作成功！");
			} else {
				System.out.println("删除操作失败！");
			}
		} else {
			System.out.println("删除单个文件不存在！" + myFile.operate.getFilePath());
		}
	}

	public void cut(MyFile myFile) {
		FileOutputStream outputStream = null;
		InputStream inputStream = null;
		byte[] bytes = new byte[1024];
		int temp = 0;
		try {
			System.out.println(myFile.operate.getFilePath());
			inputStream = new FileInputStream(myFile.operate.getFilePath());
			outputStream = new FileOutputStream(myFile.operate.getTargetPath());
			while ((temp = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, temp);
				outputStream.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        File file = new File(myFile.operate.getFilePath());
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				System.out.println("剪切操作成功");
			} else {
				System.out.println("剪切过程中删除操作失败！");
			}
		} else {
			System.out.println("剪切文件不存在！" + myFile.operate.getFilePath());
		}
	}
}
