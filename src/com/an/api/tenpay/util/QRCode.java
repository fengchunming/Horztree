package com.an.api.tenpay.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * @author Karas
 */
public class QRCode {
	private int imageSize = 512;

	private BufferedImage core(String codeText, int size) {
		try {
			Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(codeText, BarcodeFormat.QR_CODE, size, size, hintMap);
			int CrunchifyWidth = byteMatrix.getWidth();
			BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
			image.createGraphics();

			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
			graphics.setColor(Color.BLACK);

			for (int i = 0; i < CrunchifyWidth; i++) {
				for (int j = 0; j < CrunchifyWidth; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}

			return image;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String generate(String codeText) {
		try {
			BufferedImage bufImg = this.core(codeText, imageSize);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufImg, "png", baos);
			byte[] bytes = baos.toByteArray();
			return Base64.encodeBase64String(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void generate(String codeText, OutputStream output) {
		try {
			BufferedImage bufImg = this.core(codeText, imageSize);
			ImageIO.write(bufImg, "png", output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generate(String codeText, String path) {
		try {
			BufferedImage bufImg = this.core(codeText, imageSize);
			File imgFile = new File(path);
			ImageIO.write(bufImg, "png", imgFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}