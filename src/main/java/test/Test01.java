package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Test01 {

	private static final char[] hexadecimal =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		  'a', 'b', 'c', 'd', 'e', 'f' };
	
	public static void main(String[] args) {
		try {
			test02();
			
			System.out.println(getMD5(new File("test.txt")));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static String getMD5( final File input ) {
		String rtn = null;
		if( input != null ) {
			FileInputStream	fis	= null;
			FileChannel		fc	= null;
			try {
				MessageDigest md5 = MessageDigest.getInstance( "MD5" );
				fis = new FileInputStream( input );
				fc  =fis.getChannel();
				ByteBuffer bb = fc.map( FileChannel.MapMode.READ_ONLY , 0L , fc.size() );
				md5.update( bb );
				byte[] out = md5.digest();
				System.out.println(out.length);
				rtn = byte2hexa( out );
			}
			catch( NoSuchAlgorithmException ex ) {
				String errMsg = "MessageDigestで MD5 インスタンスの作成に失敗しました。[" + input + "]"
							+ ex.getMessage() ;
				throw new RuntimeException( errMsg,ex );
			}
			catch( IOException ex ) {
				String errMsg = "ファイルの読み取りを失敗しました。[" + input + "]"
							+ ex.getMessage() ;
				throw new RuntimeException( errMsg,ex );
			}
			finally {
				try {
					if( fc != null) {
						fc.close();
					}
					if( fis != null ) {
						fis.close();
					}
				}catch(Exception e) {
					// スルー
				}
//				Closer.ioClose( fc );
//				Closer.ioClose( fis );
			}
		}
		return rtn;
	}
	
	public static void test02() throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		try (InputStream is = Files.newInputStream(Paths.get("test.txt"));
				DigestInputStream dis = new DigestInputStream(is, md)) {
			while(dis.read() > 0) {
				
			}
		}
		
		
		byte[] digest = md.digest();
		String str = byte2hexa(digest);
		
		System.out.println(str);
//		
		try(InputStream is = new FileInputStream(new File("test.txt"));
				DigestInputStream dis = new DigestInputStream(is, md)){
			while(dis.read() > 0) {
				
			}
		}
		
		digest = md.digest();
		System.out.println(byte2hexa(digest));
	}

	public static void test01() {
		// TODO 自動生成されたメソッド・スタブ
		File file = new File("test.txt");

		try {
			file.createNewFile();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/** 共通関数 */
	public static String byte2hexa( final byte[] input ) {
		String rtn = null;
		if( input != null ) {
			int len = input.length ;
			char[] ch = new char[len*2];
			for( int i=0; i<len; i++ ) {
				int high = ((input[i] & 0xf0) >> 4);
				int low  = (input[i] & 0x0f);
				ch[i*2]   = hexadecimal[high];
				ch[i*2+1] = hexadecimal[low];
			}
			rtn =  new String(ch);
		}
		return rtn;
	}
}
