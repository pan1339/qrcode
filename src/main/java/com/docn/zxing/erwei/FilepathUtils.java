package com.docn.zxing.erwei;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

public class FilepathUtils {

    /**
     * 解析二维码解析
     */
    public static String analyzeEncode(String path) {
        String content = null;
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码
            System.out.println("图片中内容：  ");
            System.out.println("author： " + result.getText());
            content = result.getText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 生成二维码
     *
     * @param content
     * @throws Exception
     */
    public static String getEncode(String content) throws Exception {
        String mkdir = "D://qRCode/";
        File f = new File(mkdir);
        if (!f.exists())
            f.mkdirs();
        String string = UUIDUtils.getQRCodePath();
        String path = mkdir + string + ".png";
        int width = 300; // 图像宽度
        int height = 300; // 图像高度
        String format = "png";// 图像类型
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
        Path path1 = Paths.get(path);
        MatrixToImageWriter.writeToPath(bitMatrix, format, path1);// 输出图像
        return path;
    }


    public static void main(String[] args) throws Exception {
        String path = getEncode("我是小球小球---");
        System.out.println(path);
        
        String content = analyzeEncode("D://qRCode/test.png");
        System.out.println(content);
    }

}
