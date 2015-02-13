package com.java.xml.saml2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.xml.util.Base64;
/**
 * 解析单点退出时候ssocircle返回的SAMLRequest和SAMLResponse信息
 *
 */
public class DecodeSSOCircleResponseData {
	public static void main(String[] args) throws Exception {
		String message1 = "nZLBbtswDIZfxdA9tqTYjSPEbgMEBQx0w9a0O+xGS7SrwpY8Sd76+FOTJkiHbYdeSf78+ZHcXL+MQ/ITndfWVISllCRopFXa9BV5fLhdlOS63ngYBz6JO9vbOdzjjxl9SKLSeHFMVWR2Rljw2gsDI3oRpNhvP90JnlIxORustANJdlGoDYSD21MIkxdZptWUem+ldnLAVNpR5Pkyi5Gs2X3ZD/YelXYoQzZigO2gwWfncpI0u4pAFJRPUuYMkek251y3vMyLfpWDfFaxyPsZG+MDmFARTlmxoHzB2AOjoigEY+myXH0nybfTJuLY5I1bHMTukvf/uOA9uldEUp8Q47wpxjW3TqseU4MRBgz06GDSm+zS5+T6OfZtdh9xTW6tGyH8u5yl7BDRatEdSgWOoIetUg69J8mr9dcZBt1pdMc7/e1MpHagTfoLTH9zARczJ6Ijw/l/9rF7HLAxCl9qzzu67hTQfE3bjuYFdHxdLHm3KhntroCVqPKrdUvZW7c/9Ofou6+sfwM=&SigAlg=http://www.w3.org/2000/09/xmldsig#rsa-sha1&Signature=TdV1Pa+JQTK4YVQG6y+tUXVFAtLr79x50CfhTtGf/qZB8dxb82CDTD9SNHQQs0gxgw2HQXQVSXQoLpFivY+Mu5bEqkPitDycKF9i9KMYEy1F8k2U0r4V3dnj5rFR94tPU9G+rp0xF/YlOJXgyLg6NtPpfXgQs+1yihTK4gzbLbx0YEBDPEc+IOCdZbaCj20q3vaxBnhJ/KVkLF0QyK31hbOXzFHpJZurbscITkph2BHXicoSnzmyUGT9n0Nyu98bhmex5ctUFjDZN/Cf3z4Mnpru3TwjStjx3xWN5KAMPinEnc1elOSiB2sPGKzdPPTLyJHr697Aub+VTcoTpVHE8g==";
		DecodeSSOCircleResponseData sp = new DecodeSSOCircleResponseData();
		String decodemessage1 = sp.decodeMessage(message1);
		System.out.println(decodemessage1);

	}

	protected String decodeMessage(String message) throws Exception {

		byte[] decodedBytes = Base64.decode(message);
		if (decodedBytes == null) {
			throw new MessageDecodingException("Unable to Base64 decode incoming message");
		}

		ByteArrayInputStream bytesIn = new ByteArrayInputStream(decodedBytes);
		InflaterInputStream inflater = new InflaterInputStream(bytesIn,new Inflater(true));

		byte[] buffer = new byte[1024];

		int offset = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ((offset = inflater.read(buffer)) != -1) {
			out.write(buffer, 0, offset);
		}
		byte[] uncompressed = out.toByteArray();
		return new String(uncompressed);
	}
}
