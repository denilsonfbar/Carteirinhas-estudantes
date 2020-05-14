package Controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

public class CodBarras {
	public static byte[] geraCodigoBarras(String texto) throws FileNotFoundException, IOException {
		final int dpi = 1200;

		// Create the barcode bean
		Code39Bean bean = new Code39Bean();
		// Configure the barcode generator
		bean.setWideFactor(3);
		bean.doQuietZone(true);
		// Open output file
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			// Set up the canvas provider for monochrome PNG output
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(bao, "image/x-png", dpi,
					BufferedImage.TYPE_BYTE_BINARY, false, 0);
			// Generate the barcode
			bean.generateBarcode(canvas, texto);
			// Signal end of generation
			canvas.finish();
			
		} finally {
			bao.close();
		}
		return bao.toByteArray();
	}

}
