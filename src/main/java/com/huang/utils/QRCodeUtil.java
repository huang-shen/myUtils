package com.huang.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.codec.binary.Base64OutputStream;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuangShen
 * @Description 创建二维码
 * @create 2021-10-14 7:26
 */
public class QRCodeUtil {
     // base64编码集
    public static final String CHARSET = "UTF-8";
    // 二维码高度
    public static final int HEIGHT = 150;
    // 二维码宽度
    public static final int WIDTH = 150;
    // 二维码外边距
    public static final int MARGIN = 0;
    // 二维码图片格式
    private static final String FORMAT = "jpg";




    /**
     * 生成二维码
     * @explain
     * @param data 字符串（二维码实际内容）
     * @return
     */
    public static BufferedImage createQRCode(String data) {


        return createQRCode(data, WIDTH, HEIGHT, MARGIN);

    }

    /**
     * 生成二维码
     * @explain
     * @param data 字符串（二维码实际内容）
     * @param width 宽
     * @param height 高
     * @param margin 外边距，单位：像素，只能为整数，否则：报错
     * @return BufferedImage
     */
    public static BufferedImage createQRCode(String data, int width, int height, int margin) {
        BitMatrix matrix;
        try {
            // 设置QR二维码参数
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>(2);
            // 纠错级别（H为最高级别）
            // L级：约可纠错7%的数据码字
            // M级：约可纠错15%的数据码字
            // Q级：约可纠错25%的数据码字
            // H级：约可纠错30%的数据码字
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 字符集
            hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
            // 边框，(num * 10)
            hints.put(EncodeHintType.MARGIN, 0);// num
            // 编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE,
                    width, height, hints);

            // 裁减白边（强制减掉白边）
            if (margin == 0) {
                matrix = deleteWhite(matrix);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return MatrixToImageWriter.toBufferedImage(matrix);
    }


    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }

        int width = resMatrix.getWidth();
        int height = resMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, resMatrix.get(x, y) ? 0 : 255);// 0-黑色;255-白色
            }
        }

        return resMatrix;
    }

    /**
     * 生成带logo的二维码
     * @explain 宽、高、外边距使用定义好的值
     * @param data 字符串（二维码实际内容）
     * @param logoFile logo图片文件对象
     * @return BufferedImage
     */
    public static BufferedImage createQRCodeWithLogo(String data, File logoFile) {
        return createQRCodeWithLogo(data, WIDTH, HEIGHT, MARGIN, logoFile);
    }

    /**
     * 生成带logo的二维码
     * @explain 自定义二维码的宽和高
     * @param data 字符串（二维码实际内容）
     * @param width 宽
     * @param height 高
     * @param logoFile logo图片文件对象
     * @return BufferedImage
     * @return
     */
    public static BufferedImage createQRCodeWithLogo(String data, int width, int height, int margin, File logoFile) {
        BufferedImage combined = null;
        try {
            BufferedImage qrcode = createQRCode(data, width, height, margin);
            BufferedImage logo = ImageIO.read(logoFile);
            int deltaHeight = height - logo.getHeight();
            int deltaWidth = width - logo.getWidth();
            combined = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) combined.getGraphics();
            g.drawImage(qrcode, 0, 0, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g.drawImage(logo, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return combined;
    }



    /**
     * 将二维码信息写入文件中
     * @explain
     * @param image
     * @param file 用于存储二维码的文件对象
     */
    public static void writeToFile(BufferedImage image, File file) {
        try {
            ImageIO.write(image, FORMAT, file);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将二维码信息写入流中
     * @explain
     * @param image
     * @param  stream 文件
     */
    public static void writeToStream(BufferedImage image, OutputStream stream) {
        try {
            ImageIO.write(image, FORMAT, stream);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取base64格式的二维码
     * @explain 图片类型：jpg
     *  展示：<img src="data:image/jpeg;base64,base64Str"/>
     * @param image
     * @return base64
     */
    public static String writeToBase64(BufferedImage image) {
        String base64Str;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            OutputStream os = new Base64OutputStream(bos);
            writeToStream(image, os);
            // 按指定字符集进行转换并去除换行符
            base64Str = bos.toString(CHARSET).replace("\r\n", "");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return "data:image/jpg;base64,"+base64Str;
    }


    /**
     * 将base64转成图片
     * @explain
     * @param base64 base64格式图片
     * @param file 用于存储二维码的文件对象
     */
    public static void base64ToImage(String base64, File file) {
        FileOutputStream os;
        try {
            Base64 d = new Base64();
            byte[] bs = d.decode(base64);
            os = new FileOutputStream(file.getAbsolutePath());
            os.write(bs);
            os.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}